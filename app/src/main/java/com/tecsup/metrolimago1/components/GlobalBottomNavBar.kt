package com.tecsup.metrolimago1.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*

@Composable
fun GlobalBottomNavBar(navController: NavController, currentRoute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        shape = RoundedCornerShape(50.dp)
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
                        tint = LightGray,
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
                        tint = LightGray,
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
                        tint = LightGray,
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
                                tint = LightGray,
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
                        tint = LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
