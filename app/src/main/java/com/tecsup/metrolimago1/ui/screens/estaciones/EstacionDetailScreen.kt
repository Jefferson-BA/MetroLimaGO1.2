package com.tecsup.metrolimago1.ui.screens.estaciones

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    val station = MockStations.findById(estacionId)
    var showGoogleMap by remember { mutableStateOf(true) }

    // Estado para ubicación del usuario
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var showRoute by remember { mutableStateOf(false) }
    var routeInfo by remember { mutableStateOf<RouteInfo?>(null) }

    // FusedLocationProviderClient
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            // Obtener ubicación actual
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                // Manejar error de permisos
            }
        }
    }

    // Verificar permisos al iniciar
    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocationPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                // Manejar error de permisos
            }
        }
    }

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
                    // Botón para obtener ubicación
                    IconButton(
                        onClick = {
                            if (hasLocationPermission) {
                                try {
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        location?.let {
                                            userLocation = LatLng(it.latitude, it.longitude)
                                        }
                                    }
                                } catch (e: SecurityException) {
                                    // Manejar error de permisos
                                }
                            } else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.station_get_location),
                            tint = MetroGreen
                        )
                    }

                    // Botón para mostrar ruta
                    if (userLocation != null && station != null) {
                        IconButton(
                            onClick = {
                                showRoute = true
                                routeInfo = calculateRouteFromUserLocation(userLocation!!, station)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = stringResource(R.string.station_show_route),
                                tint = MetroOrange
                            )
                        }
                    }

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
                    .verticalScroll(rememberScrollState())
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
                                    position = if (userLocation != null) {
                                        // Centrar entre usuario y estación
                                        val centerLat = (userLocation!!.latitude + station.latitude) / 2
                                        val centerLng = (userLocation!!.longitude + station.longitude) / 2
                                        CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 14f)
                                    } else {
                                        CameraPosition.fromLatLngZoom(
                                            LatLng(station.latitude, station.longitude),
                                            16f
                                        )
                                    }
                                },
                                properties = MapProperties(
                                    isMyLocationEnabled = hasLocationPermission,
                                    mapType = MapType.NORMAL,
                                    isTrafficEnabled = false
                                ),
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = true,
                                    compassEnabled = false,
                                    myLocationButtonEnabled = hasLocationPermission
                                )
                            ) {
                                // Marcador de la estación
                                Marker(
                                    state = MarkerState(position = LatLng(station.latitude, station.longitude)),
                                    title = station.name,
                                    snippet = "${station.line} - ${station.address}"
                                )

                                // Marcador de ubicación del usuario
                                userLocation?.let { userLoc ->
                                    Marker(
                                        state = MarkerState(position = userLoc),
                                        title = stringResource(R.string.station_your_location),
                                        snippet = stringResource(R.string.station_current_position)
                                    )
                                }

                                // Ruta entre usuario y estación
                                if (showRoute && userLocation != null) {
                                    val routePoints = listOf(
                                        userLocation!!,
                                        LatLng(station.latitude, station.longitude)
                                    )
                                    Polyline(
                                        points = routePoints,
                                        color = MetroOrange,
                                        width = 6f
                                    )
                                }
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

                // Información de ruta (si está disponible)
                if (routeInfo != null) {
                    RouteInfoCard(
                        routeInfo = routeInfo!!,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        context = context
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Información adicional
                StationInfoCard(
                    station = station,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Horarios y Estado
                StationScheduleAndStatusCard(
                    station = station,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Servicios Cercanos
                if (station.nearbyServices.isNotEmpty()) {
                    NearbyServicesCard(
                        services = station.nearbyServices,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Información de Tarifas
                FareInfoCard(
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

// Clase para información de rutas
data class RouteInfo(
    val distance: String,
    val duration: String,
    val steps: List<String>
)

// Función para calcular ruta desde ubicación del usuario
fun calculateRouteFromUserLocation(userLocation: LatLng, station: com.tecsup.metrolimago1.domain.models.Station): RouteInfo {
    // Simulación de cálculo de distancia y tiempo
    val distance = "${(1..15).random()} km"
    val duration = "${(5..45).random()} min"
    val steps = listOf(
        "1. Activa tu ubicación GPS",
        "2. Dirígete hacia ${station.name}",
        "3. Sigue las indicaciones del mapa",
        "4. Has llegado a ${station.name}"
    )
    return RouteInfo(distance, duration, steps)
}

@Composable
fun RouteInfoCard(
    routeInfo: RouteInfo,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
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
            // Título de la ruta
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = stringResource(R.string.station_route_title),
                    tint = MetroOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.station_route_title),
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información de distancia y tiempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.station_route_distance),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = routeInfo.distance,
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.station_route_duration),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = routeInfo.duration,
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pasos de la ruta
            Text(
                text = stringResource(R.string.station_route_steps),
                color = textColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            routeInfo.steps.forEach { step ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MetroOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step,
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// Nuevo componente para Horarios y Estado
@Composable
fun StationScheduleAndStatusCard(
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
                text = "Horarios y Estado",
                color = textColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Horarios
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Horarios",
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Horario de funcionamiento",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${station.openingTime} - ${station.closingTime}",
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estado
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Estado",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = when (station.status) {
                            com.tecsup.metrolimago1.domain.models.StationStatus.OPERATIONAL -> "Operativa"
                            com.tecsup.metrolimago1.domain.models.StationStatus.MAINTENANCE -> "En Mantenimiento"
                            com.tecsup.metrolimago1.domain.models.StationStatus.CONSTRUCTION -> "En Construcción"
                            com.tecsup.metrolimago1.domain.models.StationStatus.CLOSED -> "Cerrada"
                        },
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

// Nuevo componente para Servicios Cercanos
@Composable
fun NearbyServicesCard(
    services: List<com.tecsup.metrolimago1.domain.models.NearbyService>,
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
                text = "Servicios Cercanos",
                color = textColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            services.forEach { service ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getServiceIcon(service.type),
                        contentDescription = service.name,
                        tint = MetroOrange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = service.name,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = service.address,
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = service.distance,
                        color = MetroOrange,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

// Función para obtener el icono según el tipo de servicio
@Composable
fun getServiceIcon(type: com.tecsup.metrolimago1.domain.models.ServiceType): ImageVector {
    return when (type) {
        com.tecsup.metrolimago1.domain.models.ServiceType.RESTAURANT -> Icons.Default.Restaurant
        com.tecsup.metrolimago1.domain.models.ServiceType.BANK -> Icons.Default.AccountBalance
        com.tecsup.metrolimago1.domain.models.ServiceType.PHARMACY -> Icons.Default.LocalPharmacy
        com.tecsup.metrolimago1.domain.models.ServiceType.HOSPITAL -> Icons.Default.LocalHospital
        com.tecsup.metrolimago1.domain.models.ServiceType.UNIVERSITY -> Icons.Default.School
        com.tecsup.metrolimago1.domain.models.ServiceType.MALL -> Icons.Default.ShoppingCart
        com.tecsup.metrolimago1.domain.models.ServiceType.PARK -> Icons.Default.Park
        com.tecsup.metrolimago1.domain.models.ServiceType.ATM -> Icons.Default.CreditCard
    }
}

// Nuevo componente para Información de Tarifas
@Composable
fun FareInfoCard(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información de Tarifas",
                color = textColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Precio Adulto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Adulto",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Adulto",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "S/ 2.50",
                    color = MetroOrange,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Precio Estudiante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Estudiante",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estudiante (con carné)",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "S/ 1.25",
                    color = MetroOrange,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            // Métodos de pago
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "Pago",
                    tint = MetroGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Métodos: Efectivo, Tarjeta de Crédito/Débito, Tarjeta Lima",
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}