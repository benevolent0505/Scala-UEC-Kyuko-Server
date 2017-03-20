package repository

import models.{Lecture, Teacher}
import scalikejdbc._
import services.TeacherServiceImpl

/**
 * Created by mikio on 2017/03/18.
 */

trait UsesLectureRepository {
  val lectureRepository: LectureRepository
}

trait LectureRepository {
  def insert (lecture: Lecture): Lecture

  def find(id: Long): Option[Lecture]

  def find(name: String): Seq[Lecture]

  def find(teacher: Teacher): Seq[Lecture]
}

trait MixInLectureRepository {
  val lectureRepository = LectureRepositoryImpl
}

object LectureRepositoryImpl extends LectureRepository {
  def * = (rs: WrappedResultSet) => {
    Lecture(
      rs.long("id"),
      rs.string("name"),
      TeacherServiceImpl.findById(rs.long("teacher_id")),
      rs.string("category"),
      rs.int("period"),
      rs.string("remark"),
      rs.boolean("graduate"),
      rs.jodaLocalDateTime("created_at")
    )
  }

  def insert(lecture: Lecture): Lecture = DB localTx { implicit s =>
    val id =
      sql"""
           insert into Lectures
             (name, teacher_id, category, period, remark, graduate, created_at)
           values
             (${lecture.name}, ${lecture.teacher.get.id}, ${lecture.category}, ${lecture.period}, ${lecture.remark}, ${lecture.isGraduate}, ${lecture.createdAt})
        """.updateAndReturnGeneratedKey.apply()

    Lecture(id = id, lecture.name, lecture.teacher, lecture.category, lecture.period, lecture.remark, lecture.isGraduate, lecture.createdAt)
  }

  def find(id: Long): Option[Lecture] = DB readOnly { implicit s =>
    sql"""select * from Lectures where id = ${id}""".map(*).single().apply()
  }

  def find(name: String): Seq[Lecture] = DB readOnly { implicit s =>
    sql"""select * from Lectures where name = ${name}""".map(*).list().apply()
  }

  def find(teacher: Teacher): Seq[Lecture] = DB readOnly { implicit s =>
    sql"""select * from Lectures where teacher_id = ${teacher.id}""".map(*).list().apply()
  }
}
