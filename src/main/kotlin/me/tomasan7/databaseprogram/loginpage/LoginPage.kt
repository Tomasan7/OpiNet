package me.tomasan7.databaseprogram.loginpage

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexfacciorusso.previewer.PreviewTheme
import me.tomasan7.databaseprogram.registerpage.RegisterPage
import me.tomasan7.databaseprogram.ui.AppThemePreviewer
import me.tomasan7.databaseprogram.ui.component.PasswordTextField
import me.tomasan7.databaseprogram.ui.component.VerticalSpacer

@Composable
fun LoginPage(modifier: Modifier = Modifier)
{
    val viewModel = remember { LoginPageViewModel() }
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        VerticalSpacer(16.dp)
        TextField(
            value = uiState.username,
            singleLine = true,
            onValueChange = { viewModel.setUsername(it) },
            label = { Text("Username") }
        )
        PasswordTextField(
            password = uiState.password,
            onPasswordChange = { viewModel.setPassword(it) },
            onChangeVisibilityClick = { viewModel.changePasswordVisibility() },
            passwordShown = uiState.passwordShown,
            label = { Text("Password") }
        )
        Button(onClick = { println("Registered") }) {
            Text("Register")
        }
    }
}

@Composable
@Preview
fun RegisterPagePreview()
{
    AppThemePreviewer {
        preview(previewTheme = PreviewTheme.Dark) {
            RegisterPage()
        }
    }
}
