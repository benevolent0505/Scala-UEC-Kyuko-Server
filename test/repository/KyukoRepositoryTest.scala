package repository

import models._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.joda.time.LocalDateTime
import org.scalatest.FlatSpec

/**
 * Created by mikio on 2017/03/21.
 */
class KyukoRepositoryTest extends FlatSpec {

  behavior of "KyukoRepository"

  it should "convert Tuple of teacher lecture and kyuko_day from string Seq" in {
    val (category, date, period, lectureName, teacherName, remark) =
      ("2年・昼", "10月6日(木)", "1", "中国語運用演習", "○千葉", "&nbsp;")
    val testSet = Seq(category, date, period, lectureName, teacherName, remark)
    val updatedString = "ページ更新日：2016/09/01 8:35:01"

    val (teacher, lecture, kyukoDay) = KyukoRepositoryImpl.convert(testSet, updatedString, UndergraduateSchool)
    assert(teacher.id === 0l)
    assert(teacher.name === teacherName)

    assert(lecture.id === 0l)
    assert(lecture.name === lectureName)
    assert(lecture.category === category)
    assert(lecture.isGraduate === false)
    assert(lecture.period === period.toInt)
    assert(lecture.remark === remark)

    assert(kyukoDay.date === new LocalDateTime(2016, 10, 6, 0, 0))
  }

  it should "convert list of teacher, lecture and kyuko_day from kyuko page" in {
    val browser = JsoupBrowser()
    val onDay = getClass.getResource("/kyuko_on_day.html").getPath
    val offDay = getClass.getResource("/kyuko_off_day.html").getPath

    val onDayDoc = browser.parseFile(onDay, "Shift_JIS")
    val offDayDoc = browser.parseFile(offDay, "Shift_JIS")

    val emptyList = KyukoRepositoryImpl.getKyukoPage(offDayDoc, UndergraduateSchool)
    assert(emptyList.isEmpty)

    val testSet = Seq(
      Map("category" -> "2年・昼", "kyuko_date" -> new LocalDateTime(2016, 10, 6, 0, 0),
        "period" -> 1, "lecture_name" -> "中国語運用演習", "teacher_name" -> "○千葉", "remark" -> ""),
      Map("category" -> "1年・昼", "kyuko_date" -> new LocalDateTime(2016, 10, 6, 0, 0),
        "period" -> 2, "lecture_name" -> "選択中国語第二", "teacher_name" -> "○千葉", "remark" -> ""),
      Map("category" -> "3M", "kyuko_date" -> new LocalDateTime(2016, 10, 27, 0, 0),
        "period" -> 3, "lecture_name" -> "知能機械工学基礎実験I(5. ミドルウェアを用いた移動ロボットの知覚制御のみ）", "teacher_name" -> "中村（友）", "remark" -> "「5. ミドルウェアを用いた移動ロボットの知覚制御」のみが休講となります"),
      Map("category" -> "人文社会", "kyuko_date" -> new LocalDateTime(2016, 10, 5, 0, 0),
        "period" -> 5, "lecture_name" -> "美術B", "teacher_name" -> "○久々湊", "remark" -> "")
    )
    val list = KyukoRepositoryImpl.getKyukoPage(onDayDoc, UndergraduateSchool).sortBy(_._2.period) // 授業時間順に
    for ((test, set) <- testSet.zip(list)) {
      val (teacher, lecture, kyukoDate) = set

      assert(teacher.name === test("teacher_name"))

      assert(lecture.name === test("lecture_name"))
      assert(lecture.isGraduate === false)
      assert(lecture.period === test("period"))
      assert(lecture.category === test("category"))
      assert(lecture.remark === test("remark"))

      assert(kyukoDate.date === test("kyuko_date"))
    }
  }
}
