import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Category
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val flashcardRepository: FlashcardRepository,
) : ViewModel() {
    private val _categoryId = MutableStateFlow(0)
    private val _currentCategory = MutableStateFlow<Category?>(null)
    val currentCategory = _currentCategory.asStateFlow()

    var cardUiState by mutableStateOf(CardUiState())
        private set

    init {
        viewModelScope.launch {
            _categoryId.collectLatest { categoryId ->
                val cardsFlow = flashcardRepository.getCategoryStream(categoryId)
                cardsFlow
                    .collect { category ->
                        _currentCategory.value = category
                    }
            }
        }
    }

    fun updateUiState(cardDetails: CardDetails) {
        cardUiState =
            CardUiState(cardDetails = cardDetails, isEntryValid = validateEntry(cardDetails))
    }

    suspend fun saveCard(categoryId: Int) {
        if (validateEntry()) {
            cardUiState =
                cardUiState.copy(cardDetails = cardUiState.cardDetails.copy(categoryId = categoryId))
            flashcardRepository.insertFlashcard(cardUiState.cardDetails.toCard())
            resetUiState()
        }
    }

    fun setCategoryId(categoryId: Int) {
        _categoryId.value = categoryId
    }

    fun resetUiState() {
        cardUiState = CardUiState()
    }

    private fun validateEntry(cardDetails: CardDetails = cardUiState.cardDetails): Boolean {
        return with(cardDetails) {
            question.isNotBlank() && answer.isNotBlank()
        }
    }
}

data class CardUiState(
    val cardDetails: CardDetails = CardDetails(),
    val isEntryValid: Boolean = false,
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

fun Flashcard.toCardDetails(): CardDetails = CardDetails(
    id = id,
    question = question,
    answer = answer,
    categoryId = categoryId,
)
