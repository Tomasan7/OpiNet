package me.tomasan7.databaseprogram.util

import androidx.compose.runtime.Composable
import com.alexfacciorusso.previewer.Previewer
import com.alexfacciorusso.previewer.PreviewerScope
import me.tomasan7.databaseprogram.ui.theme.AppTheme

@Composable
fun AppThemePreviewer(content: PreviewerScope.() -> Unit) {
    Previewer(wrapperBlock = { wrappedContent ->
        AppTheme(useDarkTheme = previewTheme.isDark) {
            wrappedContent()
        }
    }, content = content)
}
