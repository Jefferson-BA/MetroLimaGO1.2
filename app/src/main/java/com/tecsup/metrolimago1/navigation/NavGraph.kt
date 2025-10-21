package com.tecsup.metrolimago1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.metrolimago1.ui.screens.configuracion.ConfiguracionScreen
import com.tecsup.metrolimago1.ui.screens.home.HomeScreen
import com.tecsup.metrolimago1.ui.screens.estaciones.ListaEstacionesScreen
import com.tecsup.metrolimago1.ui.screens.rutas.PlanificadorRutaScreen
import com.tecsup.metrolimago1.ui.screens.chat.ChatScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Estaciones : Screen("estaciones")
    object EstacionDetail : Screen("estaciones/{estacionId}") {
        fun createRoute(estacionId: String) = "estaciones/$estacionId"
    }
    object Rutas : Screen("rutas")
    object Configuracion : Screen("configuracion")
    object Chat : Screen("chat")
}

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Estaciones.route) {
            ListaEstacionesScreen(navController = navController)
        }

        composable(
            route = Screen.EstacionDetail.route,
            arguments = listOf(navArgument("estacionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val estacionId = backStackEntry.arguments?.getString("estacionId")
            com.tecsup.metrolimago1.ui.screens.estaciones.EstacionDetailScreen(
                navController = navController,
                estacionId = estacionId
            )
        }

        composable(Screen.Rutas.route) {
            PlanificadorRutaScreen(navController = navController)
        }

        composable(Screen.Configuracion.route) {
            ConfiguracionScreen(navController = navController)
        }

        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
    }
}