package me.tomasan7.databaseprogram.comment

import me.tomasan7.databaseprogram.post.PostTable
import me.tomasan7.databaseprogram.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object CommentTable : IntIdTable("comment")
{
    val text = text("text")
    val uploadDate = date("upload_date")
    val authorId = reference("author_id", UserTable)
    val postId = reference("post_id", PostTable)
}
