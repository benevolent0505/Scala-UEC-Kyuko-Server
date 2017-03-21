package repository

import models.Teacher
import scalikejdbc._

/**
 * Created by benevolent0505 on 17/03/15.
 */
trait TeacherRepository {
  val defaultOffset: Int

  val defaultLimit: Int

  def insert(teacher: Teacher): Teacher

  def find(name: String): Option[Teacher]

  def find(id: Long): Option[Teacher]

  def select(offset: Int = defaultOffset, limit: Int = defaultLimit): Seq[Teacher]

  def count(): Long
}

trait TeacherRepositoryImpl extends TeacherRepository {

  val defaultOffset = 0

  val defaultLimit = 20

  def insert(teacher: Teacher): Teacher = DB localTx { implicit s =>
    val id: Long =
      sql"""
           insert into Teachers
             (name, created_at)
           values (${teacher.name}, ${teacher.createdAt})"""
        .updateAndReturnGeneratedKey.apply()
    teacher.copy(id = id)
  }

  def createTeacher(rs: WrappedResultSet): Teacher =
    Teacher(rs.long("id"), rs.string("name"), rs.jodaLocalDateTime("created_at"))

  def find(name: String): Option[Teacher] = DB readOnly { implicit s =>
    sql"""select * from Teachers where name = ${name}"""
      .map(createTeacher).single().apply()
  }

  def find(id: Long): Option[Teacher] = DB readOnly { implicit s =>
    sql"""select * from Teachers where id = ${id}"""
      .map(createTeacher).single().apply()
  }

  def select(offset: Int = defaultOffset, limit: Int = defaultLimit): Seq[Teacher] = DB readOnly { implicit s =>
    sql"""select * from Teachers order by id offset ${offset} limit ${limit}""".map(createTeacher).list().apply()
  }

  def count(): Long = DB readOnly { implicit s =>
    sql"""select count(1) from Teachers""".map(_.long(1)).single().apply().get
  }
}
