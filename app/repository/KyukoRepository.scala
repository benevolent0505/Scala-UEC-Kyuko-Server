package repository

import java.nio.charset.Charset

import dispatch.Http
import models._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import scalikejdbc._
import services.{LectureService, TeacherServiceImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by mikio on 2017/03/18.
 */

trait UsesKyukoRepository {
  val kyukoRepository: KyukoRepository
}

trait KyukoRepository {
  def find(date: LocalDateTime): Seq[KyukoDate]

  def find(start: LocalDateTime, end: LocalDateTime): Seq[KyukoDate]

  def find(lecture: Lecture): Seq[KyukoDate]

  def find(teacher: Teacher): Seq[KyukoDate]

  def find(lecture: Lecture, date: LocalDateTime): Option[KyukoDate]

  def fetch(school: School): Future[Seq[(Teacher, Lecture, KyukoDate)]]

  def store(list: Seq[(Teacher, Lecture, KyukoDate)]): Seq[(Teacher, Lecture, KyukoDate)]
}

trait MixInKyukoRepository {
  val kyukoRepository = KyukoRepositoryImpl
}

object KyukoRepositoryImpl extends KyukoRepository {

  def * = (rs: WrappedResultSet) => KyukoDate(
    rs.long("id"),
    LectureService.findById(rs.long("lecture_id")).get,
    rs.jodaLocalDateTime("date"),
    rs.jodaLocalDateTime("created_at")
  )

  def insert(kyukoDate: KyukoDate): KyukoDate = DB localTx { implicit s =>
    val id =
      sql"""
           insert into KyukoDays
             (lecture_id, date, created_at)
           values
             (${kyukoDate.lecture.id}, ${kyukoDate.date}, ${kyukoDate.createdAt})
        """.updateAndReturnGeneratedKey.apply()

    KyukoDate(id = id, kyukoDate.lecture, kyukoDate.date, kyukoDate.createdAt)
  }

  def find(date: LocalDateTime): Seq[KyukoDate] = DB readOnly { implicit s =>
    sql"""select * from KyukoDays where date = ${date}""".map(*).list().apply()
  }

  def find(start: LocalDateTime, end: LocalDateTime) = DB readOnly { implicit s =>
    sql"""select * from KyukoDays where date between ${start} and ${end}""".map(*).list().apply()
  }

  def find(lecture: Lecture): Seq[KyukoDate] = DB readOnly { implicit s =>
    sql"""select * from KyukoDays where lecture_id = ${lecture.id}""".map(*).list().apply()
  }

  def find(teacher: Teacher): Seq[KyukoDate] = DB readOnly { implicit s =>
    sql"""select * from KyukoDays where teacher_id = ${teacher.id}""".map(*).list().apply()
  }

  def find(lecture: Lecture, date: LocalDateTime): Option[KyukoDate] = DB readOnly { implicit s =>
    sql"""select * from KyukoDays where lecture_id = ${lecture.id} and date = ${date}""".map(*).single().apply()
  }

  def fetch(school: School): Future[Seq[(Teacher, Lecture, KyukoDate)]] = {
    val browser = JsoupBrowser()
    // scalascraper は Shift_JISを扱えないためdispatchで取得する
    val f = Http(dispatch.url(school.url) OK dispatch.as.String.charset(Charset.forName("Shift_JIS"))).map {
      browser.parseString
    }.map(getKyukoPage(_, school))

    Await.ready(f, Duration.Inf)
  }

  def store(list: Seq[(Teacher, Lecture, KyukoDate)]): Seq[(Teacher, Lecture, KyukoDate)] = {
    list.map { case (teacher, lecture, kyukoDate) =>
      val l = LectureService.findByName(lecture.name).find(_.teacher.get.name == teacher.name).getOrElse(
        LectureService.create(lecture.name, teacher.name, lecture.category, lecture.period, lecture.remark, lecture.isGraduate, LocalDateTime.now)
      )
      val t = TeacherServiceImpl.findOrCreateByName(teacher.name)
      val k = find(l, kyukoDate.date).getOrElse(
        insert(KyukoDate(l, kyukoDate.date, kyukoDate.createdAt))
      )
      (t, l, k)
    }
  }

  def getKyukoPage(doc: Document, school: School): Seq[(Teacher, Lecture, KyukoDate)] = {
    val isNoSchedule = doc.body.text.contains("現在、休講の予定はありません。")
    val items: Option[List[Element]] = doc >?> elementList("tr")

    val now = LocalDateTime.now.toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss"))
    val updatedString = doc.body.children.find(_.text.startsWith("ページ更新日")).map(_.text)
      .getOrElse(s"ページ更新日：$now")

    if (!isNoSchedule && items.isDefined) {
      items.get.tail
        .map(_ >> elementList("td").map(_ >> text("td")))
        .map(convert(_, updatedString, school))
    } else List.empty[(Teacher, Lecture, KyukoDate)]
  }

  // Teacher, Lecture, KyukoDateのTupleを返す
  // 既に取得済かどうかのチェックは行わない
  // よって全て id が 0l
  // 永続化するかどうかは store メソッドにゆだねる
  def convert(values: Seq[String], updatedString: String, school: School): (Teacher, Lecture, KyukoDate) = {
    val name = values(3)
    val teacherName = values(4)
    val category = values(0)
    // 時限処理
    val periodRegex = """(\d)\D*""".r
    val period = periodRegex.findAllIn(values(2)).matchData.map(m => m.group(1).toInt).toList.head
    val remark = if (values(5).toCharArray.map(_.toInt).head == 0xA0) "" else values(5).trim // nbsp避け
    val isGraduate = school match {
      case UndergraduateSchool => false
      case GraduateSchool => true
    }
    // 日付処理
    val updated = LocalDateTime.parse(updatedString.split("：").last,
      DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss"))
    val regexp = """(\d+)月(\d+)日.+""".r
    val regexp(month, day) = values(1).trim
    // TODO: fetchしたタイミングでyearがずれる可能性あり
    val year = if (month.toInt >= updated.getMonthOfYear) {
      updated.getYear
    } else {
      updated.getYear + 1
    }
    val date = new LocalDateTime(year, month.toInt, day.toInt, 0, 0)

    val teacher = Teacher(teacherName, LocalDateTime.now)
    val lecture = Lecture(name, Some(teacher), category, period, remark, isGraduate, LocalDateTime.now)
    val kyukoDate = KyukoDate(lecture, date, LocalDateTime.now)

    (teacher, lecture, kyukoDate)
  }
}
