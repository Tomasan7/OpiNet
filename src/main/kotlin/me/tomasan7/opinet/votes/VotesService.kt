package me.tomasan7.opinet.votes

import kotlinx.collections.immutable.ImmutableList

interface VotesService
{
    suspend fun createVote(voteDto: VoteDto): Int
    suspend fun getVotesOnPost(postId: Int): ImmutableList<VoteDto>
    suspend fun getPostsOrderedByVotes(): ImmutableList<Int>
    suspend fun removeVoteByUserOnPost(userId: Int, postId: Int): Boolean
}
