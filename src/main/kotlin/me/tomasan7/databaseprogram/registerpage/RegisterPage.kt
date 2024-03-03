package me.tomasan7.databaseprogram.registerpage

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
import me.tomasan7.databaseprogram.ui.AppThemePreviewer
import me.tomasan7.databaseprogram.ui.component.PasswordTextField
import me.tomasan7.databaseprogram.ui.component.VerticalSpacer

@Composable
fun RegisterPage(modifier: Modifier = Modifier)
{
    val viewModel = remember { RegisterPageViewModel() }
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Register",
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
        TextField(
            value = uiState.firstName,
            singleLine = true,
            onValueChange = { viewModel.setFirstName(it) },
            label = { Text("First name") }
        )
        TextField(
            value = uiState.lastName,
            singleLine = true,
            onValueChange = { viewModel.setLastName(it) },
            label = { Text("Last name") }
        )
        PasswordTextField(
            password = uiState.password,
            onPasswordChange = { viewModel.setPassword(it) },
            onChangeVisibilityClick = { viewModel.changePasswordVisibility() },
            passwordShown = uiState.passwordShown,
            label = { Text("Password") }
        )
        PasswordTextField(
            password = uiState.confirmingPassword,
            onPasswordChange = { viewModel.setConfirmingPassword(it) },
            onChangeVisibilityClick = { viewModel.changeConfirmingPasswordVisibility() },
            passwordShown = uiState.confirmingPasswordShown,
            label = { Text("Confirm password") }
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
