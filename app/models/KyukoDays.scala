package models

import org.joda.time.LocalDateTime

/**
 * Created by benevolent0505 on 17/03/15.
 */
case class KyukoDays(
  id: Long,
  lecture: Lecture,
  date: LocalDateTime,
  remark: String,
  createdAt: LocalDateTime
)

object KyukoDays {
  def apply(
    lecture: Lecture,
    date: LocalDateTime,
    remark: String,
    createdAt: LocalDateTime
  ): KyukoDays = new KyukoDays(0l, lecture, date, remark, createdAt)
}
