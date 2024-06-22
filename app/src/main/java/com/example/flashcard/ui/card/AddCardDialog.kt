
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddCardDialog(
    uiState: CardUiState,
    onCardValueChange: (CardDetails) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add Card") },
        text = {
            Column {
                CardInputForm(
                    cardDetails = uiState.cardDetails,
                    onValueChange = onCardValueChange
                )
            }
        },
        confirmButton = {
            Button(onClick = onSaveClick) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
