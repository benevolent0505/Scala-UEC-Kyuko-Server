package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def about = Action {
    Ok(views.html.home.about())
  }

  def source = Action {
    Redirect("http://kyoumu.office.uec.ac.jp/kyuukou/")
  }
}
