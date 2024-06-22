import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CardInputForm(
    modifier: Modifier = Modifier,
    cardDetails: CardDetails,
    onValueChange: (CardDetails) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = cardDetails.question,
            minLines = 2,
            onValueChange = { onValueChange(cardDetails.copy(question = it)) },
            label = { Text("Front Side") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter front side") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = cardDetails.answer,
            onValueChange = { onValueChange(cardDetails.copy(answer = it)) },
            label = { Text("Back Side") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardInputFormPreview() {
    CardInputForm(cardDetails = CardDetails()) {

    }
}
