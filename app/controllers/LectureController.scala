package controllers

import play.api.mvc.{Action, Controller}
import services.{KyukoService, LectureService}

/**
 * Created by mikio on 2017/03/21.
 */
class LectureController extends Controller {

  def index(page: Int) = Action {
    val lectures = if (page > 0) {
      LectureService.getByPage(page)
    } else LectureService.getByPage()
    val maxPage = if (lectures.nonEmpty) {
      LectureService.count().toInt / lectures.size
    } else page

    Ok(views.html.lectures.index(lectures, page, maxPage))
  }

  def show(id: Long) = Action {
    LectureService.findById(id).map { lecture =>
      val kyukoDays = KyukoService.findByLecture(lecture)
      Ok(views.html.lectures.show(lecture, kyukoDays))
    }.getOrElse(NotFound("存在しないIDです"))
  }
}
