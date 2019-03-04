package validators

import graphql.Context

import scala.concurrent.Future

/**
  * Determines functions to validate that user has administrator rights.
  */
trait AdminAccessValidator {

  /**
    * Validate that user has administrator rights.
    *
    * @param context  a context, that GraphQL work with
    * @param callback a callback function that will be executed if the user has administrator rights
    * @tparam T generic return type
    * @return result of the callback function
    */
  def withAdminAccessValidation[T](context: Context)(callback: => Future[T]): Future[T]
}