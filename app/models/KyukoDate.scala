package models

import org.joda.time.LocalDateTime

/**
 * Created by benevolent0505 on 17/03/15.
 */
case class KyukoDate (
  id: Long,
  lecture: Lecture,
  date: LocalDateTime,
  createdAt: LocalDateTime
)

object KyukoDate {
  def apply(
    lecture: Lecture,
    date: LocalDateTime,
    createdAt: LocalDateTime
  ): KyukoDate = new KyukoDate(0l, lecture, date, createdAt)
}
