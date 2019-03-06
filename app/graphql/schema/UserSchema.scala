package graphql.schema

import graphql.GraphQLTypes._
import com.google.inject.Inject
import graphql.GraphQLContext
import graphql.resolvers.UserResolver
import sangria.schema.{Argument, Field, ListType, OptionType, StringType}

class UserSchema @Inject()(userResolver: UserResolver) {

  /**
    * List of GraphQL queries defined for the User type.
    */
  val Queries: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "users",
      fieldType = ListType(UserType),
      resolve = _ => userResolver.users()
    ),
    Field(
      name = "findUser",
      fieldType = OptionType(UserType),
      arguments = List(Argument("login", StringType)),
      resolve = sangriaContext => userResolver.findUser(sangriaContext.arg[String]("login"))
    ),
    Field(
      name = "currentUser",
      fieldType = UserType,
      resolve = sangriaContext => userResolver.currentUser(sangriaContext.ctx)
    )
  )

  /**
    * List of GraphQL mutations defined for the User type.
    */
  val Mutations: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "register",
      fieldType = TokensType,
      arguments = List(
        Argument("login", StringType),
        Argument("password", StringType)
      ),
      resolve = sangriaContext =>
        userResolver.register(
          sangriaContext.arg[String]("login"),
          sangriaContext.arg[String]("password"),
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