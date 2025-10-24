package com.tecsup.metrolimago1.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.ui.theme.MetroLimaGO1Theme
import com.tecsup.metrolimago1.ui.theme.ThemeState

@Composable
fun GlobalBottomNavBar(navController: NavController, currentRoute: String) {
    val themeState = LocalThemeState.current

    val transparentBackground = if (themeState.isDarkMode)
        Color.Black.copy(alpha = 0.3f)
    else
        Color.White.copy(alpha = 0.4f)

    val borderColor = if (themeState.isDarkMode)
        Color.White.copy(alpha = 0.2f)
    else
        Color.Black.copy(alpha = 0.15f)

    val inactiveIconColor = if (themeState.isDarkMode)
        Color.White.copy(alpha = 0.7f)
    else
        Color.Black.copy(alpha = 0.7f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 22.dp)
            .blur(radius = 0.dp),
        colors = CardDefaults.cardColors(containerColor = transparentBackground),
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Icon
            if (currentRoute == Screen.Home.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroBrightOrange,
                            RoundedCornerShape(22.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = inactiveIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (currentRoute == Screen.Estaciones.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroOrange,
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { navController.navigate(Screen.Estaciones.route) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = inactiveIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (currentRoute == Screen.Rutas.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroOrange,
                            RoundedCornerShape(22.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = "Rutas",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { navController.navigate(Screen.Rutas.route) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = "Rutas",
                        tint = inactiveIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

                    if (currentRoute == Screen.Vivo.route) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MetroOrange,
                                    RoundedCornerShape(22.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "En vivo",
                                tint = White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { navController.navigate(Screen.Vivo.route) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "En vivo",
                                tint = inactiveIconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

            if (currentRoute == Screen.Configuracion.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroBrightOrange,
                            RoundedCornerShape(22.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { navController.navigate(Screen.Configuracion.route) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        tint = inactiveIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Modo Claro - Pantalla completa")
@Composable
fun PreviewGlobalBottomNavBar_LightFull() {
    val fakeNavController = rememberNavController()
    val themeState = ThemeState().apply { updateDarkMode(false) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        GlobalBottomNavBar(
                            navController = fakeNavController,
                            currentRoute = Screen.Home.route
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Modo Oscuro - Pantalla completa", backgroundColor = 0xFF000000)
@Composable
fun PreviewGlobalBottomNavBar_DarkFull() {
    val fakeNavController = rememberNavController()
    val themeState = ThemeState().apply { updateDarkMode(true) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        GlobalBottomNavBar(
                            navController = fakeNavController,
                            currentRoute = Screen.Configuracion.route
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                }
            }
        }
    }
}