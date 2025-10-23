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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.tecsup.metrolimago1.R

@Composable
fun GlobalBottomNavBar(navController: NavController, currentRoute: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 30.dp),
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
                        contentDescription = stringResource(R.string.nav_home),
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
                        contentDescription = stringResource(R.string.nav_home),
                        tint = LightGray,
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
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.nav_stations),
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
                        contentDescription = stringResource(R.string.nav_stations),
                        tint = LightGray,
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

@Preview(showBackground = true)
@Composable
fun GlobalBottomNavBarPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlobalBottomNavBar(
                navController = navController,
                currentRoute = "home" // Puedes cambiar por "rutas", "vivo", etc. para ver el estado activo
            )
        }
    }
}