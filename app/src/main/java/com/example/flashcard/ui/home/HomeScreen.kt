package com.example.flashcard.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.FlashcardViewModel
import com.example.flashcard.model.Category
import com.example.flashcard.ui.components.CategoryCreateDialog
import com.example.flashcard.ui.components.CategoryOptionDialog
import com.example.flashcard.ui.components.EditCategoryDialog

import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val showDialog by viewModel.showDialog
    val showEditCategoryDialog by viewModel.showEditCategoryDialog
    val selectedCategory by viewModel.selectedCategory
    val coroutineScope = rememberCoroutineScope()


    HomeScreenContent(
        modifier = modifier,
        onCategoryLongPress = viewModel::onCategoryLongPress,
        categoryList = homeUiState.categoryList
    )

    if (showDialog && selectedCategory != null) {
        CategoryOptionDialog(
            category = selectedCategory!!,
            onDismiss = viewModel::onDialogDismiss,
            onOpen = { /* TODO: Handle Open */ },
            onAdd = { /* TODO: Handle Add */ },
            onRename = {
                viewModel.onDialogDismiss()
                viewModel.showEditCategoryDialog()
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteCategory(it)
                    viewModel.onDialogDismiss()
                }
            }
        )
    }

    if (showEditCategoryDialog && selectedCategory != null) {
        EditCategoryDialog(
            category = selectedCategory!!,
            onConfirm = {
                coroutineScope.launch {
                    viewModel.updateCategory(
                        Category(id = selectedCategory!!.id, name = it.trim())
                    )
                }
                    viewModel.dismissEditCategoryDialog()
            },
            onDismiss = {
                viewModel.dismissEditCategoryDialog()
            },
        )
    }
}

@Composable
fun HomeScreenContent(
    categoryList: List<Category>,
    onCategoryLongPress: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categoryList.isEmpty()) {
        EmptyCategoryMessage(modifier = modifier)
    } else {
        CategoryList(
            categoryList = categoryList,
            onCategoryLongPress = onCategoryLongPress
        )
    }
}

@Composable
fun EmptyCategoryMessage(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Create a Category",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun CategoryList(
    categoryList: List<Category>,
    onCategoryLongPress: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(categoryList) {
            CategoryItem(
                category = it,
                onLongPress = { onCategoryLongPress(it) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryItem(
    category: Category,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { /* Handle click if needed */ },
                onLongClick = onLongPress
            )
    ) {
        Text(
            text = category.name,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryItemPreview() {
    val category = Category(1, "Category 1")
    CategoryItem(
        category = category,
        onLongPress = {}
    )
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun CategoryListPreview() {
    val categoryList = listOf(
        Category(1, "Category 1"),
        Category(2, "Category 2"),
        Category(3, "Category 3")
    )
    CategoryList(
        categoryList = categoryList,
        onCategoryLongPress = {}
    )
}