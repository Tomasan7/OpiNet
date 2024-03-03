package me.tomasan7.databaseprogram

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import me.tomasan7.databaseprogram.loginpage.LoginScreen
import me.tomasan7.databaseprogram.registerscreen.RegisterScreen
import me.tomasan7.databaseprogram.ui.theme.AppTheme

@Composable
@Preview
fun App()
{
    Navigator(LoginScreen) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CurrentScreen()
        }
    }
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
