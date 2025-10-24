package com.tecsup.metrolimago1.ui.screens.estaciones

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tecsup.metrolimago1.components.TopBar
import com.tecsup.metrolimago1.data.local.MockStations
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionDetailScreen(navController: NavController, estacionId: String?) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Detalle estación",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        val station = MockStations.findById(estacionId)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (station == null) {
                Text(text = "Estación no encontrada", style = MaterialTheme.typography.titleLarge)
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = station.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "ID: ${station.id}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Línea: ${station.line}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Dirección: ${station.address}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Coordenadas: ${station.latitude}, ${station.longitude}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Text(text = station.description, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
