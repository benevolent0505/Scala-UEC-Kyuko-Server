package models

import org.joda.time.LocalDateTime
import org.scalatest.FlatSpec

/**
 * Created by benevolent0505 on 17/03/16.
 */
class TeacherTest extends FlatSpec {

  behavior of "Teacher"

  it should "have id, name, createdAt" in {
    val id = 1l
    val name = "Alice"
    val createdAt = LocalDateTime.now

    val teacher = Teacher(id = id, name = name, createdAt = createdAt)
    assert(teacher.id === id)
    assert(teacher.name === name)
    assert(teacher.createdAt === createdAt)
  }

  it should "create by name and createdAt" in {
    val name = "Alice"
    val createdAt = LocalDateTime.now

    val teacher = Teacher(name, createdAt)
    assert(teacher.id === 0l)
    assert(teacher.name === name)
    assert(teacher.createdAt === createdAt)
  }
}
