package com.example.shoppinglisttwooseven

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object MapScreen : Screen("map_screen")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route // Исправлено здесь
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screen.MapScreen.route) {
            MapScreen(navController)
        }
    }
}