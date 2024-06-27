package com.example.flashcard.ui.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardTopBar(
    modifier: Modifier = Modifier,
    canClose: Boolean = false,
    onClose: () -> Unit = {},
    title: String = "",
) {

    TopAppBar(
        modifier = modifier.height(56.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Blue
        ),
//        modifier = Modifier
//            .border(2.dp, Color.Red, RectangleShape)
//            .padding(4.dp)
//            .background(Color.Green),
        title = {
            Text(
                text = title,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            if (canClose) {
                IconButton(onClick = onClose) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close"
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FlashcardTopBarPreview() {
    FlashcardTopBar(
        canClose = true
    )
}
