import java.nio.file.Paths

import models.UndergraduateSchool
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import repository.KyukoRepositoryImpl
import scalikejdbc.config.DBs
import scalikejdbc.{ConnectionPool, DB}

/**
 * Created by mikio on 2017/03/20.
 */
object InsertData extends App {
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost:5432/developmentdb", "nobody", "nobody")

  DBs.setup('development)

  val path = Paths.get("").toString
  val browser = new JsoupBrowser()

  val doc = browser.parseFile(path, "UTF-8")
  val list = KyukoRepositoryImpl.getKyukoPage(doc, UndergraduateSchool)

  val storedList = KyukoRepositoryImpl.store(list)
  storedList.foreach { case (teacher, lecture, kyukoday) =>
      println(teacher, lecture, kyukoday)
  }
}
