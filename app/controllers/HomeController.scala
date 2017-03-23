package controllers

import javax.inject._

import org.joda.time.LocalDateTime
import play.api.mvc._
import services.KyukoService

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    val today = LocalDateTime.now
    val lectures = KyukoService.findByRange(today, today).map(_.lecture)

    Ok(views.html.index(lectures))
  }

  def about = Action {
    Ok(views.html.home.about())
  }

  def source = Action {
    Redirect("http://kyoumu.office.uec.ac.jp/kyuukou/")
  }
}
