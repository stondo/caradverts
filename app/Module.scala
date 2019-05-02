import com.google.inject.AbstractModule
import javax.inject.Singleton
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import v1.caradvert.{CarAdvertRepository, CarAdvertService}

/**
  * Sets up custom components for Play.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
  */
class Module(environment: Environment, configuration: Configuration) extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[CarAdvertRepository].to[CarAdvertService].in[Singleton]
  }
}
