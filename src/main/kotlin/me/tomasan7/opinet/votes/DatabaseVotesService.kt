package me.tomasan7.opinet.votes

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseVotesService(
    private val database: Database
) : VotesService
{
    private suspend fun <T> dbQuery(statement: Transaction.() -> T) = withContext(Dispatchers.IO) {
        transaction(database, statement = statement)
    }

    suspend fun init()
    {
        dbQuery {
            SchemaUtils.create(VotesTable)
        }
    }

    private fun ResultRow.toVoteDto() = VoteDto(
        upDown = this[VotesTable.upDown],
        votedAt = Instant.fromEpochSeconds(this[VotesTable.votedAt].toLong()),
        userId = this[VotesTable.userId].value,
        postId = this[VotesTable.postId].value,
        id = this[VotesTable.id].value
    )

    override suspend fun createVote(voteDto: VoteDto): Int = dbQuery {
        VotesTable.insertAndGetId {
            it[upDown] = voteDto.upDown
            it[votedAt] = voteDto.votedAt.epochSeconds.toUInt()
            it[userId] = voteDto.userId
            it[postId] = voteDto.postId
        }.value
    }

    override suspend fun getVotesOnPost(postId: Int): ImmutableList<VoteDto> = dbQuery {
        VotesTable.selectAll()
            .where{ VotesTable.postId eq postId }
            .map { it.toVoteDto() }
            .toImmutableList()
    }

    override suspend fun getPostsOrderedByVotes(): ImmutableList<Int> = dbQuery {
        VotesTable.select(VotesTable.postId)
            .groupBy(VotesTable.postId)
            .orderBy(VotesTable.postId.count() to SortOrder.DESC)
            .map { it[VotesTable.postId].value }
            .toImmutableList()
    }

    override suspend fun removeVoteByUserOnPost(userId: Int, postId: Int): Boolean = dbQuery {
        VotesTable.deleteWhere { (VotesTable.userId eq userId) and (VotesTable.postId eq postId) } > 0
    }
}
