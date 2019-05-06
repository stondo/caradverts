// TODO - not completed. I wanted to add generic point to handle failures and handle them properly. (es. log to db and always return a meaningful msg to the user)
//package utils
//
//import play.api.libs.json.Json
//import play.api.mvc.Results._
//
//import scala.concurrent.{ExecutionContext, Future}
//
//object FuturesErrorExtensionMethod {
//  implicit class ErrorMessageFutureDb[A](val future: Future[A]) extends AnyVal {
//    def internalServerError()(implicit ec: ExecutionContext): Future[Any] = future.recoverWith {
//
//      case e: Exception => Future.successful(InternalServerError(Json.toJson(Map("error" -> "Internal Server Error"))))
//
//    }
//  }
//}
