package controllers

import org.joda.time.LocalDateTime
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
    val date = new LocalDateTime(year, mon, day, 0, 0)
    val lectures = KyukoService.findByDate(date).map(_.lecture)

    Ok(views.html.kyuko.show(date, lectures))
  }
}
