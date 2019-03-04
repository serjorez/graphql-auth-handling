package services

import java.util.UUID

import graphql.Context
import play.api.mvc.Cookie

import scala.concurrent.Future

/**
  * Provides authorizing functions.
  */
class PostsAuthorizeServiceImpl extends PostsAuthorizeService {

  /** @inheritdoc*/
  override def withPostAuthorization[T](context: Context)(callback: String => Future[T]): Future[T] =
    context.requestCookies.get("my-id") match {
      case Some(id) => callback(id.value)
      case None =>
        val newId = UUID.randomUUID().toString
        context.newCookies += Cookie("my-id", newId)
        callback(newId)
    }
}