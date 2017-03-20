package services

import models.{KyukoDate, Lecture, School, Teacher}
import org.joda.time.LocalDateTime
import repository.{MixInKyukoRepository, UsesKyukoRepository}

import scala.concurrent.Future

/**
 * Created by mikio on 2017/03/18.
 */
trait KyukoService extends UsesKyukoRepository {

  def fetch(school: School): Future[Seq[(Teacher, Lecture, KyukoDate)]] =
    kyukoRepository.fetch(school)

  def store(list: Seq[(Teacher, Lecture, KyukoDate)]): Seq[(Teacher, Lecture, KyukoDate)] =
    kyukoRepository.store(list)

  def findByLectureAndDate(lecture: Lecture, date: LocalDateTime): Option[KyukoDate] =
    kyukoRepository.find(lecture, date)
}

object KyukoService extends KyukoService with MixInKyukoRepository
