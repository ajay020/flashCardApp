import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val flashcardRepository: FlashcardRepository,
) : ViewModel() {

    var cardUiState by mutableStateOf(CardUiState())
        private set

    fun updateUiState(cardDetails: CardDetails) {
        cardUiState =
            CardUiState(cardDetails = cardDetails, isEntryValid = validateEntry(cardDetails))
    }

    suspend fun saveCard(categoryId: Int) {
        if (validateEntry()) {
            cardUiState = cardUiState.copy(cardDetails = cardUiState.cardDetails.copy(categoryId = categoryId))
            flashcardRepository.insertFlashcard(cardUiState.cardDetails.toCard())
        }
    }

    private fun validateEntry(cardDetails: CardDetails = cardUiState.cardDetails): Boolean {
        return with(cardDetails) {
            question.isNotBlank() && answer.isNotBlank()
        }
    }
}

data class CardUiState(
    val cardDetails: CardDetails = CardDetails(),
    val isEntryValid: Boolean = false
)

data class CardDetails(
    val id: Int = 0,
    val question: String = "",
    val answer: String = "",
    val categoryId: Int = 0
)

fun CardDetails.toCard() = Flashcard(
    id = id,
    question = question,
    answer = answer,
    categoryId = categoryId
)

fun Flashcard.toCardDetails() : CardDetails = CardDetails(
    id = id ,
    question = question,
    answer = answer,
    categoryId = categoryId,
)

/**
 * Extension function to convert [Flashcard] to [cardUiState]
 */
fun Flashcard.toCardUiState(isEntryValid: Boolean = false): CardUiState = CardUiState(
    cardDetails = this.toCardDetails(),
    isEntryValid = isEntryValid
)
