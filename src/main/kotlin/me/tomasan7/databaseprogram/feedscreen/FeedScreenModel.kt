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
            val posts = postService.getAllPostsOrderedByUploadDateDesc().map { postDto ->
                postDto.toPost(
                    authorGetter = { userId -> getUserDto(userId).toUser() },
                    commentCountGetter = { commentService.getNumberOfCommentsForPost(postDto.id!!).toInt() }
                )
            }.toImmutableList()
            changeUiState(posts = posts)
        }
    }

    fun editPost(post: Post)
    {
        uiState = uiState.copy(editPostEvent = post)
    }

    fun editPostEventConsumed()
    {
        uiState = uiState.copy(editPostEvent = null)
    }

    fun deletePost(post: Post)
    {
        screenModelScope.launch {
            val result = postService.deletePost(post.id)
            if (result)
                changeUiState(posts = (uiState.posts - post).toImmutableList())
        }
    }

    fun openComments(postId: Int)
    {
        screenModelScope.launch {
            val commentsForPost = commentService
                .getAllCommentsForPostOrderedByUploadDateDesc(postId)
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
            val newCommentId = commentService.createComment(commentDto)
            val newCommentDto = commentDto.copy(id = newCommentId).toComment { getUserDto(it).toUser() }
            val oldComments = uiState.commentsDialogState.comments
            val newComments = (oldComments + newCommentDto).toImmutableList()
            val oldPost = uiState.posts.find { it.id == postId }!!
            val newPost = oldPost.copy(
                commentCount = oldPost.commentCount + 1
            )
            val newPosts = (uiState.posts - oldPost) + newPost
            changeUiState(
                commentsDialogState = uiState.commentsDialogState.copy(comments = newComments),
                posts = newPosts.toImmutableList()
            )
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
