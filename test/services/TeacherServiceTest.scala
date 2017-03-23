package services

import models.Teacher
import org.joda.time.LocalDateTime
import org.scalatest.FlatSpec
import repository.TeacherRepository

/**
 * Created by benevolent0505 on 17/03/16.
 */
class TeacherServiceTest extends FlatSpec {

  behavior of "TeacherService"

  val id = 1l
  val name = "Alice"
  val createdAt = LocalDateTime.now
  val teacher = Teacher(id, name, createdAt)

  val service = new TeacherService with TeacherRepository {
    def insert(teacher: Teacher): Teacher = teacher

    def find(name: String): Option[Teacher] = {
      name match {
        case "Alice" => Some(teacher)
        case _ => None
      }
    }

    def find(id: Long): Option[Teacher] = {
      id match {
        case 1l => Some(teacher)
        case _ => None
      }
    }

    val defaultOffset: Int = 0
    val defaultLimit: Int = 20

    def select(offset: Int, limit: Int): Seq[Teacher] = ???

    def count(): Long = 1
  }

  it should "create a teacher by name and createdAt" in {
    val name = "Bob"
    val now = LocalDateTime.now

    val createdTeacher = service.create(name = name, createdAt = now)

    assert(createdTeacher.name === name)
    assert(createdTeacher.createdAt === now)
  }

  it should "throw an exception if a name is empty" in {
    val exception = intercept[Exception] {
      service.create("")
    }
    assert(exception.getMessage === "Teacher name is empty")
  }

  it should "throw an exception if a name already created" in {
    val exception = intercept[Exception] {
      service.create("Alice")
    }
    assert(exception.getMessage === "Alice is already registered")
  }

  it should "find a teacher by name" in {
    val foundTeacher = service.findByName(name)

    assert(foundTeacher.isDefined)
    assert(foundTeacher.get.id === id)
    assert(foundTeacher.get.name === name)
    assert(foundTeacher.get.createdAt === createdAt)
  }

  it should "find a teacher by id" in {
    val foundTeacher = service.findById(id)

    assert(foundTeacher.isDefined)
    assert(foundTeacher.get.id === id)
    assert(foundTeacher.get.name === name)
    assert(foundTeacher.get.createdAt === createdAt)
  }

  it should "find or create a teacher by name" in {
    val foundTeacher = service.findOrCreateByName(name)
    val createdTeacher = service.findOrCreateByName("Bob")

    assert(foundTeacher.id === id)
    assert(foundTeacher.name === name)
    assert(foundTeacher.createdAt === createdAt)

    assert(createdTeacher.name === "Bob")
  }
}
