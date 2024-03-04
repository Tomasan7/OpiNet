package me.tomasan7.databaseprogram.post

import me.tomasan7.databaseprogram.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

object PostTable : IntIdTable()
{
    val title = varchar("title", 100)
    val content = text("content")
    val authorId = reference("author_id", UserTable)
}
