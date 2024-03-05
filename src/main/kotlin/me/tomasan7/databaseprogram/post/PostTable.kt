package me.tomasan7.databaseprogram.post

import me.tomasan7.databaseprogram.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object PostTable : IntIdTable()
{
    val title = varchar("title", 100)
    val content = text("content")
    val uploadDate = date("upload_date")
    val authorId = reference("author_id", UserTable)
}
