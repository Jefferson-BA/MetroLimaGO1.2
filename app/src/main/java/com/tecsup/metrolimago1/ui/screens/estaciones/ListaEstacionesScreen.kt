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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.tecsup.metrolimago1.components.EstacionCard
import com.tecsup.metrolimago1.components.TopBar
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.data.database.DatabaseModule
import com.tecsup.metrolimago1.data.repository.StationRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionesScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel = remember {
        val stationDao = DatabaseModule.getStationDao(context)
        val repository = StationRepository(stationDao)
        ListaEstacionesViewModel(repository)
    }
    val stations by viewModel.stations.collectAsStateWithLifecycle()
    val availableLines by viewModel.availableLines.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedLine by viewModel.selectedLine.collectAsStateWithLifecycle()

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
            // Search field
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    label = { Text("Buscar estación") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Filter buttons for lines
            Row(modifier = Modifier.fillMaxWidth()) {
                for (line in availableLines) {
                    val isSelected = selectedLine == line
                    Button(
                        onClick = { viewModel.selectLine(if (isSelected) null else line) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = line)
                    }
                }
            }

            // Loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Stations list
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(stations) { station ->
                        EstacionCard(
                            station = station,
                            onClick = { navController.navigate(Screen.EstacionDetail.createRoute(station.id)) }
                        )
                    }
                }
            }
        }
    }
}