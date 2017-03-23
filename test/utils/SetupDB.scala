package utils

import org.scalatest.{BeforeAndAfterAll, fixture}
import scalikejdbc.config.DBsWithEnv

/**
 * Created by mikio on 2017/03/23.
 */
trait SetupDB extends BeforeAndAfterAll { self: fixture.FlatSpec =>

  override def beforeAll(): Unit = {
    DBsWithEnv("test").setupAll()
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    DBsWithEnv("test").closeAll()
    super.afterAll()
  }
}
