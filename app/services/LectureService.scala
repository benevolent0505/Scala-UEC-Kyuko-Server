package services

import models.{Lecture, Teacher}
import scalikejdbc.{AutoSession, DBSession}

/**
 * Created by benevolent0505 on 17/03/15.
 */

trait LectureService {

  def create(lecture: Lecture)(implicit s: DBSession = AutoSession): Lecture

  def findByTeacher(teacher: Teacher)(implicit s: DBSession = AutoSession): Seq[Lecture]

  def findByName(name: String)(implicit s: DBSession = AutoSession): Seq[Lecture]
}

class LectureServiceImpl {

  def create(): Unit = {

  }
}
