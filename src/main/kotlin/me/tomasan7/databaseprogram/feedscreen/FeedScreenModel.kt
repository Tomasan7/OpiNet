package me.tomasan7.databaseprogram.feedscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tomasan7.databaseprogram.DatabaseProgram
import me.tomasan7.databaseprogram.comment.CommentDto
import me.tomasan7.databaseprogram.comment.CommentService
import me.tomasan7.databaseprogram.post.PostService
import me.tomasan7.databaseprogram.user.UserDto
import me.tomasan7.databaseprogram.user.UserService
import me.tomasan7.databaseprogram.user.UserTable.firstName
import me.tomasan7.databaseprogram.user.UserTable.lastName
import me.tomasan7.databaseprogram.user.UserTable.password
import me.tomasan7.databaseprogram.user.UserTable.username

class FeedScreenModel(
    private val userService: UserService,
    private val postService: PostService,
    private val commentService: CommentService,
    private val databaseProgram: DatabaseProgram
) : ScreenModel
{
    var uiState by mutableStateOf(FeedScreenState())
        private set

    private val cachedUsers = mutableMapOf<Int, UserDto>(
        databaseProgram.currentUser.id!! to databaseProgram.currentUser
    )

    fun loadPosts()
    {
        screenModelScope.launch {
            val posts = postService.getAllPosts().map { postDto ->
                postDto.toPost(
                    authorGetter = { userId -> getUserDto(userId).toUser() },
                    commentCountGetter = { /* TODO: CHANGE */ 12 }
                )
            }.toImmutableList()
            changeUiState(posts = posts)
        }
    }

    fun openComments(postId: Int)
    {
        screenModelScope.launch {
            val commentsForPost = commentService
                .getAllCommentsForPost(postId)
                .map { it.toComment { getUserDto(it).toUser() } }
                .toImmutableList()
            val newCommentsDialogState = FeedScreenState.CommentsDialogState(
                postId = postId,
                comments = commentsForPost,
                isOpen = true
            )
            changeUiState(commentsDialogState = newCommentsDialogState)
        }
    }

    fun closeComments()
    {
        changeUiState(commentsDialogState = uiState.commentsDialogState.copy(isOpen = false))
    }

    fun postComment(commentText: String, postId: Int)
    {
        screenModelScope.launch {
            val commentDto = CommentDto(
                text = commentText,
                authorId = databaseProgram.currentUser.id!!,
                uploadDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                postId = postId
            )

            commentService.createComment(commentDto)
            changeUiState(commentsDialogState = uiState.commentsDialogState.copy(isOpen = false))
        }
    }

    private suspend fun getUserDto(id: Int): UserDto
    {
        return cachedUsers.getOrPut(id) {
            userService.getUserById(id)!!
        }
    }

    private fun changeUiState(
        posts: ImmutableList<Post>? = null,
        commentsDialogState: FeedScreenState.CommentsDialogState? = null
    )
    {
        uiState = uiState.copy(
            posts = posts ?: uiState.posts,
            commentsDialogState = commentsDialogState ?: uiState.commentsDialogState
        )
    }
}
