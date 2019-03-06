package graphql.schema

import graphql.GraphQLTypes._
import com.google.inject.Inject
import graphql.GraphQLContext
import graphql.resolvers.JwtResolver
import sangria.schema.{Argument, Field, StringType}

class JwtSchema @Inject()(jwtResolver: JwtResolver) {

  val Mutations: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "refreshTokens",
      fieldType = TokensType,
      arguments = List(Argument("refreshToken", StringType)),
      resolve = sc => jwtResolver.refreshTokens(sc.args.arg[String]("refreshToken"))
    )
  )
}