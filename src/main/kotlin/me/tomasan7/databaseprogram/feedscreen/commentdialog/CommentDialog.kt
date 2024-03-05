package me.tomasan7.databaseprogram.feedscreen.commentdialog

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.ImmutableList
import me.tomasan7.databaseprogram.feedscreen.Comment
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
        Surface(
            modifier = modifier,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp
        )
        {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
            ) {
                comments.forEach { comment ->
                    Comment(
                        comment = comment,
                        modifier = Modifier
                    )
                }
                CommentTextField(
                    onSubmitComment = onPostComment
                )
            }
        }
    }
}

@Composable
fun CommentTextField(
    onSubmitComment: (String) -> Unit
)
{
    var commentText by remember { mutableStateOf("") }

    val trailingIconButton = if (commentText.isNotBlank())
    {
        @Composable
        {
            IconButton({ onSubmitComment(commentText) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
    else
        null

    OutlinedTextField(
        value = commentText,
        onValueChange = { commentText = it },
        placeholder = { Text("Write a comment...") },
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = trailingIconButton,
        singleLine = false
    )
}

@Composable
private fun Comment(
    comment: Comment,
    modifier: Modifier
)
{
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
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

private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
