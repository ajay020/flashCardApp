import androidx.compose.runtime.Composable
import com.example.flashcard.ui.main.MainScreen

@Composable
fun FlashcardApp(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    MainScreen(
        isDarkTheme = isDarkTheme,
        onThemeToggle = onThemeToggle
    )
}
