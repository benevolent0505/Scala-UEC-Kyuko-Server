package models

import org.joda.time.LocalDateTime

/**
 * Created by benevolent0505 on 17/03/15.
 */
case class KyukoDate (
  id: Long,
  lecture: Lecture,
  date: LocalDateTime,
  remark: String,
  createdAt: LocalDateTime
)

object KyukoDate {
  def apply(
    lecture: Lecture,
    date: LocalDateTime,
    remark: String,
    createdAt: LocalDateTime
  ): KyukoDate = new KyukoDate(0l, lecture, date, remark, createdAt)
}
