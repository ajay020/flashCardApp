package com.example.flashcard.ui.navigation

object NavRoutes {
    const val Home = "home"
    const val Profile = "profile"
    const val AddCard = "add_card/{categoryId}"
    const val EditCard = "edit_card/{categoryId}"

    fun editCardRoute(categoryId: Int) = "edit_card/$categoryId"
    fun addCardRoute(categoryId: Int) = "add_card/$categoryId"
}
