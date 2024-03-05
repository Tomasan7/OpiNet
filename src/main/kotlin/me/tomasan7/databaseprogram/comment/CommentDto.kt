package me.tomasan7.databaseprogram.comment

import kotlinx.datetime.LocalDate

data class CommentDto(
    val text: String,
    val uploadDate: LocalDate,
    val authorId: Int,
    val postId: Int,
    val id: Int? = null
)
