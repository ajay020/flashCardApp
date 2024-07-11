package com.example.flashcard.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.flashcard.model.Category
import com.example.flashcard.ui.home.CategoryDetails
import com.example.flashcard.ui.theme.FlashCardTheme

@Composable
fun CategoryOptionDialog(
    category: CategoryDetails,
    onDismiss: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = category.name) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                OutlinedButton(
                    onClick = { onRename() },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    border = null
                ) {
                    Text(
                        "Rename",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                OutlinedButton(
                    onClick = { onDelete() },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    border = null
                ) {
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryOptionDialogPreview() {
    FlashCardTheme(darkTheme = false) {
        CategoryOptionDialog(
            category = CategoryDetails(name = "English"),
            onDismiss = { /*TODO*/ },
            onRename = { /*TODO*/ },
            onDelete = { /*TODO*/ })
    }
}
