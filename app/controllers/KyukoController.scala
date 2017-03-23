package controllers

import org.joda.time.{IllegalFieldValueException, LocalDateTime}
import play.api.mvc.{Action, Controller}
import services.KyukoService

/**
 * Created by mikio on 2017/03/20.
 */
class KyukoController extends Controller {

  def index = Action {
    Ok(views.html.kyuko.index())
  }

  def show(year: Int, mon: Int, day: Int) = Action {
    val date = try {
      new Right(new LocalDateTime(year, mon, day, 0, 0))
    } catch {
      case e: IllegalFieldValueException => Left(e)
    }

    date match {
      case Right(date) => {
        val lectures = KyukoService.findByDate(date).map(_.lecture)
        Ok(views.html.kyuko.show(date, lectures))
      }
      case Left(e) => BadRequest(e.getMessage)
    }
  }
}
