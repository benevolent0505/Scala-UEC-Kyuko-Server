package actors

import akka.actor.Actor
import models.UndergraduateSchool
import play.api.Logger
import services.KyukoService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by mikio on 2017/03/19.
 */
class FetchPagesActor extends Actor {

  def receive: Receive = {
    case msg: String =>
      Logger.info(msg)
      val f = KyukoService.fetch(UndergraduateSchool)
      f.onSuccess {
        case list => KyukoService.store(list)
      }
  }
}
