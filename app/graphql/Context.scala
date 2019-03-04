package graphql

import play.api.mvc.{Cookie, Cookies, Headers}

import scala.collection.mutable.ListBuffer

/**
  * Defines some contextual object that flows across the whole execution.
  * It can be provided to execution by the user in order to help fulfill the GraphQL query.
  *
  * @param requestHeaders contains headers that came from the HTTP request
  * @param requestCookies contains cookies that came from the HTTP request
  * @param newHeaders     stores new headers, which should later be attached to the HTTP response
  * @param newCookies     stores new cookies, which should later be attached to the HTTP response
  */
case class Context(requestHeaders: Headers,
                   requestCookies: Cookies,
                   newHeaders: ListBuffer[(String, String)] = ListBuffer.empty,
                   newCookies: ListBuffer[Cookie] = ListBuffer.empty)