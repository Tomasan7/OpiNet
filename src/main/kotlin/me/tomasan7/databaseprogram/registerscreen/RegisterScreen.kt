package me.tomasan7.databaseprogram.registerscreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.alexfacciorusso.previewer.PreviewTheme
import me.tomasan7.databaseprogram.loginpage.LoginScreen
import me.tomasan7.databaseprogram.ui.AppThemePreviewer
import me.tomasan7.databaseprogram.ui.component.PasswordTextField
import me.tomasan7.databaseprogram.ui.component.VerticalSpacer


data class RegisterScreen(
    private val username: String = "",
    private val password: String = "",
) : Screen
{
    @Composable
    override fun Content()
    {
        val model = rememberScreenModel { RegisterScreenModel(username, password) }
        val uiState = model.uiState
        val navigator = LocalNavigator.currentOrThrow

        Column(
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            VerticalSpacer(16.dp)
            TextField(value = uiState.username,
                singleLine = true,
                onValueChange = { model.setUsername(it) },
                label = { Text("Username") })
            TextField(value = uiState.firstName,
                singleLine = true,
                onValueChange = { model.setFirstName(it) },
                label = { Text("First name") })
            TextField(value = uiState.lastName,
                singleLine = true,
                onValueChange = { model.setLastName(it) },
                label = { Text("Last name") })
            PasswordTextField(password = uiState.password,
                onPasswordChange = { model.setPassword(it) },
                onChangeVisibilityClick = { model.changePasswordVisibility() },
                passwordShown = uiState.passwordShown,
                label = { Text("Password") })
            PasswordTextField(password = uiState.confirmingPassword,
                onPasswordChange = { model.setConfirmingPassword(it) },
                onChangeVisibilityClick = { model.changeConfirmingPasswordVisibility() },
                passwordShown = uiState.confirmingPasswordShown,
                label = { Text("Confirm password") })
            Button({ model.register() }) {
                Text("Register")
            }
            TextButton({ navigator push LoginScreen }) {
                Text(
                    text = "Go back to login",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
@Preview
fun RegisterScreenPreview()
{
    AppThemePreviewer {
        preview(previewTheme = PreviewTheme.Dark) {
            RegisterScreen().Content()
        }
    }
}
