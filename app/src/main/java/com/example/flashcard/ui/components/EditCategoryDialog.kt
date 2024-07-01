package com.example.flashcard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flashcard.model.Category
import com.example.flashcard.ui.home.CategoryDetails

@Composable
fun EditCategoryDialog(
    categoryDetails: CategoryDetails,
    onConfirm: (CategoryDetails) -> Unit,
    onDismiss: () -> Unit,
) {
    var newName by remember { mutableStateOf(categoryDetails.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Category Name") },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        CategoryDetails(
                            id = categoryDetails.id,
                            name = newName,
                            flashcardCount = categoryDetails.flashcardCount
                        )
                    )
                }) {
                Text("Rename")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}