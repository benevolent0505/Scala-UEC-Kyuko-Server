package controllers

import models.KyukoDate
import org.joda.time.LocalDateTime
import play.api.Logger
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
}
