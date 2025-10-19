package com.tecsup.metrolimago1.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Estaciones : Screen("estaciones")
    object Rutas : Screen("rutas")
    object Configuracion : Screen("configuracion")
}