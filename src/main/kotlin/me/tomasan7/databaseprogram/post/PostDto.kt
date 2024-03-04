package me.tomasan7.databaseprogram.post

data class PostDto(
    val title: String,
    val content: String,
    val authorId: Int,
    val id: Int? = null
)
