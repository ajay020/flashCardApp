package com.example.flashcard.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.ui.main.MainTopBar
import com.example.flashcard.R
import com.example.flashcard.model.Category
import com.example.flashcard.ui.components.ConfirmDeleteDialog
import com.example.flashcard.ui.components.EditCategoryDialog
import com.example.flashcard.ui.navigation.NavigationDestination
import com.example.flashcard.ui.theme.FlashCardTheme

import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@Composable
fun HomeScreen(
    navigateToAddCard: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    isDarkTheme:Boolean,
     onToggleTheme: () -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MainTopBar(
                showTitle = true,
                title = stringResource(HomeDestination.titleRes),
                onNavigateUp = { navController.navigateUp() },
                isDarkTheme = isDarkTheme,
                onThemeToggle = { onToggleTheme () }
            )
        }
    ) {
        HomeScreenContent(
            modifier = modifier.padding(it),
            categoryList = homeUiState.categoryList,
            onCategoryClick = {
                navigateToAddCard(it.id)
            },
            onDeleteCategory = {
                coroutineScope.launch {
                    viewModel.deleteCategory(it)
                }
            },
            onEditCategory = {
                coroutineScope.launch {
                    viewModel.updateCategory(it)
                }
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    categoryList: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onEditCategory: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categoryList.isEmpty()) {
        EmptyCategoryMessage(modifier = modifier)
    } else {
        CategoryList(
            modifier = modifier,
            categoryList = categoryList,
            onCategoryClick = onCategoryClick,
            onDelete = onDeleteCategory,
            onEdit = onEditCategory
        )
    }
}

@Composable
fun EmptyCategoryMessage(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
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
    onCategoryClick: (Category) -> Unit,
    onDelete: (Category) -> Unit,
    onEdit: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(categoryList) {
            CategoryListItem(
                category = it,
                onEdit = onEdit,
                onCategoryClick = { onCategoryClick(it) },
                onDelete = onDelete
            )
        }
    }
}


@Composable
fun CategoryListItem(
    category: Category,
    onEdit: (Category) -> Unit,
    onCategoryClick: () -> Unit,
    onDelete: (Category) -> Unit,
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    if (showEditDialog) {
        EditCategoryDialog(
            category = category,
            onConfirm = {
                onEdit(it)
                showEditDialog = false
            },
            onDismiss = {
                showEditDialog = false
            },
        )
    }

    if (showDialog) {
        ConfirmDeleteDialog(
            category = category,
            onConfirm = {
                showDialog = false
                onDelete(category)
            },
            onDismiss = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .clickable { onCategoryClick() }
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(R.dimen.padding_small)),
    ) {
        Row(
            modifier = Modifier
                .clickable { onCategoryClick() }
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
//            Text(
//                text = "(12 cards)",
//                style = MaterialTheme.typography.displayMedium,
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.weight(1f)
//            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CategoryItemPreview() {
    val category = Category(1, "Category 1")

    FlashCardTheme(
        darkTheme = true
    ) {
        CategoryListItem(
            category = category,
            onEdit = {},
            onDelete = {},
            onCategoryClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun CategoryListPreview() {
    val categoryList = listOf(
        Category(1, "Category 1"),
        Category(2, "Category 2"),
        Category(3, "Category 3")
    )

    FlashCardTheme(
        darkTheme = true
    ) {
        CategoryList(
            categoryList = categoryList,
            onCategoryClick = {},
            onDelete = {},
            onEdit = {}
        )
    }
}