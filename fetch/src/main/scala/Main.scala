import models.{GraduateSchool, UndergraduateSchool}
import play.api.Logger
import services.KyukoService

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by mikio on 2017/04/12.
 */
object Main {
  def main(args: Array[String]) = {
    Logger.info("start fetching page")

    val undergraduateFuture = KyukoService.fetch(UndergraduateSchool)
    val graduateFuture = KyukoService.fetch(GraduateSchool)

    undergraduateFuture.onSuccess {
      case list => {
        Logger.info("start storing undergraduate info")
        KyukoService.store(list)
      }
    }
    graduateFuture.onSuccess {
      case list => {
        Logger.info("start storing graduate info")
        KyukoService.store(list)
      }
    }
  }
}
