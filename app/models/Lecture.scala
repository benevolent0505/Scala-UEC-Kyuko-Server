package models

import org.joda.time.LocalDateTime

/**
 * Created by benevolent0505 on 17/03/15.
 */
case class Lecture (
  id: Long,
  name: String,
  teacher: Option[Teacher],
  category: String,
  period: Int,
  isGraduate: Boolean,
  createdAt: LocalDateTime
)

object Lecture {
  def apply(
    name: String,
    teacher: Option[Teacher],
    category: String,
    period: Int,
    isGraduate: Boolean,
    createdAt: LocalDateTime
  ): Lecture = new Lecture(0l, name, teacher, category, period, isGraduate, createdAt)
}
