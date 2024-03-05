package me.tomasan7.databaseprogram.feedscreen

import kotlinx.datetime.LocalDate
import me.tomasan7.databaseprogram.comment.CommentDto

data class Comment(
    val text: String,
    val author: User,
    val uploadDate: LocalDate,
    val postId: Int,
    val id: Int,
)

inline fun CommentDto.toComment(
    authorGetter: (Int) -> User
) = Comment(
    text = text,
    author = authorGetter(authorId),
    uploadDate = uploadDate,
    postId = postId,
    id = id!!
)
