package graphql.schema

import com.google.inject.Inject
import graphql.GraphQLContext
import graphql.resolvers.PostResolver
import models.Post
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema._
import services.AuthorizeService
import validators.AdminAccessValidator

/**
  * Defines GraphQL schema for the Post entity.
  */
class PostSchema @Inject()(postResolver: PostResolver,
                           authorizeService: AuthorizeService,
                           adminAccessValidator: AdminAccessValidator) {

  /**
    * Sangria's representation of the Post type.
    * It's necessary to convert Post object into Sangria's GraphQL object to represent it in the GraphQL format.
    */
  implicit val PostType: ObjectType[GraphQLContext, Post] = deriveObjectType[GraphQLContext, Post](ObjectTypeName("Post"))

  /**
    * List of GraphQL queries defined for the Post type.
    */
  val Queries: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "posts",
      fieldType = ListType(PostType),
      resolve = _ => postResolver.posts
    ),
    Field(
      name = "findPost",
      fieldType = OptionType(PostType),
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = sangriaContext =>
        postResolver.findPost(sangriaContext.args.arg[Long]("id"))
    )
  )

  /**
    * List of GraphQL mutations defined for the Post type.
    */
  val Mutations: List[Field[GraphQLContext, Unit]] = List(
    Field(
      name = "addPost",
      fieldType = PostType,
      arguments = List(
        Argument("title", StringType),
        Argument("content", StringType)
      ),
      resolve = sangriaContext =>
        postResolver.addPost(
          sangriaContext.arg[String]("title"),
          sangriaContext.arg[String]("content"),
        )(sangriaContext.ctx)
    ),
    Field(
      name = "updatePost",
      fieldType = PostType,
      arguments = List(
        Argument("id", LongType),
        Argument("title", StringType),
        Argument("content", StringType)
      ),
      resolve = sangriaContext =>
        postResolver.updatePost(
          sangriaContext.arg[Long]("id"),
          sangriaContext.arg[String]("title"),
          sangriaContext.arg[String]("content")
        )(sangriaContext.ctx)
    ),
    Field(
      name = "deletePost",
      fieldType = BooleanType,
      arguments = List(
        Argument("id", LongType)
      ),
      resolve = sangriaContext =>
        postResolver.deletePost(sangriaContext.arg[Long]("id"))(sangriaContext.ctx)
    )
  )
}