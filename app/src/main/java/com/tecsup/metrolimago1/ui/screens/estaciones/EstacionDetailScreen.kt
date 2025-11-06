package com.tecsup.metrolimago1.ui.screens.estaciones

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.data.local.FavoriteStation
import com.tecsup.metrolimago1.data.local.database.AppDatabase
import com.tecsup.metrolimago1.data.remote.StationService
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

    // Estado para la estación (obtenida de la API o mock)
    var station by remember { mutableStateOf<Station?>(null) }
    var isLoadingStation by remember { mutableStateOf(true) }
    var showGoogleMap by remember { mutableStateOf(true) }
    
    // Servicio de API
    val stationService = remember { StationService() }

    // Estado para favoritos
    val database = remember { AppDatabase.getDatabase(context) }
    var isFavorite by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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

    // Obtener estación desde la API o usar mock como fallback
    LaunchedEffect(estacionId) {
        if (estacionId != null) {
            isLoadingStation = true
            
            Log.d("EstacionDetailScreen", "Obteniendo estación con ID: $estacionId")
            
            try {
                // Intentar obtener desde la API
                val apiStation = stationService.getStationById(estacionId)
                if (apiStation != null) {
                    Log.d("EstacionDetailScreen", "Estación obtenida de API: ${apiStation.name}, imageUrl: ${apiStation.imageUrl}")
                    station = apiStation
                } else {
                    Log.w("EstacionDetailScreen", "API no devolvió estación, usando mock")
                    station = MockStations.findById(estacionId)
                }
            } catch (e: Exception) {
                // Si falla, usar datos mock
                Log.e("EstacionDetailScreen", "Error al obtener estación de API: ${e.message}", e)
                station = MockStations.findById(estacionId)
            } finally {
                isLoadingStation = false
                Log.d("EstacionDetailScreen", "Carga completada. Estación: ${station?.name}")
            }
        } else {
            station = null
            isLoadingStation = false
        }
    }

    // Verificar si la estación es favorita
    LaunchedEffect(station?.id) {
        station?.let { st ->
            val favorite = database.favoriteStationDao().getById(st.id).first()
            isFavorite = favorite != null
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
                    // Botón de favoritos
                    IconButton(
                        onClick = {
                            station?.let { st ->
                                coroutineScope.launch {
                                    if (isFavorite) {
                                        database.favoriteStationDao().delete(st.id)
                                        isFavorite = false
                                    } else {
                                        database.favoriteStationDao().insert(
                                            FavoriteStation(
                                                id = st.id,
                                                name = st.name,
                                                line = st.line,
                                                address = st.address,
                                                latitude = st.latitude,
                                                longitude = st.longitude
                                            )
                                        )
                                        isFavorite = true
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) Color(0xFFFF6B6B) else textColor
                        )
                    }

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
                        val currentStation = station
                        IconButton(
                            onClick = {
                                showRoute = true
                                currentStation?.let {
                                    routeInfo = calculateRouteFromUserLocation(userLocation!!, it)
                                }
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
        if (isLoadingStation) {
            // Mostrar indicador de carga
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
                    CircularProgressIndicator(
                        color = MetroOrange,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando estación...",
                        color = textColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else if (station == null) {
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
            // Usar let para hacer smart cast
            station?.let { currentStation ->
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
                                        val centerLat = (userLocation!!.latitude + currentStation.latitude) / 2
                                        val centerLng = (userLocation!!.longitude + currentStation.longitude) / 2
                                        CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 14f)
                                    } else {
                                        CameraPosition.fromLatLngZoom(
                                            LatLng(currentStation.latitude, currentStation.longitude),
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
                                    state = MarkerState(position = LatLng(currentStation.latitude, currentStation.longitude)),
                                    title = currentStation.name,
                                    snippet = "${currentStation.line} - ${currentStation.address}"
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
                                        LatLng(currentStation.latitude, currentStation.longitude)
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
                                        text = currentStation.name,
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card de imagen de la estación
                    StationImageCard(
                        imageUrl = currentStation.imageUrl,
                        stationName = currentStation.name,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detalles de la estación (combinado: info + estado + línea)
                    StationDetailsCard(
                        station = currentStation,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        navController = navController
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
                    
                    // Servicios Cercanos (si existen)
                    if (currentStation.nearbyServices.isNotEmpty()) {
                        NearbyServicesCard(
                            services = currentStation.nearbyServices,
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
}

@Composable
fun StationDetailsCard(
    station: com.tecsup.metrolimago1.domain.models.Station,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    navController: NavController
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

            // Información de la línea (clickeable)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { 
                    // Navegar a detalle de línea
                    val lineId = when (station.line) {
                        "Línea 1" -> "LINEA_1"
                        "Línea 2" -> "LINEA_2"
                        "Línea 3" -> "LINEA_3"
                        else -> "LINEA_1"
                    }
                    navController.navigate(Screen.LineDetail.createRoute(lineId))
                }
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
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ver línea",
                    tint = secondaryTextColor,
                    modifier = Modifier.size(16.dp)
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

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Estado y Horarios (combinados)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Estado
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (station.status) {
                            com.tecsup.metrolimago1.domain.models.StationStatus.OPERATIONAL -> "Operativa"
                            com.tecsup.metrolimago1.domain.models.StationStatus.MAINTENANCE -> "Mantenimiento"
                            com.tecsup.metrolimago1.domain.models.StationStatus.CONSTRUCTION -> "Construcción"
                            com.tecsup.metrolimago1.domain.models.StationStatus.CLOSED -> "Cerrada"
                        },
                        color = textColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Horarios
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Horarios",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${station.openingTime} - ${station.closingTime}",
                        color = textColor,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Descripción (si existe)
            if (station.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Descripción",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = station.description,
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
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

// Componente para mostrar la imagen de la estación
@Composable
fun StationImageCard(
    imageUrl: String,
    stationName: String,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNotEmpty() && imageUrl.isNotBlank()) {
                // Mostrar imagen desde la URL
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de $stationName",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Mostrar mensaje cuando no hay imagen
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "Error de carga",
                        tint = secondaryTextColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error de carga",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Esta estación está en construcción o no cuenta con conexión a internet",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}