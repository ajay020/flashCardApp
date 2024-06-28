import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcard.ui.main.MainScreen

@Composable
fun FlashcardApp(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    MainScreen(
        isDarkTheme = isDarkTheme,
        onThemeToggle = onThemeToggle
    )
}
