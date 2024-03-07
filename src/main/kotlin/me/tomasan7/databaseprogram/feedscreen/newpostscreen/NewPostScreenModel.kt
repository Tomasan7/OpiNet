package me.tomasan7.databaseprogram.feedscreen.newpostscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tomasan7.databaseprogram.DatabaseProgram
import me.tomasan7.databaseprogram.feedscreen.Post
import me.tomasan7.databaseprogram.post.PostDto
import me.tomasan7.databaseprogram.post.PostService

class NewPostScreenModel(
    private val postService: PostService,
    databaseProgram: DatabaseProgram,
    private val editingPost: Post?,
) : ScreenModel
{
    var uiState by mutableStateOf(NewPostScreenState(
        isEditing = editingPost != null,
        title = editingPost?.title ?: "",
        content = editingPost?.content ?: ""
    ))
        private set

    private val currentUser = databaseProgram.currentUser

    fun setTitle(title: String) = changeUiState(title = title)

    fun setContent(content: String) = changeUiState(content = content)

    fun goBackToFeedEventConsumed() = changeUiState(goBackToFeedEvent = false)

    fun submit()
    {
        if (editingPost != null)
            submitPostUpdate()
        else
            submitPost()
    }

    private fun submitPost()
    {
        val postDto = PostDto(
            title = uiState.title,
            content = uiState.content,
            uploadDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            authorId = currentUser.id!!
        )

        screenModelScope.launch {
            postService.createPost(postDto)
            changeUiState(goBackToFeedEvent = true)
        }
    }

    private fun submitPostUpdate()
    {
        val postDto = PostDto(
            id = editingPost!!.id,
            title = uiState.title,
            content = uiState.content,
            uploadDate = editingPost.uploadDate,
            authorId = editingPost.author.id
        )

        screenModelScope.launch {
            postService.updatePost(postDto)
            changeUiState(goBackToFeedEvent = true)
        }
    }

    private fun changeUiState(
        title: String? = null,
        content: String? = null,
        goBackToFeedEvent: Boolean? = null
    )
    {
        uiState = uiState.copy(
            title = title ?: uiState.title,
            content = content ?: uiState.content,
            goBackToFeedEvent = goBackToFeedEvent ?: uiState.goBackToFeedEvent
        )
    }
}
