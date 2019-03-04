package services

import graphql.Context

import scala.concurrent.Future

/**
  * Determines authorizing functions.
  */
trait PostsAuthorizeService {

  /**
    * Authorizing user using data from the context.
    *
    * @param context  a context, that GraphQL work with
    * @param callback a callback function that will be executed if the the user is authorized
    * @tparam T generic return type
    * @return result of the callback function
    */
  def withPostAuthorization[T](context: Context)(callback: String => Future[T]): Future[T]
}