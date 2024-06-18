package com.example.flashcard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcard.model.Category

@Composable
fun CategoryOptionDialog(
    category: Category,
    onDismiss: () -> Unit,
    onOpen: () -> Unit,
    onAdd: () -> Unit,
    onRename: () -> Unit,
    onDelete: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = category.name) },
        text = {
            Column {
                Text(
                    text = "Open",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onOpen() }
                )
                Text(
                    text = "Add Card",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onAdd() }
                )
                Text(
                    text = "Rename",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onRename() }
                )
                Text(
                    text = "Delete",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onDelete(category) }
                )
            }
        },
        confirmButton = {},
        modifier = modifier
    )
}
