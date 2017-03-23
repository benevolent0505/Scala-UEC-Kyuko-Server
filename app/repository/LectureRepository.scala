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

  val defaultOffset: Int

  val defaultLimit: Int

  def insert (lecture: Lecture): Lecture

  def find(id: Long): Option[Lecture]

  def find(name: String): Seq[Lecture]

  def find(teacher: Teacher): Seq[Lecture]

  def select(offset: Int = defaultOffset, limit: Int = defaultLimit): Seq[Lecture]

  def count(): Long
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
      rs.boolean("graduate"),
      rs.jodaLocalDateTime("created_at")
    )
  }

  val defaultOffset = 0

  val defaultLimit = 20

  def insert(lecture: Lecture): Lecture = DB localTx { implicit s =>
    val id =
      sql"""
           insert into Lectures
             (name, teacher_id, category, period, graduate, created_at)
           values
             (${lecture.name}, ${lecture.teacher.get.id}, ${lecture.category}, ${lecture.period}, ${lecture.isGraduate}, ${lecture.createdAt})
        """.updateAndReturnGeneratedKey.apply()

    Lecture(id = id, lecture.name, lecture.teacher, lecture.category, lecture.period, lecture.isGraduate, lecture.createdAt)
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

  def select(offset: Int = defaultOffset, limit: Int = defaultLimit): Seq[Lecture] = DB readOnly { implicit s =>
    sql"""select * from Lectures order by id  offset ${offset} limit ${limit}""".map(*).list().apply()
  }

  def count(): Long = DB readOnly { implicit s =>
    sql"""select count(1) from Lectures""".map(_.long(1)).single().apply().get
  }
}
