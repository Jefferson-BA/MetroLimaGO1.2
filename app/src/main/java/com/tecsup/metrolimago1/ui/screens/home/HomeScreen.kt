package com.tecsup.metrolimago1.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MetroLima GO") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Metro Icono",
                modifier = Modifier.size(100.dp).padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            NavigationCard(
                title = "Estaciones",
                description = "Ver lista de estaciones",
                onClick = { navController.navigate(Screen.Estaciones.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            NavigationCard(
                title = "Rutas",
                description = "Planificar un trayecto",
                onClick = { navController.navigate(Screen.Rutas.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            NavigationCard(
                title = "Configuración",
                description = "Ajustes de la aplicación",
                onClick = { navController.navigate(Screen.Configuracion.route) }
            )
        }
    }
}

@Composable
fun NavigationCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(90.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}