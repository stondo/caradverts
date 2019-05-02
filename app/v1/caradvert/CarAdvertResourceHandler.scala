package v1.caradvert

import java.util.UUID

import javax.inject.{Inject, Provider}
import models.CarAdvert
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * Controls access to the backend data, returning [[CarAdvert]]
  */
class CarAdvertResourceHandler @Inject()(routerProvider: Provider[CarAdvertRouter], postRepository: CarAdvertRepository)(
    implicit ec: ExecutionContext) {

  def create(carAdvert: CarAdvert)(implicit mc: MarkerContext): Future[Option[UUID]] = {

    postRepository.create(carAdvert)
  }

//  def lookup(id: String)(implicit mc: MarkerContext): Future[Option[CarAdvert]] = {
//    val postFuture = postRepository.get(PostId(id))
//    postFuture.map { maybePostData =>
//      maybePostData.map { postData =>
//        createPostResource(postData)
//      }
//    }
//  }
//
//  def find(implicit mc: MarkerContext): Future[Iterable[CarAdvert]] = {
//    postRepository.list().map { postDataList =>
//      postDataList.map(postData => createPostResource(postData))
//    }
//  }

}
