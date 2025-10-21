package com.tecsup.metrolimago1.ui.screens.rutas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificadorRutaScreen(navController: NavController) {
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planificador de Rutas") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Planifica tu trayecto 🚉",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = origen,
                onValueChange = { origen = it },
                label = { Text("Estación de origen") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Estación de destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    mensaje = if (origen.isNotBlank() && destino.isNotBlank()) {
                        "Ruta planificada de $origen a $destino"
                    } else {
                        "Por favor ingresa ambas estaciones"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular Ruta")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (mensaje.isNotBlank()) {
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}