package com.tecsup.metrolimago1.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.components.MenuCard
import com.tecsup.metrolimago1.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "MetroLima GO") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF3F51B5), Color(0xFF2196F3))
                    )
                )
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Train,
                contentDescription = "Metro Icono",
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "¡Bienvenido a MetroLima GO!",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            MenuCard(
                title = "Estaciones",
                description = "Explora todas las estaciones disponibles",
                icon = Icons.Default.List,
                onClick = { navController.navigate(Screen.Estaciones.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuCard(
                title = "Rutas",
                description = "Planifica tu trayecto fácilmente",
                icon = Icons.Default.Place,
                onClick = { navController.navigate(Screen.Rutas.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            MenuCard(
                title = "Configuración",
                description = "Personaliza tu experiencia",
                icon = Icons.Default.Settings,
                onClick = { navController.navigate(Screen.Configuracion.route) }
            )
        }
    }
}
