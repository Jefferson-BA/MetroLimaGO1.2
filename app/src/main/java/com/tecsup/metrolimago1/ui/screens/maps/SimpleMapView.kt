package com.tecsup.metrolimago1.ui.screens.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.ui.theme.*

@Composable
fun SimpleMapView(
    selectedOrigin: Station?,
    selectedDestination: Station?,
    showRoute: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    var showGoogleMap by remember { mutableStateOf(false) }
    
    if (showGoogleMap) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(MockStations.LIMA_CENTER, MockStations.LIMA_ZOOM)
            },
            properties = MapProperties(
                isMyLocationEnabled = false,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            // Marcadores de estaciones
            MockStations.stations.forEach { station ->
                Marker(
                    state = MarkerState(position = LatLng(station.latitude, station.longitude)),
                    title = station.name,
                    snippet = "${station.line} - ${station.address}"
                )
            }

            // Ruta entre estaciones
            if (showRoute && selectedOrigin != null && selectedDestination != null) {
                val originPos = LatLng(selectedOrigin.latitude, selectedOrigin.longitude)
                val destPos = LatLng(selectedDestination.latitude, selectedDestination.longitude)
                
                Polyline(
                    points = listOf(originPos, destPos),
                    color = MetroOrange,
                    width = 8f
                )
            }

            // Líneas del Metro
            MetroLinesOverlay()
        }
    }
    
    if (!showGoogleMap) {
        // Vista de mapa simplificada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(secondaryTextColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Mapa",
                    tint = MetroOrange,
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Mapa del Metro de Lima",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "30 estaciones disponibles",
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón para activar Google Maps
                Button(
                    onClick = { showGoogleMap = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MetroOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Activar Mapa")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Información de estaciones seleccionadas
                if (selectedOrigin != null || selectedDestination != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            if (selectedOrigin != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FlightTakeoff,
                                        contentDescription = "Origen",
                                        tint = MetroOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Origen: ${selectedOrigin.name}",
                                        color = textColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            
                            if (selectedDestination != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FlightLand,
                                        contentDescription = "Destino",
                                        tint = MetroGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Destino: ${selectedDestination.name}",
                                        color = textColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            
                            if (showRoute && selectedOrigin != null && selectedDestination != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Route,
                                        contentDescription = "Ruta",
                                        tint = MetroOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Ruta calculada",
                                        color = MetroOrange,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetroLinesOverlay() {
    // Línea 1 - Naranja
    val linea1Stations = MockStations.getStationsByLine("Línea 1")
    if (linea1Stations.isNotEmpty()) {
        val linea1Points = linea1Stations.map { LatLng(it.latitude, it.longitude) }
        Polyline(
            points = linea1Points,
            color = MetroOrange,
            width = 6f
        )
    }

    // Línea 2 - Verde
    val linea2Stations = MockStations.getStationsByLine("Línea 2")
    if (linea2Stations.isNotEmpty()) {
        val linea2Points = linea2Stations.map { LatLng(it.latitude, it.longitude) }
        Polyline(
            points = linea2Points,
            color = MetroGreen,
            width = 6f
        )
    }

    // Línea 3 - Azul
    val linea3Stations = MockStations.getStationsByLine("Línea 3")
    if (linea3Stations.isNotEmpty()) {
        val linea3Points = linea3Stations.map { LatLng(it.latitude, it.longitude) }
        Polyline(
            points = linea3Points,
            color = Color(0xFF2196F3),
            width = 6f
        )
    }
}
