import javax.inject.{Inject, Singleton}

import actors.FetchPagesActor
import akka.actor.{ActorSystem, Props}
import com.google.inject.ImplementedBy
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import play.api.inject.ApplicationLifecycle

/**
 * Created by mikio on 2017/03/19.
 */

@ImplementedBy(classOf[CronJob])
trait Cron

@Singleton
class CronJob @Inject() (system: ActorSystem, lifeCycle: ApplicationLifecycle) extends Cron {

  val fetchPagesActor = system.actorOf(Props(classOf[FetchPagesActor]))

  QuartzSchedulerExtension(system).schedule("9o'clock", fetchPagesActor, "fetch")
}
