package services

import graphql.GraphQLContext

import scala.concurrent.Future

/**
  * Determines authorizing functions.
  */
trait AuthorizationService {

  /**
    * Authorizing user using data from the context.
    *
    * @param context  a context, that GraphQL work with
    * @param callback a callback function that will be executed if the the user is authorized
    * @tparam T generic return type
    * @return result of the callback function
    */
  def withAuthorization[T](context: GraphQLContext)(callback: Long => Future[T]): Future[T]
}