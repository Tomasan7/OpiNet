package me.tomasan7.opinet.votes

import me.tomasan7.opinet.post.PostTable
import me.tomasan7.opinet.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

object VotesTable : IntIdTable("votes")
{
    val upDown = bool("up_down")
    val votedAt = uinteger("voted_at") /* Unix epoch */
    val userId = reference("user_id", UserTable)
    val postId = reference("post_id", PostTable)
}
