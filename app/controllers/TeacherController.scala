package controllers

import play.api.mvc.{Action, Controller}
import services.{LectureService, TeacherServiceImpl}

/**
 * Created by mikio on 2017/03/21.
 */
class TeacherController extends Controller {

  def index(page: Int) = Action {
    val teachers = if (page > 0) {
      TeacherServiceImpl.getByPage(page)
    } else TeacherServiceImpl.getByPage()

    val maxPage = if (teachers.nonEmpty) {
      TeacherServiceImpl.getCount() / teachers.size + 1
    } else page

    Ok(views.html.teachers.index(teachers, page, maxPage.toInt))
  }

  def show(id: Long) = Action {
    TeacherServiceImpl.findById(id).map { teacher =>
      val lectures = LectureService.findByTeacher(teacher)
      Ok(views.html.teachers.show(teacher, lectures))
    }.getOrElse(NotFound("存在しないIDです"))
  }
}
