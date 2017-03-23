package services

import models.{Lecture, Teacher}
import org.joda.time.LocalDateTime
import repository.{MixInLectureRepository, UsesLectureRepository}

/**
 * Created by benevolent0505 on 17/03/15.
 */

trait LectureService extends UsesLectureRepository {

  def create(name: String, teacherName: String, category: String, period: Int, isGraduate: Boolean,
             createdAt: LocalDateTime = LocalDateTime.now): Lecture = {
    if (name.isEmpty) throw new Exception("name is empty")
    if (findByName(name).exists(_.teacher.get.name == teacherName)) throw new Exception(s"$name is already created")

    val teacher = TeacherServiceImpl.findOrCreateByName(teacherName)
    lectureRepository.insert(Lecture(name, Some(teacher), category, period, isGraduate, createdAt))
  }

  def findById(id: Long): Option[Lecture] =
    lectureRepository.find(id)

  def findByName(name: String): Seq[Lecture] =
    lectureRepository.find(name)

  def findByTeacher(teacher: Teacher): Seq[Lecture] =
    lectureRepository.find(teacher)

  def getByPage(page: Int = 1): Seq[Lecture] =
    lectureRepository.select((page - 1) * lectureRepository.defaultLimit)

  def count(): Long = lectureRepository.count()
}

object LectureService extends LectureService with MixInLectureRepository
