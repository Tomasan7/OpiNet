package me.tomasan7.databaseprogram.post

import kotlinx.datetime.LocalDate

data class PostDto(
    val title: String,
    val content: String,
    val uploadDate: LocalDate,
    val authorId: Int,
    val id: Int? = null
)
