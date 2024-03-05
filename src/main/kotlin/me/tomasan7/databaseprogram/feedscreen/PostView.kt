package me.tomasan7.databaseprogram.feedscreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexfacciorusso.previewer.previewLightAndDark
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import me.tomasan7.databaseprogram.ui.AppThemePreviewer
import me.tomasan7.databaseprogram.ui.format
import java.time.format.DateTimeFormatter

@Composable
fun Post(post: Post)
{
    ElevatedCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${post.author.firstName} ${post.author.lastName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = post.uploadDate.format(dateFormat),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}

private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

/* Dark mode */
@Composable
@Preview
fun PostPreview()
{
    val author = User(
        "dusan.kucharik",
        "Dušan",
        "Kuchařík"
    )

    val post = Post(
        "Dneska večer hraju v EDM caffee",
        """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. In mollis nunc sed id semper risus in hendrerit. Est placerat in egestas erat. Suspendisse sed nisi lacus sed viverra. Dignissim convallis aenean et tortor.
        """.trimIndent(),
        author,
        LocalDate(2021, 10, 15),
        1
    )

    AppThemePreviewer {
        previewLightAndDark {
            Post(post)
        }
    }
}
