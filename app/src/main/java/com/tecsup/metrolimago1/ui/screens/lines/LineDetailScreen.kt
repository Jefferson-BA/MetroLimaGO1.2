package com.tecsup.metrolimago1.ui.screens.lines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockLines
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineDetailScreen(navController: NavController, lineId: String?) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    val metroLine = MockLines.findById(lineId)
    
    // Estado para la estación seleccionada
    var selectedStation by remember { mutableStateOf<Station?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = metroLine?.name ?: "Línea",
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardColor
                )
            )
        },
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = "")
        }
    ) { paddingValues ->
        if (metroLine == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Línea no encontrada",
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)
            ) {
                // Información de la línea
                item {
                    LineInfoCard(
                        line = metroLine,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )
                }

                // Mapa con recorrido
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LineMapCard(
                        line = metroLine,
                        selectedStation = selectedStation,
                        onStationSelected = { station -> selectedStation = station },
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )
                }
                
                // Información de la estación seleccionada
                selectedStation?.let { station ->
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SelectedStationInfoCard(
                            station = station,
                            cardColor = cardColor,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            onCloseClick = { selectedStation = null },
                            onViewDetailsClick = {
                                navController.navigate(Screen.EstacionDetail.createRoute(station.id))
                            }
                        )
                    }
                }

                // Lista de estaciones
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Estaciones",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(metroLine.stations) { station ->
                    StationItemCard(
                        station = station,
                        isSelected = selectedStation?.id == station.id,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        lineColor = parseColor(metroLine.color),
                        onClick = {
                            selectedStation = station
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun LineInfoCard(
    line: com.tecsup.metrolimago1.domain.models.Line,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Estado de la línea
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = when (line.status) {
                        com.tecsup.metrolimago1.domain.models.LineStatus.OPERATIONAL -> Icons.Default.CheckCircle
                        com.tecsup.metrolimago1.domain.models.LineStatus.MAINTENANCE -> Icons.Default.Warning
                        com.tecsup.metrolimago1.domain.models.LineStatus.CONSTRUCTION -> Icons.Default.Construction
                        com.tecsup.metrolimago1.domain.models.LineStatus.EXPANSION -> Icons.Default.TrendingUp
                    },
                    contentDescription = "Estado",
                    tint = when (line.status) {
                        com.tecsup.metrolimago1.domain.models.LineStatus.OPERATIONAL -> MetroGreen
                        com.tecsup.metrolimago1.domain.models.LineStatus.MAINTENANCE -> Color(0xFFFFA726)
                        com.tecsup.metrolimago1.domain.models.LineStatus.CONSTRUCTION -> Color(0xFF2196F3)
                        com.tecsup.metrolimago1.domain.models.LineStatus.EXPANSION -> Color(0xFF2196F3)
                    },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (line.status) {
                        com.tecsup.metrolimago1.domain.models.LineStatus.OPERATIONAL -> "Operativa"
                        com.tecsup.metrolimago1.domain.models.LineStatus.MAINTENANCE -> "En Mantenimiento"
                        com.tecsup.metrolimago1.domain.models.LineStatus.CONSTRUCTION -> "En Construcción"
                        com.tecsup.metrolimago1.domain.models.LineStatus.EXPANSION -> "En Expansión"
                    },
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            Text(
                text = line.description,
                color = secondaryTextColor,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Fecha de apertura",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = line.openingDate,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Column {
                    Text(
                        text = "Horario",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = line.operatingHours,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Número de estaciones
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Estaciones",
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${line.stations.size} estaciones",
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LineMapCard(
    line: com.tecsup.metrolimago1.domain.models.Line,
    selectedStation: Station?,
    onStationSelected: (Station) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    val cameraPositionState = rememberCameraPositionState {
        position = when {
            selectedStation != null -> {
                CameraPosition.fromLatLngZoom(
                    LatLng(selectedStation.latitude, selectedStation.longitude),
                    15f
                )
            }
            line.stations.isNotEmpty() -> {
                CameraPosition.fromLatLngZoom(
                    LatLng(line.stations.first().latitude, line.stations.first().longitude),
                    12f
                )
            }
            else -> CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 11f)
        }
    }
    
    // Actualizar la cámara cuando cambia la estación seleccionada
    LaunchedEffect(selectedStation?.id) {
        selectedStation?.let { station ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(station.latitude, station.longitude),
                15f
            )
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Dibujar el recorrido de la línea
            if (line.route.size >= 2) {
                Polyline(
                    points = line.route,
                    color = parseColor(line.color),
                    width = 8f
                )
            }

            // Marcadores de estaciones
            line.stations.forEach { station ->
                val isSelected = selectedStation?.id == station.id
                Marker(
                    state = MarkerState(
                        position = LatLng(station.latitude, station.longitude)
                    ),
                    title = station.name,
                    snippet = station.address,
                    onClick = {
                        onStationSelected(station)
                        true
                    }
                )
            }
        }
    }
}

@Composable
fun StationItemCard(
    station: Station,
    isSelected: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    lineColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) lineColor.copy(alpha = 0.2f) else cardColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, lineColor)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de color de la línea
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(50.dp)
                    .background(lineColor, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Icono de estación
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Estación",
                tint = lineColor,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información de la estación
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = station.address,
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Indicador de estado
            Icon(
                imageVector = when (station.status) {
                    com.tecsup.metrolimago1.domain.models.StationStatus.OPERATIONAL -> Icons.Default.CheckCircle
                    com.tecsup.metrolimago1.domain.models.StationStatus.MAINTENANCE -> Icons.Default.Warning
                    com.tecsup.metrolimago1.domain.models.StationStatus.CONSTRUCTION -> Icons.Default.Construction
                    com.tecsup.metrolimago1.domain.models.StationStatus.CLOSED -> Icons.Default.Close
                },
                contentDescription = "Estado",
                tint = when (station.status) {
                    com.tecsup.metrolimago1.domain.models.StationStatus.OPERATIONAL -> MetroGreen
                    com.tecsup.metrolimago1.domain.models.StationStatus.MAINTENANCE -> Color(0xFFFFA726)
                    com.tecsup.metrolimago1.domain.models.StationStatus.CONSTRUCTION -> Color(0xFF2196F3)
                    com.tecsup.metrolimago1.domain.models.StationStatus.CLOSED -> Color(0xFFE53E3E)
                },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SelectedStationInfoCard(
    station: Station,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onCloseClick: () -> Unit,
    onViewDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Información",
                        tint = MetroOrange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Estación Seleccionada",
                            color = textColor,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = station.name,
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = secondaryTextColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = station.address,
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = onViewDetailsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MetroOrange
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ver Detalles",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

fun parseColor(colorString: String): androidx.compose.ui.graphics.Color {
    return try {
        // Si es un formato de Color de Compose
        if (colorString.startsWith("Color(") && colorString.endsWith(")")) {
            val hexValue = colorString.substringAfter("(").substringBefore(")")
            Color(android.graphics.Color.parseColor("#$hexValue"))
        } else {
            // Intenta parsear como color hexadecimal
            Color(android.graphics.Color.parseColor(colorString))
        }
    } catch (e: Exception) {
        MetroOrange // Color por defecto
    }
}
