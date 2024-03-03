package me.tomasan7.databaseprogram

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.tomasan7.databaseprogram.registerpage.RegisterPage
import me.tomasan7.databaseprogram.ui.theme.AppTheme

@Composable
@Preview
fun App()
{
    RegisterPage(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    )
}

fun main() = application {
    AppTheme {
        Window(onCloseRequest = ::exitApplication) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                App()
            }
        }
    }
}
