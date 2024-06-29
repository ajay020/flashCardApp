package com.example.flashcard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SheetContent(
    showCategoryDialog: () -> Unit,
    onClose: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        OutlinedButton(
            onClick = showCategoryDialog
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Card")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                showCategoryDialog()
                onClose()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Create Deck")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SheetContentPreview() {
    SheetContent(showCategoryDialog = {}, onClose = {})
}