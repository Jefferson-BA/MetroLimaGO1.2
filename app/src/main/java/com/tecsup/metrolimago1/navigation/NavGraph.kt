package com.tecsup.metrolimago1.navigation

import com.tecsup.metrolimago1.ui.screens.intro.IntroScreen
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
import com.tecsup.metrolimago1.ui.screens.splash.SplashScreen
import com.tecsup.metrolimago1.ui.screens.vivo.VivoScreen
import com.tecsup.metrolimago1.ui.screens.maps.MapsScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")               // 👈 nueva ruta para tu GlassIntroScreen
    object Home : Screen("home")
    object Estaciones : Screen("estaciones")
    object EstacionDetail : Screen("estaciones/{estacionId}") {
        fun createRoute(estacionId: String) = "estaciones/$estacionId"
    }
    object Rutas : Screen("rutas")
    object Maps : Screen("maps")
    object Favoritos : Screen("favoritos")
    object LineDetail : Screen("lines/{lineId}") {
        fun createRoute(lineId: String) = "lines/$lineId"
    }
    object Configuracion : Screen("configuracion")
    object Chat : Screen("chat")
    object Vivo : Screen("vivo")
}

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 🟡 Pantalla de carga
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // 🟢 Nueva pantalla de introducción
        composable(Screen.Intro.route) {
            IntroScreen(navController = navController)
        }

        // 🔵 Pantalla principal
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

        composable(Screen.Maps.route) {
            MapsScreen(navController = navController)
        }

        composable(Screen.Favoritos.route) {
            com.tecsup.metrolimago1.ui.screens.estaciones.FavoriteStationsScreen(navController = navController)
        }

        composable(
            route = Screen.LineDetail.route,
            arguments = listOf(navArgument("lineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lineId = backStackEntry.arguments?.getString("lineId")
            com.tecsup.metrolimago1.ui.screens.lines.LineDetailScreen(navController = navController, lineId = lineId)
        }

        composable(Screen.Configuracion.route) {
            ConfiguracionScreen(navController = navController)
        }

        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }

        composable(Screen.Vivo.route) {
            VivoScreen(navController = navController)
        }
    }
}
