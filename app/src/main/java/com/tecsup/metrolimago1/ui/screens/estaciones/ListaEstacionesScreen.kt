package com.tecsup.metrolimago1.ui.screens.estaciones

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.components.TopBar
import com.tecsup.metrolimago1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Lista de Estaciones",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Placeholder: single item that navigates to detail with sample id
            Card(onClick = { navController.navigate(Screen.EstacionDetail.createRoute("Lima-01")) }, modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Estacion", modifier = Modifier.size(48.dp))
                    Text(text = "Estación: Lima-01", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Toque para ver detalle", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}