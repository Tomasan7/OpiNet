package me.tomasan7.databaseprogram.feedscreen.newpostscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tomasan7.databaseprogram.config.Config
import me.tomasan7.databaseprogram.feedscreen.Post
import me.tomasan7.databaseprogram.feedscreen.User
import me.tomasan7.databaseprogram.post.PostDto
import me.tomasan7.databaseprogram.post.PostService
import me.tomasan7.databaseprogram.user.UserService
import me.tomasan7.databaseprogram.util.now
import me.tomasan7.databaseprogram.util.parseLocalDate
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

class NewPostScreenModel(
    private val postService: PostService,
    private val userService: UserService,
    private val currentUser: User,
    private val editingPost: Post?,
    private val importConfig: Config.Import
) : ScreenModel
{
    var uiState by mutableStateOf(NewPostScreenState(
        isEditing = editingPost != null,
        title = editingPost?.title ?: "",
        content = editingPost?.content ?: ""
    ))
        private set

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
            authorId = currentUser.id
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

    fun onImportClick() = changeUiState(filePickerOpen = true)


    fun closeImportFilePicker() = changeUiState(filePickerOpen = false)

    fun onImportFileChosen(path: String)
    {
        val dateFormatter = try
        {
            DateTimeFormatter.ofPattern(importConfig.dateFormat)
        }
        catch (e: IllegalArgumentException)
        {
            logger.warn { "IMPORT: Configured date format is not valid (${importConfig.dateFormat}), aborting import..." }
            return
        }

        screenModelScope.launch {
            csvReader {
                delimiter = importConfig.csvDelimiter
            }.openAsync(path) {
                readAllAsSequence().forEach { fields ->
                    if (fields.size != 4)
                    {
                        logger.info { "IMPORT: Post was not imported, because it has ${fields.size} fields instead of 4" }
                        return@forEach
                    }

                    val (authorUsername, uploadDateStr, title, content) = fields

                    if (authorUsername.isBlank() || uploadDateStr.isBlank() || title.isBlank() || content.isBlank())
                    {
                        logger.info { "IMPORT: Post was not imported, because it has empty fields" }
                        return@forEach
                    }

                    val uploadDate = try
                    {
                        uploadDateStr.parseLocalDate(dateFormatter)
                    }
                    catch (e: Exception)
                    {
                        logger.info { "IMPORT: Post was not imported, because it has an invalid upload date format. dd.MM.yyyy is expected." }
                        println(e)
                        return@forEach
                    }

                    if (uploadDate > LocalDate.now())
                    {
                        logger.info { "IMPORT: Post was not imported, because it has an upload date in the future" }
                        return@forEach
                    }

                    val author = userService.getUserByUsername(authorUsername)

                    if (author == null)
                    {
                        logger.info { "IMPORT: Post was not imported, because user '$authorUsername' does not exist" }
                        return@forEach
                    }

                    val postDto = PostDto(
                        title = title,
                        content = content,
                        uploadDate = uploadDate,
                        authorId = author.id!!
                    )

                    try
                    {
                        postService.createPost(postDto)
                        logger.info { "IMPORT: Imported post titled '$title' by $authorUsername uploaded at $uploadDate" }
                    }
                    catch (e: Exception)
                    {
                        logger.error { "IMPORT: Post titled '$title' was not imported. (${e.message})" }
                    }
                }
            }
        }
    }

    private fun changeUiState(
        title: String? = null,
        content: String? = null,
        goBackToFeedEvent: Boolean? = null,
        filePickerOpen: Boolean? = null
    )
    {
        uiState = uiState.copy(
            title = title ?: uiState.title,
            content = content ?: uiState.content,
            goBackToFeedEvent = goBackToFeedEvent ?: uiState.goBackToFeedEvent,
            filePickerOpen = filePickerOpen ?: uiState.filePickerOpen
        )
    }
}
