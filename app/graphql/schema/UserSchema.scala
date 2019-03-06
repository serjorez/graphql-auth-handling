package graphql.schema

import com.google.inject.Inject
import graphql.GraphQLContext
import graphql.resolvers.UserResolver
import models.jwt.Tokens
import models.User
import sangria.macros.derive.{ExcludeFields, ObjectTypeName, deriveObjectType}
import sangria.schema.{Argument, Field, ObjectType, OptionInputType, StringType}

class UserSchema @Inject()(userResolver: UserResolver) {

  /**
    * Sangria's representation of the User type.
    * It's necessary to convert User object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val UserType: ObjectType[GraphQLContext, User] =
    deriveObjectType[GraphQLContext, User](ObjectTypeName("User"), ExcludeFields("password"))

  /**
    * Sangria's representation of the Tokens type.
    * It's necessary to convert Tokens object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val TokensType: ObjectType[GraphQLContext, Tokens] =
    deriveObjectType[GraphQLContext, Tokens](ObjectTypeName("Tokens"))

  /**
    * List of GraphQL mutations defined for the User type.
    */
  val Mutations: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "register",
      fieldType = TokensType,
      arguments = List(
        Argument("login", StringType),
        Argument("password", StringType),
        Argument("role", OptionInputType(StringType))
      ),
      resolve = sangriaContext =>
        userResolver.register(
          sangriaContext.arg[String]("login"),
          sangriaContext.arg[String]("password"),
          sangriaContext.arg[Option[String]]("role"),
        )(sangriaContext.ctx)
    ),
    Field(
      name = "login",
      fieldType = TokensType,
      arguments = List(
        Argument("login", StringType),
        Argument("password", StringType)
      ),
      resolve = sangriaContext =>
        userResolver.login(
          sangriaContext.arg[String]("login"),
          sangriaContext.arg[String]("password")
        )(sangriaContext.ctx)
    )
  )
}