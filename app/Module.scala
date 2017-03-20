import com.google.inject.AbstractModule

/**
 * Created by mikio on 2017/03/19.
 */
class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[Cron]).to(classOf[CronJob]).asEagerSingleton()
  }
}
