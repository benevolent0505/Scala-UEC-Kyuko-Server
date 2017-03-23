package repository

import org.scalatest.fixture
import scalikejdbc.scalatest.AutoRollback
import utils.{Factory, SetupDB}

/**
 * Created by mikio on 2017/03/23.
 */
class LectureRepositoryTest extends fixture.FlatSpec with SetupDB with AutoRollback {

  behavior of "LectureRepository"

  val repository = LectureRepositoryImpl

  it should "insert a lecture" in { implicit session =>
    val lecture = Factory.createLecture()

    val created = repository.insert(lecture)
    assert(created.name === lecture.name)
    assert(created.teacher === lecture.teacher)
    assert(created.category === lecture.category)
  }

  it should "find a lecture by id" in { implicit session =>
    
  }

  it should "find lectures by name" in { implicit session =>

  }

  it should "find lectures by teacher" in { implicit session =>

  }

  it should "find lectures with offset and limit" in { implicit session =>

  }

  it should "count lectures" in { implicit session =>

  }
}
