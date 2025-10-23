package com.tecsup.metrolimago1.ui.screens.estaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionesScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    
    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)
    
    var query by remember { mutableStateOf("") }
    var selectedLines by remember { mutableStateOf(setOf<String>()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.stations_title),
                        color = textColor,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Estaciones.route)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botones de filtro de línea
            LineFilterButtons(
                selectedLines = selectedLines,
                onLineToggle = { line ->
                    selectedLines = if (selectedLines.contains(line)) {
                        selectedLines - line
                    } else {
                        selectedLines + line
                    }
                },
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Indicador de líneas seleccionadas
            if (selectedLines.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.stations_showing_lines, selectedLines.size),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lista de estaciones con filtrado mejorado
            val filtered = MockStations.stations.filter { station ->
                // Filtro por líneas seleccionadas
                val lineMatch = if (selectedLines.isEmpty()) {
                    true // Si no hay líneas seleccionadas, mostrar todas
                } else {
                    selectedLines.contains(station.line)
                }
                
                // Filtro por búsqueda (nombre, dirección, descripción)
                val searchMatch = if (query.isBlank()) {
                    true
                } else {
                    station.name.contains(query, ignoreCase = true) ||
                    station.address.contains(query, ignoreCase = true) ||
                    station.description.contains(query, ignoreCase = true) ||
                    station.id.contains(query, ignoreCase = true)
                }
                
                lineMatch && searchMatch
            }
            
            if (filtered.isEmpty()) {
                // Mensaje cuando no hay resultados
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "Sin resultados",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (query.isNotEmpty()) "No se encontraron estaciones" else "No hay estaciones en esta línea",
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (query.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Intenta con otros términos de búsqueda",
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { station ->
                        StationCard(
                            station = station,
                            onClick = { navController.navigate(Screen.EstacionDetail.createRoute(station.id)) },
                            cardColor = cardColor,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = secondaryTextColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.stations_search_placeholder),
                        color = secondaryTextColor
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )
            
            // Botón de limpiar búsqueda
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LineFilterButtons(
    selectedLines: Set<String>,
    onLineToggle: (String) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = stringResource(R.string.stations_filter_lines),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón Línea 1
            FilterChip(
                onClick = { onLineToggle("Línea 1") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MetroOrange, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.line_1))
                    }
                },
                selected = selectedLines.contains("Línea 1"),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MetroOrange,
                    selectedLabelColor = White,
                    containerColor = secondaryTextColor.copy(alpha = 0.1f)
                )
            )

            // Botón Línea 2
            FilterChip(
                onClick = { onLineToggle("Línea 2") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MetroGreen, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.line_2))
                    }
                },
                selected = selectedLines.contains("Línea 2"),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MetroGreen,
                    selectedLabelColor = White,
                    containerColor = secondaryTextColor.copy(alpha = 0.1f)
                )
            )

            // Botón Línea 3
            FilterChip(
                onClick = { onLineToggle("Línea 3") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF2196F3), RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.line_3))
                    }
                },
                selected = selectedLines.contains("Línea 3"),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2196F3),
                    selectedLabelColor = White,
                    containerColor = secondaryTextColor.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
fun StationCard(
    station: com.tecsup.metrolimago1.domain.models.Station,
    onClick: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de ubicación
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                tint = secondaryTextColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Información de la estación
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = station.address,
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Dirección",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            
            // Indicador de tiempo
            Card(
                colors = CardDefaults.cardColors(containerColor = MetroGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${(3..15).random()} min",
                    color = White,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}