package me.tomasan7.opinet.registerscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import me.tomasan7.opinet.OpiNet
import me.tomasan7.opinet.config.Config
import me.tomasan7.opinet.user.UserDto
import me.tomasan7.opinet.user.UserService
import me.tomasan7.opinet.user.UsernameAlreadyExistsException
import java.nio.channels.UnresolvedAddressException

private val logger = KotlinLogging.logger {}

class RegisterScreenModel(
    username: String = "",
    password: String = "",
    private val userService: UserService,
    private val opiNet: OpiNet,
    private val importConfig: Config.Import
) : ScreenModel
{
    var uiState by mutableStateOf(RegisterScreenState(username = username, password = password))
        private set

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace(), errorText = "")

    fun setFirstName(firstName: String) = changeUiState(firstName = firstName.removeWhitespace(), errorText = "")

    fun setLastName(lastName: String) = changeUiState(lastName = lastName.removeWhitespace(), errorText = "")

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace(), errorText = "")

    fun setConfirmingPassword(confirmingPassword: String) = changeUiState(confirmingPassword = confirmingPassword.removeWhitespace(), errorText = "")

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun changeConfirmingPasswordVisibility() = changeUiState(confirmingPasswordShown = !uiState.confirmingPasswordShown)

    fun registrationSuccessEventConsumed() = changeUiState(registrationSuccessEvent = false, errorText = "")

    fun onImportClick()
    {
        changeUiState(filePickerOpen = true)
    }

    fun onImportFileChosen(path: String)
    {
        screenModelScope.launch {
            csvReader {
                delimiter = importConfig.csvDelimiter
            }.openAsync(path) {
                readAllAsSequence().forEach { fields ->
                    if (fields.size != 4)
                    {
                        logger.warn { "IMPORT: Skipped line because it had ${fields.size} fields instead of 3" }
                        return@forEach
                    }

                    val (username, firstName, lastName, password) = fields

                    val userDto = UserDto(
                        username = username,
                        firstName = firstName,
                        lastName = lastName
                    )
                    try
                    {
                        userService.createUser(userDto, password)
                        logger.info { "IMPORT: Imported $username - $firstName $lastName" }
                    }
                    catch (e: UsernameAlreadyExistsException)
                    {
                        logger.info { "IMPORT: $username - $firstName $lastName was not imported, because it already exists" }
                    }
                    catch (e: Exception)
                    {
                        logger.error { "IMPORT: $username - $firstName $lastName was not imported. (${e.message})" }
                    }
                }
            }
        }
    }

    fun closeImportFilePicker()
    {
        changeUiState(filePickerOpen = false)
    }

    fun register()
    {
        if (uiState.username.isBlank()
            || uiState.firstName.isBlank()
            || uiState.lastName.isBlank()
            || uiState.password.isBlank()
            || uiState.confirmingPassword.isBlank()
        )
        {
            changeUiState(errorText = "All fields must be filled")
            return
        }

        if (uiState.password != uiState.confirmingPassword)
        {
            changeUiState(errorText = "Passwords do not match")
            return
        }

        val userDto = UserDto(
            username = uiState.username,
            firstName = uiState.firstName,
            lastName = uiState.lastName
        )
        val password = uiState.password

        screenModelScope.launch {
            try
            {
                val newUserId = userService.createUser(userDto, password)
                opiNet.currentUser = userDto.copy(id = newUserId)
                changeUiState(registrationSuccessEvent = true)
            }
            catch (e: UnresolvedAddressException)
            {
                changeUiState(errorText = "There was an error connecting to the database, check your internet connection")
            }
            catch (e: UsernameAlreadyExistsException)
            {
                changeUiState(errorText = "Username ${uiState.username} already exists")
                logger.error { e.message }
            }
            catch (e: Exception)
            {
                changeUiState(errorText = "There was an unknown error")
                e.printStackTrace()
            }
        }
    }

    private fun changeUiState(
        username: String = uiState.username,
        firstName: String = uiState.firstName,
        lastName: String = uiState.lastName,
        password: String = uiState.password,
        confirmingPassword: String = uiState.confirmingPassword,
        passwordShown: Boolean = uiState.passwordShown,
        confirmingPasswordShown: Boolean = uiState.confirmingPasswordShown,
        errorText: String = uiState.errorText,
        registrationSuccessEvent: Boolean = uiState.registrationSuccessEvent,
        filePickerOpen: Boolean = uiState.filePickerOpen
    )
    {
        uiState = uiState.copy(
            username = username,
            firstName = firstName,
            lastName = lastName,
            password = password,
            confirmingPassword = confirmingPassword,
            passwordShown = passwordShown,
            confirmingPasswordShown = confirmingPasswordShown,
            errorText = errorText,
            registrationSuccessEvent = registrationSuccessEvent,
            filePickerOpen = filePickerOpen
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
