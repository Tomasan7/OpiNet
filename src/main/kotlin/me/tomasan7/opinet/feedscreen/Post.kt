package me.tomasan7.opinet.feedscreen

import kotlinx.datetime.LocalDate
import me.tomasan7.opinet.post.PostDto

data class Post(
    val title: String,
    val content: String,
    val author: User,
    val uploadDate: LocalDate,
    val commentCount: Int,
    val id: Int
)

inline fun PostDto.toPost(
    authorGetter: (Int) -> User,
    commentCountGetter: (Int) -> Int
) = Post(
    title = title,
    content = content,
    author = authorGetter(authorId),
    commentCount = commentCountGetter(id!!),
    uploadDate = uploadDate,
    id = id
)
