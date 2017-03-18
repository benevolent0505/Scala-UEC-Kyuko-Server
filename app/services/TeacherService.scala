package services

import models.Teacher
import org.joda.time.LocalDateTime
import repository.{TeacherRepository, TeacherRepositoryImpl}

/**
 * Created by benevolent0505 on 17/03/15.
 */

trait TeacherService {
  self: TeacherRepository =>

  def create(name: String, createdAt: LocalDateTime = LocalDateTime.now): Teacher = {
    if (name.isEmpty) throw new Exception("Teacher name is empty")
    if (findByName(name).isDefined) throw new Exception(s"$name is already registered")

    insert(Teacher(name, createdAt))
  }

  def findOrCreateByName(name: String): Teacher = {
    findByName(name)
      .getOrElse(create(name))
  }

  def findById(id: Long): Option[Teacher] = {
    find(id)
  }

  def findByName(name: String): Option[Teacher] = {
    find(name)
  }
}

object TeacherServiceImpl extends TeacherService with TeacherRepositoryImpl
