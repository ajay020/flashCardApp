package com.example.flashcard.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcard.R
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.home.CategoryDetails
import com.example.flashcard.ui.theme.FlashCardTheme

@Composable
fun CardItem(
    flashcard: Flashcard,
    onDelete: (flashcard: Flashcard) -> Unit,
    onUpdate: (flashcard: Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }


    if (showDialog) {
        CardOptionDialog(
            flashcard = flashcard,
            onEdit = { showEditDialog = true },
            onDelete = onDelete,
            onDismiss = { showDialog = false }
        )
    }

    if (showEditDialog) {
        EditFlashcardDialog(
            flashcard = flashcard,
            onConfirm = onUpdate,
            onDismiss = {
                showEditDialog = false
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
//                    horizontal = dimensionResource(R.dimen.padding_small),
//                    vertical = dimensionResource(R.dimen.padding_small)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(12f)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = flashcard.question,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = flashcard.answer,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            IconButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Delete",
                )
            }

        }
    }
}

@Composable
fun CardOptionDialog(
    modifier: Modifier = Modifier,
    flashcard: Flashcard,
    onEdit: () -> Unit,
    onDelete: (flashcard: Flashcard) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                OutlinedButton(
                    onClick = {
                        onEdit()
                        onDismiss()
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    border = null
                ) {
                    Text(
                        "Edit",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                OutlinedButton(
                    onClick = {
                        onDelete(flashcard)
                        onDismiss()
                    },
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

@Composable
fun EditFlashcardDialog(
    flashcard: Flashcard,
    onConfirm: (Flashcard) -> Unit,
    onDismiss: () -> Unit,
) {
    var question by remember { mutableStateOf(flashcard.question) }
    var answer by remember { mutableStateOf(flashcard.answer) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Card") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Front side") },
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Back side") },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        Flashcard(
                            id = flashcard.id,
                            question = question,
                            answer = answer,
                            categoryId = flashcard.categoryId
                        )
                    )
                    onDismiss()
                }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CardItemPreview() {

    FlashCardTheme(
        darkTheme = false
    ) {
//        val flashcard = Flashcard(
//            id = 1,
//            question = "2+2 = ?",
//            answer = "2+2 = 4 ",
//            categoryId = 2
//        )
//        CardItem(flashcard = flashcard)

//        CardOptionDialog(
//            flashcard = Flashcard(
//                id = 1,
//                question = "2+2 = ?",
//                answer = "2+2 = 4 ",
//                categoryId = 2
//            ),
//            onEdit = { /*TODO*/ },
//            onDelete = { /*TODO*/ },
//            onDismiss = { /*TODO*/ }
//        )

        EditFlashcardDialog(flashcard = Flashcard(
            id = 1,
            question = "2+2 = ?",
            answer = "2+2 = 4 ",
            categoryId = 2
        ), onConfirm = {}) {

        }

    }
}