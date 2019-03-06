package graphql

import models.{Post, User}
import models.jwt.Tokens
import sangria.macros.derive.{ExcludeFields, ObjectTypeName, deriveObjectType}
import sangria.schema.ObjectType

object GraphQLTypes {

  /**
    * Sangria's representation of the Post type.
    * It's necessary to convert Post object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val PostType: ObjectType[GraphQLContext, Post] = deriveObjectType[GraphQLContext, Post](ObjectTypeName("Post"))

  /**
    * Sangria's representation of the User type.
    * It's necessary to convert User object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val UserType: ObjectType[GraphQLContext, User] = deriveObjectType[GraphQLContext, User](ObjectTypeName("User"), ExcludeFields("password"))

  /**
    * Sangria's representation of the Tokens type.
    * It's necessary to convert Tokens object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val TokensType: ObjectType[GraphQLContext, Tokens] = deriveObjectType[GraphQLContext, Tokens](ObjectTypeName("Tokens"))
}