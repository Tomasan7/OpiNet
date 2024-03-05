package me.tomasan7.databaseprogram.feedscreen

import kotlinx.datetime.LocalDate
import me.tomasan7.databaseprogram.post.PostDto

data class Post(
    val title: String,
    val content: String,
    val author: User,
    val uploadDate: LocalDate,
    val id: Int
)

inline fun PostDto.toPost(authorGetter: (Int) -> User) = Post(
    title = title,
    content = content,
    author = authorGetter(authorId),
    uploadDate = uploadDate,
    id = id!!
)
