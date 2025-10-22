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

@Composable
fun GlobalBottomNavBar(navController: NavController, currentRoute: String) {
    val themeState = LocalThemeState.current
    
    // Colores transparentes según el tema
    val transparentBackground = if (themeState.isDarkMode) 
        Color.Black.copy(alpha = 0.3f) 
    else 
        Color.White.copy(alpha = 0.4f) // Más opaco en modo claro
    
    // Borde para mejor visibilidad
    val borderColor = if (themeState.isDarkMode) 
        Color.White.copy(alpha = 0.2f)
    else 
        Color.Black.copy(alpha = 0.15f) // Borde sutil en modo claro
    
    // Colores para iconos inactivos con mejor contraste
    val inactiveIconColor = if (themeState.isDarkMode) 
        Color.White.copy(alpha = 0.7f)
    else 
        Color.Black.copy(alpha = 0.7f)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .blur(radius = 0.dp), // Sin blur para mantener nitidez
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
                            MetroOrange,
                            RoundedCornerShape(24.dp)
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
            
            // Location Icon
            if (currentRoute == Screen.Estaciones.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroOrange,
                            RoundedCornerShape(24.dp)
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
            
            // Routes Icon
            if (currentRoute == Screen.Rutas.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroOrange,
                            RoundedCornerShape(24.dp)
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
            
                    // Signal Icon (En vivo)
                    if (currentRoute == Screen.Vivo.route) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MetroOrange,
                                    RoundedCornerShape(24.dp)
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
            
            // Settings Icon
            if (currentRoute == Screen.Configuracion.route) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MetroOrange,
                            RoundedCornerShape(24.dp)
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
