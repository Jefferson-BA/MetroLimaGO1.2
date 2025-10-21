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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.components.EstacionCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
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
            var query by remember { mutableStateOf("") }
            var selectedLine by remember { mutableStateOf<String?>(null) }

            // Search + simple filter row
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar estación") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Simple filter buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                val lines = MockStations.stations.map { it.line }.distinct()
                for (line in lines) {
                    val isSelected = selectedLine == line
                    Button(onClick = { selectedLine = if (isSelected) null else line }, modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = line)
                    }
                }
            }

            val filtered = MockStations.stations.filter {
                (selectedLine == null || it.line == selectedLine) && (query.isBlank() || it.name.contains(query, true) || it.id.contains(query, true))
            }

            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(filtered) { station ->
                    EstacionCard(station = station, onClick = { navController.navigate(Screen.EstacionDetail.createRoute(station.id)) })
                }
            }
        }
    }
}