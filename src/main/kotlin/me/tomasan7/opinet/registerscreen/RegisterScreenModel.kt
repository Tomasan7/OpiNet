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

    fun setUsername(username: String) = changeUiState(username = username.removeWhitespace())

    fun setFirstName(firstName: String) = changeUiState(firstName = firstName.removeWhitespace())

    fun setLastName(lastName: String) = changeUiState(lastName = lastName.removeWhitespace())

    fun setPassword(password: String) = changeUiState(password = password.removeWhitespace())

    fun setConfirmingPassword(confirmingPassword: String) = changeUiState(confirmingPassword = confirmingPassword.removeWhitespace())

    fun changePasswordVisibility() = changeUiState(passwordShown = !uiState.passwordShown)

    fun changeConfirmingPasswordVisibility() = changeUiState(confirmingPasswordShown = !uiState.confirmingPasswordShown)

    fun registrationSuccessEventConsumed() = changeUiState(registrationSuccessEvent = false)

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
                    if (fields.size != 3)
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
            || uiState.password != uiState.confirmingPassword
        )
            return

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
            catch (e: UsernameAlreadyExistsException)
            {
                logger.error { e.message }
            }
            catch (e: Exception)
            {
                logger.error { e.message }
            }
        }
    }

    private fun changeUiState(
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        password: String? = null,
        confirmingPassword: String? = null,
        passwordShown: Boolean? = null,
        confirmingPasswordShown: Boolean? = null,
        registrationSuccessEvent: Boolean? = null,
        filePickerOpen: Boolean? = null
    )
    {
        uiState = uiState.copy(
            username = username ?: uiState.username,
            firstName = firstName ?: uiState.firstName,
            lastName = lastName ?: uiState.lastName,
            password = password ?: uiState.password,
            confirmingPassword = confirmingPassword ?: uiState.confirmingPassword,
            passwordShown = passwordShown ?: uiState.passwordShown,
            confirmingPasswordShown = confirmingPasswordShown ?: uiState.confirmingPasswordShown,
            registrationSuccessEvent = registrationSuccessEvent ?: uiState.registrationSuccessEvent,
            filePickerOpen = filePickerOpen ?: uiState.filePickerOpen
        )
    }

    private fun String.removeWhitespace() = this.replace(Regex("\\s"), "")
}
