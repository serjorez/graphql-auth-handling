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
      resolve =
        sangriaContext =>
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
        authorizeService.withAuthorization(sangriaContext.ctx)(
          userId =>
            postResolver.addPost(
              sangriaContext.args.arg[String]("title"),
              sangriaContext.args.arg[String]("content"),
              userId.toLong //TODO fix that
            )
        )
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
        adminAccessValidator.withAdminAccessValidation(sangriaContext.ctx)(
          postResolver.updatePost(
            sangriaContext.args.arg[Long]("id"),
            sangriaContext.args.arg[String]("title"),
            sangriaContext.args.arg[String]("content")
          )
        )
    ),
    Field(
      name = "deletePost",
      fieldType = BooleanType,
      arguments = List(
        Argument("id", LongType)
      ),
      resolve =
        sangriaContext =>
          adminAccessValidator.withAdminAccessValidation(sangriaContext.ctx)(
            postResolver.deletePost(sangriaContext.args.arg[Long]("id"))
          )
    )
  )
}