package com.tecsup.metrolimago1.ui.screens.estaciones

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionDetailScreen(navController: NavController, estacionId: String?) {
    val themeState = LocalThemeState.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    val station = MockStations.findById(estacionId)
    var showGoogleMap by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.station_details_title),
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                            tint = textColor
                        )
                    }
                },
                actions = {
                    // Botón para activar Google Maps
                    IconButton(onClick = { showGoogleMap = true }) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = stringResource(R.string.map_activate_map),
                            tint = MetroOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardColor
                )
            )
        },
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Estaciones.route)
        }
    ) { paddingValues ->
        if (station == null) {
            // Estación no encontrada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOff,
                        contentDescription = stringResource(R.string.station_not_found),
                        tint = secondaryTextColor,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.station_not_found),
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = stringResource(R.string.station_not_found_description),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)
            ) {
                // Mapa de Google Maps
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box {
                        if (showGoogleMap) {
                            // Mapa de Google Maps
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(
                                        LatLng(station.latitude, station.longitude),
                                        16f
                                    )
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
                                // Marcador de la estación
                                Marker(
                                    state = MarkerState(position = LatLng(station.latitude, station.longitude)),
                                    title = station.name,
                                    snippet = "${station.line} - ${station.address}"
                                )
                            }
                        } else {
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
                                        contentDescription = stringResource(R.string.map_map),
                                        tint = MetroOrange,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stringResource(R.string.station_location_title),
                                        color = textColor,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = station.name,
                                        color = secondaryTextColor,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { showGoogleMap = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = MetroOrange),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(stringResource(R.string.map_activate_map))
                                    }
                                }
                            }
                        }
                    }
                }

                // Detalles de la estación
                StationDetailsCard(
                    station = station,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Información adicional
                StationInfoCard(
                    station = station,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }
        }
    }
}

@Composable
fun StationDetailsCard(
    station: com.tecsup.metrolimago1.domain.models.Station,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
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
            // Título de la estación
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.station_location),
                    tint = MetroOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = station.name,
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información de la línea
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            when (station.line) {
                                "Línea 1" -> MetroOrange
                                "Línea 2" -> MetroGreen
                                "Línea 3" -> Color(0xFF2196F3)
                                else -> secondaryTextColor
                            },
                            RoundedCornerShape(6.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = station.line,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dirección
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = stringResource(R.string.station_address),
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = station.address,
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ID de la estación
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Tag,
                    contentDescription = stringResource(R.string.station_id),
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = station.id,
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun StationInfoCard(
    station: com.tecsup.metrolimago1.domain.models.Station,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
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
            Text(
                text = stringResource(R.string.station_additional_info),
                color = textColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Coordenadas
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = stringResource(R.string.station_coordinates),
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.station_latitude),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${station.latitude}",
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.station_longitude),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${station.longitude}",
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            if (station.description.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.station_description),
                        tint = secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.station_description),
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = station.description,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}