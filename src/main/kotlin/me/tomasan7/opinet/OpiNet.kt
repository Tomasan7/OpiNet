package me.tomasan7.opinet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import ch.qos.logback.classic.Level
import kotlinx.coroutines.runBlocking
import me.tomasan7.opinet.comment.CommentService
import me.tomasan7.opinet.comment.DatabaseCommentService
import me.tomasan7.opinet.config.Config
import me.tomasan7.opinet.config.ConfigProvider
import me.tomasan7.opinet.config.FileConfigProvider
import me.tomasan7.opinet.loginscreen.LoginScreen
import me.tomasan7.opinet.post.DatabasePostService
import me.tomasan7.opinet.post.PostService
import me.tomasan7.opinet.ui.theme.AppTheme
import me.tomasan7.opinet.user.DatabaseUserService
import me.tomasan7.opinet.user.UserDto
import me.tomasan7.opinet.user.UserService
import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OpiNet : ConfigProvider, ScreenModel
{
    lateinit var currentUser: UserDto

    private lateinit var configProvider: ConfigProvider
    private lateinit var config: Config
    private lateinit var database: Database
    lateinit var userService: UserService
        private set
    lateinit var postService: PostService
        private set
    lateinit var commentService: CommentService
        private set

    fun init()
    {
        initConfig()
        initLogging()
        initDatabase()
        initServices()
    }

    private fun initLogging()
    {
        val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger
        rootLogger.level = Level.toLevel(config.logLevel, Level.INFO)
    }

    private fun initConfig()
    {
        configProvider = FileConfigProvider("opinet.conf")
        reloadConfig()
    }

    private fun initDatabase()
    {
        val dbConf = config.database

        database = if (dbConf.driver != null)
            Database.connect(
                url = dbConf.url,
                driver = dbConf.driver,
                user = dbConf.user ?: "",
                password = dbConf.password ?: ""
            )
        else
            Database.connect(
                url = dbConf.url,
                user = dbConf.user ?: "",
                password = dbConf.password ?: ""
            )
    }

    private fun initServices()
    {
        runBlocking {
            userService = DatabaseUserService(database).also { it.init() }
            commentService = DatabaseCommentService(database).also { it.init() }
            postService = DatabasePostService(database, commentService).also { it.init() }
        }
    }

    fun start() = application {
        Window(
            title = "OpiNet",
            icon = painterResource("opinet.png"),
            onCloseRequest = ::exitApplication
        ) {
            AppTheme {
                Navigator(LoginScreen) { navigator ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        ) {
                            navigator.rememberNavigatorScreenModel { this@OpiNet }
                            CurrentScreen()
                        }
                    }
                }
            }
        }
    }

    override fun getConfig() = config

    fun reloadConfig()
    {
        config = configProvider.getConfig()
    }
}

@Composable
fun Navigator.getOpiNet() = rememberNavigatorScreenModel<OpiNet> {
    throw IllegalStateException("OpiNet not found")
}
