package com.tecsup.metrolimago1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.ui.screens.configuracion.ConfiguracionScreen
import com.tecsup.metrolimago1.ui.screens.home.HomeScreen
import com.tecsup.metrolimago1.ui.screens.estaciones.ListaEstacionesScreen
import com.tecsup.metrolimago1.ui.screens.rutas.PlanificadorRutaScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Estaciones : Screen("estaciones")
    object Rutas : Screen("rutas")
    object Configuracion : Screen("configuracion")
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

        composable(Screen.Rutas.route) {
            PlanificadorRutaScreen(navController = navController)
        }

        composable(Screen.Configuracion.route) {
            ConfiguracionScreen(navController = navController)
        }
    }
}