package com.example.flashcard.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
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
import com.example.flashcard.ui.components.CategoryCreateDialog
import com.example.flashcard.ui.components.CategoryOptionDialog
import com.example.flashcard.ui.components.ConfirmDeleteDialog
import com.example.flashcard.ui.components.EditCategoryDialog
import com.example.flashcard.ui.components.EmptyMessage
import com.example.flashcard.ui.main.FlashCardAppPreview
import com.example.flashcard.ui.navigation.NavigationDestination
import com.example.flashcard.ui.theme.FlashCardTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    navigateToAddCard: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val categoryList by viewModel.categoryListFlow.collectAsState()
    val homeUiState = viewModel.homeUiState
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        topBar = {
            MainTopBar(
                showTitle = true,
                showActions = true,
                title = stringResource(HomeDestination.titleRes),
                onNavigateUp = onNavigateUp,
                onSettingsClick = navigateToSettings
            )
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
            ),
            categoryList = categoryList,
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

        if (viewModel.showDialog) {
            CategoryCreateDialog(
                onDismiss = {
                    viewModel.showDialog = false
                    viewModel.clearError()
                },
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.saveCategory()
                        if (!viewModel.homeUiState.isDuplicateError) {
                            viewModel.showDialog = false
                        }
                    }
                },
                onCategoryValueChange = viewModel::updateUiState,
                categoryDetails = homeUiState.categoryDetails,
                isDuplicateError = homeUiState.isDuplicateError
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    categoryList: List<CategoryDetails>,
    onCategoryClick: (CategoryDetails) -> Unit,
    onDeleteCategory: (CategoryDetails) -> Unit,
    onEditCategory: (CategoryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categoryList.isEmpty()) {
        EmptyMessage("Add a category to get started", modifier = modifier)
    } else {
        CategoryList(
            modifier = modifier.fillMaxSize(),
            categoryList = categoryList,
            onCategoryClick = onCategoryClick,
            onDelete = onDeleteCategory,
            onEdit = onEditCategory
        )
    }
}



@Composable
fun CategoryList(
    categoryList: List<CategoryDetails>,
    onCategoryClick: (CategoryDetails) -> Unit,
    onDelete: (CategoryDetails) -> Unit,
    onEdit: (CategoryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(categoryList) {
            CategoryListItem(
                categoryDetails = it,
                onEdit = onEdit,
                onCategoryClick = { onCategoryClick(it) },
                onDelete = onDelete
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryListItem(
    categoryDetails: CategoryDetails,
    onEdit: (CategoryDetails) -> Unit,
    onCategoryClick: () -> Unit,
    onDelete: (CategoryDetails) -> Unit,
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showCategoryOptionDialog by remember {
        mutableStateOf(false)
    }

    if (showEditDialog) {
        EditCategoryDialog(
            categoryDetails = categoryDetails,
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
            category = categoryDetails,
            onConfirm = {
                showDialog = false
                onDelete(categoryDetails)
            },
            onDismiss = { showDialog = false }
        )
    }

    if (showCategoryOptionDialog) {
        CategoryOptionDialog(
            category = categoryDetails,
            onDismiss = { showCategoryOptionDialog = false },
            onRename = {
                showEditDialog = true
                showCategoryOptionDialog = false
            },
            onDelete = {
                showDialog = true
                showCategoryOptionDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = { onCategoryClick() },
                onLongClick = { }
            )
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .clickable { onCategoryClick() }
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_small),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(12f),
            ) {
                Text(
                    text = categoryDetails.name,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${categoryDetails.flashcardCount} cards",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            IconButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    showCategoryOptionDialog = true
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


@Preview(showBackground = false)
@Composable
private fun EmptyMessagePreview() {
//    FlashCardTheme(
//        darkTheme = false
//    ) {
//        EmptyMessage()
//    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryItemPreview() {
    val category = CategoryDetails(1, "Category 1 ", flashcardCount = 12)

    FlashCardTheme(
        darkTheme = true
    ) {
        CategoryListItem(
            categoryDetails = category,
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
        CategoryDetails(1, "CategoryDetails 1"),
        CategoryDetails(2, "CategoryDetails 2"),
        CategoryDetails(3, "CategoryDetails 3")
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