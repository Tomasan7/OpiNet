package me.tomasan7.databaseprogram.comment

data class CommentDto(
    val text: String,
    val authorId: Int,
    val postId: Int,
    val id: Int? = null
)
