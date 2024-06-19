package com.example.flashcard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcard.CategoryDetails

@Composable
fun CategoryCreateDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCategoryValueChange: (CategoryDetails) -> Unit,
    categoryDetails: CategoryDetails,
    isDuplicateError: Boolean
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Create Category") },
        text = {
            Column {
                TextField(
                    value = categoryDetails.name,
                    onValueChange = {
                        onCategoryValueChange(categoryDetails.copy(name = it))
                    },
                    label = { Text("Category Name") },
                    isError = isDuplicateError
                )
                if (isDuplicateError) {
                    Text(
                        text = "Category name already exists",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = categoryDetails.name.isNotBlank(),
                onClick = {
                    onConfirm()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryCreateDialogPreview() {
    CategoryCreateDialog(
        onDismiss = { /*TODO*/ },
        onConfirm = { /*TODO*/ },
        onCategoryValueChange = { /*TODO*/ },
        categoryDetails = CategoryDetails(),
        isDuplicateError = true
    )
}