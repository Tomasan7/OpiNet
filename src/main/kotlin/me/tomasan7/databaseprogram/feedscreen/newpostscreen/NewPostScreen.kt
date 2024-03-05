package me.tomasan7.databaseprogram.feedscreen.newpostscreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.alexfacciorusso.previewer.PreviewTheme
import me.tomasan7.databaseprogram.getDatabaseProgram
import me.tomasan7.databaseprogram.ui.AppThemePreviewer

class NewPostScreen : Screen
{
    @Composable
    override fun Content()
    {
        val navigator = LocalNavigator.currentOrThrow
        val databaseProgram = navigator.getDatabaseProgram()
        val model = rememberScreenModel { NewPostScreenModel(databaseProgram.postService, databaseProgram) }
        val uiState = model.uiState

        if (uiState.goBackToFeedEvent)
        {
            model.goBackToFeedEventConsumed()
            navigator.pop()
        }

        Column(
            modifier = Modifier
                .width(250.dp)
        ) {
            IconButton({ navigator.pop() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Back",
                )
            }
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { model.setTitle(it) },
                singleLine = false,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.content,
                onValueChange = { model.setContent(it) },
                singleLine = false,
                label = { Text("Content") },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Button({ model.submit() }) {
                Text("Submit")
            }
        }
    }
}

@Preview
@Composable
fun NewPostScreenPreview()
{
    AppThemePreviewer {
        preview(previewTheme = PreviewTheme.Dark) {
            NewPostScreen().Content()
        }
    }
}
