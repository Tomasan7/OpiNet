package me.tomasan7.opinet.comment

import kotlinx.collections.immutable.ImmutableList

interface CommentService
{
    /**
     * Creates a comment for a post.
     *
     * @return The id of the new comment.
     */
    suspend fun createComment(comment: CommentDto): Int

    /** Returns a comment by its [id] or `null` if this id doesn't exist. */
    suspend fun getCommentById(id: Int): CommentDto?

    /** Returns all comments on a [post][postId]. May be empty. */
    suspend fun getAllCommentsForPostOrderedByUploadDateDesc(postId: Int): ImmutableList<CommentDto>

    /** Returns all comments by a [user][authorId]. May be empty. */
    suspend fun getAllCommentsForUser(authorId: Int): ImmutableList<CommentDto>

    /** Returns the number of comments on a [post][postId]. */
    suspend fun getNumberOfCommentsForPost(postId: Int): Long

    /**
     * Deletes a comment by its [id][postId].
     *
     * @return The number of comments deleted.
     */
    suspend fun deleteCommentsForPost(postId: Int): Int
}
