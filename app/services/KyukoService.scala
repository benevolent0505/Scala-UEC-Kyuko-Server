package services

import models.{KyukoDays, Lecture, School, Teacher}
import org.joda.time.LocalDateTime
import repository.{MixInKyukoRepository, UsesKyukoRepository}

import scala.concurrent.Future

/**
 * Created by mikio on 2017/03/18.
 */
trait KyukoService extends UsesKyukoRepository {

  def fetch(school: School): Future[Seq[(Teacher, Lecture, KyukoDays)]] =
    kyukoRepository.fetch(school)

  def store(list: Seq[(Teacher, Lecture, KyukoDays)]): Seq[(Teacher, Lecture, KyukoDays)] =
    kyukoRepository.store(list)

  def findByDate(date: LocalDateTime): Seq[KyukoDays] =
    kyukoRepository.find(date)

  def findByRange(start: LocalDateTime, end: LocalDateTime): Seq[KyukoDays] =
    kyukoRepository.find(start, end)

  def findByLecture(lecture: Lecture): Seq[KyukoDays] =
    kyukoRepository.find(lecture)

  def findByLectureAndDate(lecture: Lecture, date: LocalDateTime): Option[KyukoDays] =
    kyukoRepository.find(lecture, date)
}

object KyukoService extends KyukoService with MixInKyukoRepository
