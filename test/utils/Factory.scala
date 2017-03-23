package utils

import models.{Lecture, Teacher}
import org.joda.time.LocalDateTime

import scala.util.Random

/**
 * Created by mikio on 2017/03/23.
 */
object Factory {

  def createTeacher(
    id: Long = 0l,
    name: String = Random.nextInt().toString
  ): Teacher = {
    Teacher(id, name, LocalDateTime.now)
  }

  def createLecture(
    id: Long = 0l,
    name: String = Random.nextInt().toString,
    teacher: Option[Teacher] = Some(createTeacher()),
    category: String = Random.nextInt().toString,
    period: Int = Random.nextInt(9),
    isGraduate: Boolean = false): Lecture = {
    Lecture(id, name, teacher, category, period, isGraduate, LocalDateTime.now)
  }
}
