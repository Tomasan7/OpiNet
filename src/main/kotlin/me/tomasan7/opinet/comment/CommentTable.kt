package me.tomasan7.opinet.comment

import me.tomasan7.opinet.post.PostTable
import me.tomasan7.opinet.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object CommentTable : IntIdTable("comment")
{
    val text = text("text")
    val uploadDate = date("upload_date")
    val authorId = reference("author_id", UserTable)
    val postId = reference("post_id", PostTable)
}
