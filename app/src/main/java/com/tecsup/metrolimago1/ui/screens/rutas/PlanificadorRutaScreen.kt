package com.tecsup.metrolimago1.ui.screens.rutas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificadorRutaScreen(navController: NavController) {
    val themeState = LocalThemeState.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    // Estado para las estaciones seleccionadas
    var selectedOrigin by remember { mutableStateOf<Station?>(null) }
    var selectedDestination by remember { mutableStateOf<Station?>(null) }
    var isCalculating by remember { mutableStateOf(false) }
    var routeResult by remember { mutableStateOf("") }

    // Cargar estaciones desde MockStations
    val stations = remember { MockStations.stations }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Planificador de Rutas",
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardColor
                )
            )
        },
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Rutas.route)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Título principal
            Text(
                text = "Planifica tu trayecto",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Selecciona tu estación de origen y destino",
                style = MaterialTheme.typography.bodyMedium.copy(color = secondaryTextColor),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Selector de estación de origen
            StationSelector(
                label = "Estación de Origen",
                selectedStation = selectedOrigin,
                onStationSelected = { selectedOrigin = it },
                stations = stations,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                icon = Icons.Default.FlightTakeoff
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de estación de destino
            StationSelector(
                label = "Estación de Destino",
                selectedStation = selectedDestination,
                onStationSelected = { selectedDestination = it },
                stations = stations,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                icon = Icons.Default.FlightLand
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Calcular Ruta
            Button(
                onClick = {
                    if (selectedOrigin != null && selectedDestination != null) {
                        isCalculating = true
                        // Simular cálculo de ruta
                        routeResult = "Ruta calculada de ${selectedOrigin!!.name} a ${selectedDestination!!.name}"
                        isCalculating = false
                    }
                },
                enabled = selectedOrigin != null && selectedDestination != null && !isCalculating,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MetroOrange,
                    contentColor = White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isCalculating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isCalculating) "Calculando..." else "Calcular Ruta",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Mostrar resultado de la ruta
            if (routeResult.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                RouteResultCard(
                    result = routeResult,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }
        }
    }
}

@Composable
fun StationSelector(
    label: String,
    selectedStation: Station?,
    onStationSelected: (Station) -> Unit,
    stations: List<Station>,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    icon: ImageVector
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MetroOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(
                    containerColor = if (expanded) MetroOrange.copy(alpha = 0.1f) else cardColor
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 8.dp else 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono de la estación
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selectedStation != null) MetroOrange else secondaryTextColor,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedStation?.name ?: "Selecciona una estación",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (selectedStation != null) textColor else secondaryTextColor,
                                fontWeight = if (selectedStation != null) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        if (selectedStation != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Indicador de línea
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            when (selectedStation.line) {
                                                "Línea 1" -> MetroOrange
                                                "Línea 2" -> MetroGreen
                                                else -> secondaryTextColor
                                            },
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedStation.line,
                                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                                )
                            }
                        }
                    }
                    
                    // Icono de dropdown
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Cerrar" else "Abrir",
                        tint = if (expanded) MetroOrange else secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        cardColor,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                stations.forEach { station ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Icono de estación
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Estación",
                                    tint = MetroOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = station.name,
                                        color = textColor,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Indicador de línea
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(
                                                    when (station.line) {
                                                        "Línea 1" -> MetroOrange
                                                        "Línea 2" -> MetroGreen
                                                        else -> secondaryTextColor
                                                    },
                                                    RoundedCornerShape(4.dp)
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = station.line,
                                            color = secondaryTextColor,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                                
                                // Icono de dirección
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Seleccionar",
                                    tint = secondaryTextColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },
                        onClick = {
                            onStationSelected(station)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RouteResultCard(
    result: String,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
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
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Ruta calculada",
                tint = MetroGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ruta Calculada",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodyMedium.copy(color = secondaryTextColor)
                )
            }
        }
    }
}
