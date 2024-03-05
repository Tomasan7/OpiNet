package me.tomasan7.databaseprogram.comment

import me.tomasan7.databaseprogram.post.PostTable
import me.tomasan7.databaseprogram.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

object CommentTable : IntIdTable()
{
    val text = text("text")
    val authorId = reference("author_id", UserTable)
    val postId = reference("post_id", PostTable)
}
