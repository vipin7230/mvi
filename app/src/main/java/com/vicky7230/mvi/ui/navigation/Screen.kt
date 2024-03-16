package com.vicky7230.mvi.ui.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_screen")

}