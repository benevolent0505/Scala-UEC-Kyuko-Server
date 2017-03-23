package controllers

import models._
import org.joda.time.LocalDateTime
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import services.KyukoService

/**
 * Created by mikio on 2017/03/22.
 */
class KyukoAPIController extends Controller {

  case class Event(id: Long, title: String, start: LocalDateTime, url: String)
  object Event {
    def apply(kyukoDate: KyukoDate): Event = {
      val id = kyukoDate.id
      val title = kyukoDate.lecture.name
      val start = kyukoDate.date
      val url = s"/lectures/${kyukoDate.lecture.id}"

      new Event(id, title, start, url)
    }
  }

  implicit val eventToJson = new Writes[Event] {
    def writes(event: Event) = Json.obj(
      "id" -> event.id,
      "title" -> event.title,
      "start" -> event.start.toLocalDate.toString,
      "url" -> event.url
    )
  }

  implicit val teacherToJson = new Writes[Teacher] {
    def writes(teacher: Teacher) = Json.obj(
      "id" -> teacher.id,
      "name" -> teacher.name
    )
  }

  implicit val lectureToJson = new Writes[Lecture] {
    def writes(lecture: Lecture) = Json.obj(
      "id" -> lecture.id,
      "name" -> lecture.name,
      "teacher" -> lecture.teacher.get,
      "category" -> lecture.category,
      "period" -> lecture.period,
      "isGraduate" -> lecture.isGraduate
    )
  }

  implicit val kyukoDateToJson = new Writes[KyukoDate] {
    def writes(kyukoDate: KyukoDate) = Json.obj(
      "id" -> kyukoDate.id,
      "lecture" -> kyukoDate.lecture,
      "date" -> kyukoDate.date.toString,
      "remark" -> kyukoDate.remark
    )
  }

  // カレンダー表示用のAPIエンドポイントなのでエラーはログに残したいが穏便に処理はしたい
  def getKyukoEvents(start: Option[String], end: Option[String]) = Action {
    def parseLocalDateTime(datetime: String): Either[IllegalArgumentException, LocalDateTime] = {
      try {
        Right(LocalDateTime.parse(datetime))
      } catch {
        case e: IllegalArgumentException => Left(e)
      }
    }

    val startTime = start.map(parseLocalDateTime(_) match {
      case Right(date) => date
      case Left(e) => Logger.error(e.getMessage); LocalDateTime.now
    }).getOrElse(LocalDateTime.now)
    val endTime = end.map(parseLocalDateTime(_) match {
      case Right(date) => date
      case Left(e) => Logger.error(e.getMessage); LocalDateTime.now
    }).getOrElse(LocalDateTime.now)

    val events = KyukoService.findByRange(startTime, endTime).map(Event.apply)

    Ok(Json.toJson(events))
  }

  def getCurrentKyuko(schoolStr: Option[String]) = Action.async {
    val school = schoolStr match {
      case Some("graduate") => GraduateSchool
      case _ => UndergraduateSchool
    }

    val f = KyukoService.fetch(school)

    f.map { list => Ok(Json.toJson(list.map(_._3))) }
  }
}
