package me.tomasan7.databaseprogram.feedscreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.alexfacciorusso.previewer.PreviewTheme
import me.tomasan7.databaseprogram.feedscreen.commentdialog.CommentsDialog
import me.tomasan7.databaseprogram.feedscreen.newpostscreen.NewPostScreen
import me.tomasan7.databaseprogram.getDatabaseProgram
import me.tomasan7.databaseprogram.ui.AppThemePreviewer

object FeedScreen : Screen
{
    private fun readResolve(): Any = FeedScreen

    @Composable
    override fun Content()
    {
        val navigator = LocalNavigator.currentOrThrow
        val databaseProgram = navigator.getDatabaseProgram()
        val model = rememberScreenModel { FeedScreenModel(
            databaseProgram.userService,
            databaseProgram.postService,
            databaseProgram.commentService,
            databaseProgram
        ) }
        val uiState = model.uiState

        LaunchedEffect(Unit) {
            model.loadPosts()
        }

        if (uiState.commentsDialogState.isOpen
            && uiState.commentsDialogState.postId != null)
            CommentsDialog(
                comments = uiState.commentsDialogState.comments,
                onPostComment = { commentText ->
                    model.postComment(commentText, uiState.commentsDialogState.postId)
                },
                onDismissRequest = {
                    model.closeComments()
                }
            )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.posts.forEach { post ->
                    Post(
                        post = post,
                        onCommentClick = {
                            model.openComments(post.id)
                        }
                    )
                }
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { navigator push NewPostScreen() }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview()
{
    AppThemePreviewer {
        preview(previewTheme = PreviewTheme.Dark) {
            FeedScreen.Content()
        }
    }
}
