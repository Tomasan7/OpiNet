package me.tomasan7.databaseprogram.post

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabasePostService(
    private val database: Database
) : PostService
{
    private suspend fun <T> dbQuery(statement: Transaction.() -> T) = withContext(Dispatchers.IO) {
        transaction(database, statement = statement)
    }

    suspend fun init()
    {
        dbQuery {
            SchemaUtils.create(PostTable)
        }
    }

    private fun ResultRow.toPostDto() = PostDto(
        title = this[PostTable.title],
        content = this[PostTable.content],
        authorId = this[PostTable.authorId].value,
        uploadDate = this[PostTable.uploadDate],
        id = this[PostTable.id].value
    )

    override suspend fun createPost(postDto: PostDto)
    {
        dbQuery {
            PostTable.insert {
                it[title] = postDto.title
                it[content] = postDto.content
                it[uploadDate] = postDto.uploadDate
                it[authorId] = postDto.authorId
            }
        }
    }

    override suspend fun getPostById(id: Int): PostDto? = dbQuery {
        PostTable.selectAll()
            .where { PostTable.id eq id }
            .singleOrNull()
            ?.toPostDto()
    }

    override suspend fun getAllPosts() = dbQuery {
        PostTable.selectAll()
            .map { it.toPostDto() }
            .toImmutableList()
    }

    override suspend fun getPostsByAuthorId(authorId: Int) = dbQuery {
        PostTable.selectAll()
            .where { PostTable.authorId eq authorId }
            .map { it.toPostDto() }
            .toImmutableList()
    }
}
