package me.tomasan7.databaseprogram.comment

import kotlinx.collections.immutable.ImmutableList

interface CommentService
{
    suspend fun createComment(comment: CommentDto): Int
    suspend fun getCommentById(id: Int): CommentDto?
    suspend fun getAllCommentsForPostOrderedByUploadDateDesc(postId: Int): ImmutableList<CommentDto>
    suspend fun getAllCommentsForUser(authorId: Int): ImmutableList<CommentDto>
    suspend fun getNumberOfCommentsForPost(postId: Int): Long
}
