package com.tamakara.booth.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Publish : Screen("publish")
    object Profile : Screen("profile")
    object ItemDetail : Screen("item/{itemId}") {
        fun createRoute(itemId: Long) = "item/$itemId"
    }
}

