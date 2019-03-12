package graphql.schema

import graphql.GraphQLTypes._
import com.google.inject.Inject
import graphql.GraphQLContext
import graphql.resolvers.JwtResolver
import sangria.schema.{Argument, Field, StringType}

/**
  * Defines GraphQL schema for operations with JWT tokens.
  */
class JwtSchema @Inject()(jwtResolver: JwtResolver) {

  /**
    * List of GraphQL queries defined for operations with JWT tokens.
    */
  val Mutations: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "refreshTokens",
      fieldType = TokensType,
      arguments = List(Argument("refreshToken", StringType)),
      resolve = sangriaContext =>
        jwtResolver.refreshTokens(sangriaContext.args.arg[String]("refreshToken"))
    )
  )
}