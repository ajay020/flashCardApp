package com.example.flashcard.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.flashcard.model.Category
import com.example.flashcard.ui.home.CategoryDetails


@Composable
fun ConfirmDeleteDialog(
    category: CategoryDetails,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Delete Category")
        },
        text = {
            Text("Are you sure you want to delete the category '${category.name}'?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}