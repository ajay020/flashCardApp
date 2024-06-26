package com.example.flashcard.ui.learn

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flashcard.ui.flashcard.FlashcardTopBar

@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { FlashcardTopBar() }
    ) {
        LearnScreenContent(modifier = Modifier.padding(it))
    }
}

@Composable
fun LearnScreenContent(modifier: Modifier = Modifier) {

}