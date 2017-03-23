package models

import org.joda.time.LocalDateTime
import org.scalatest.FlatSpec
import utils.Factory

/**
 * Created by mikio on 2017/03/23.
 */
class LectureTest extends FlatSpec {

  behavior of "Lecture"

  it should "have id, name, opt teacher, category, period, isGraduate, createdAt" in {
    val id = 0l
    val name = "微分積分学"
    val teacher = Factory.createTeacher()
    val category = "1, 6ｸﾗ"
    val period = 2
    val isGraduate = false
    val now = LocalDateTime.now

    val lecture = Lecture(id, name, Some(teacher), category, period, isGraduate, now)

    assert(lecture.id === id)
    assert(lecture.name === name)
    assert(lecture.teacher === Some(teacher))
    assert(lecture.category === category)
    assert(lecture.period === period)
    assert(lecture.isGraduate === isGraduate)
    assert(lecture.createdAt === now)
  }
}
