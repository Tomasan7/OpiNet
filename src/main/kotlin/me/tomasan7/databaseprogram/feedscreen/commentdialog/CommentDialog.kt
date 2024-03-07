package me.tomasan7.databaseprogram.feedscreen.commentdialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.alexfacciorusso.previewer.PreviewTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tomasan7.databaseprogram.feedscreen.Comment
import me.tomasan7.databaseprogram.feedscreen.User
import me.tomasan7.databaseprogram.ui.AppThemePreviewer
import me.tomasan7.databaseprogram.ui.component.VerticalSpacer
import me.tomasan7.databaseprogram.ui.format
import java.time.format.DateTimeFormatter

@Composable
fun CommentsDialog(
    comments: ImmutableList<Comment>,
    onPostComment: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
)
{
    Dialog(onDismissRequest = onDismissRequest) {
        CommentDialogContent(
            comments = comments,
            onPostComment = onPostComment,
            onDismissRequest = onDismissRequest,
            modifier = modifier
        )
    }
}

@Composable
private fun CommentDialogContent(
    comments: ImmutableList<Comment>,
    onPostComment: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
)
{
    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close comments",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Comments",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (comments.isNotEmpty())
                comments.forEach { comment ->
                    Comment(
                        comment = comment,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            else
                Text(
                    text = "No comments yet",
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            VerticalSpacer(10.dp)
            CommentTextField(
                onSubmitComment = onPostComment
            )
        }
    }
}

@Composable
private fun Comment(
    comment: Comment,
    modifier: Modifier = Modifier
)
{
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${comment.author.firstName} ${comment.author.lastName}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = comment.uploadDate.format(dateFormat),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = comment.text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CommentTextField(
    onSubmitComment: (String) -> Unit
)
{
    var commentText by remember { mutableStateOf("") }

    val trailingIconButton: (@Composable () -> Unit)? = if (commentText.isNotBlank())
    {
        @Composable
        {
            IconButton({ onSubmitComment(commentText); commentText = "" }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    else
        null

    OutlinedTextField(
        value = commentText,
        shape = RoundedCornerShape(16.dp),
        onValueChange = { commentText = it },
        placeholder = { Text("Write a comment...") },
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = trailingIconButton,
        singleLine = false
    )
}

@Composable
@Preview
private fun CommentDialogPreview()
{
    AppThemePreviewer {
        val comment = Comment(
            text = "This is a comment",
            author = User(
                firstName = "John",
                lastName = "Doe",
                username = "john.doe",
                id = 0
            ),
            uploadDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            postId = 0, id = 0
        )
        val secondComment = comment.copy(
            text = "This is a second comment"
        )
        preview(previewTheme = PreviewTheme.Dark) {
            CommentDialogContent(
                comments = persistentListOf(comment, secondComment),
                onPostComment = {},
                onDismissRequest = {},
                modifier = Modifier.width(500.dp)
            )
        }
    }
}

private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
