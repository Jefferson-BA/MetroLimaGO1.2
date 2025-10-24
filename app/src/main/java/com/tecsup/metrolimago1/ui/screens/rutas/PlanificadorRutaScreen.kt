package com.tecsup.metrolimago1.ui.screens.rutas

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
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
import com.tecsup.metrolimago1.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState

// Clase para información de rutas
data class RouteInfo(
    val distance: String,
    val duration: String,
    val steps: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificadorRutaScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else LightBackground
    val cardColor = if (themeState.isDarkMode) CardGray else LightCard
    val textColor = if (themeState.isDarkMode) White else LightTextPrimary
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else LightTextSecondary

    // Estado para las estaciones seleccionadas
    var selectedOrigin by remember { mutableStateOf<Station?>(null) }
    var selectedDestination by remember { mutableStateOf<Station?>(null) }
    var showRoute by remember { mutableStateOf(false) }
    var routeInfo by remember { mutableStateOf<RouteInfo?>(null) }

    // Estado para filtros de líneas
    var selectedLines by remember { mutableStateOf(setOf<String>()) }
    
    // Estado para ubicación del usuario
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var useCurrentLocation by remember { mutableStateOf(true) } // Activado por defecto
    var showGoogleMap by remember { mutableStateOf(false) }
    
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
                        useCurrentLocation = true
                    }
                }
            } catch (e: SecurityException) {
                // Manejar error de permisos
            }
        }
    }
    
    // Verificar permisos al iniciar y obtener ubicación automáticamente
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
        } else {
            // Solicitar permisos automáticamente
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Posición de la cámara centrada en Lima
    val limaPosition = remember { MockStations.LIMA_CENTER }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(limaPosition, MockStations.LIMA_ZOOM)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.routes_title),
                                color = textColor
                            )
                        },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.common_back), tint = textColor)
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
        ) {
                    // Panel de controles compacto
                    CompactRoutePanel(
                        navController = navController,
                        selectedOrigin = selectedOrigin,
                        selectedDestination = selectedDestination,
                        selectedLines = selectedLines,
                        routeInfo = routeInfo,
                        onOriginSelected = { selectedOrigin = it },
                        onDestinationSelected = { selectedDestination = it },
                        onLineToggle = { line ->
                            val newSelectedLines = if (selectedLines.contains(line)) {
                                selectedLines - line
                            } else {
                                selectedLines + line
                            }
                            selectedLines = newSelectedLines

                            // Limpiar selecciones si las estaciones seleccionadas no están en las líneas filtradas
                            val filteredStations = getFilteredStations(newSelectedLines)
                            if (selectedOrigin != null && !filteredStations.contains(selectedOrigin)) {
                                selectedOrigin = null
                            }
                            if (selectedDestination != null && !filteredStations.contains(selectedDestination)) {
                                selectedDestination = null
                            }
                        },
                        onShowRoute = {
                            showRoute = true
                            // Simular cálculo de ruta con Directions API
                            if (selectedOrigin != null && selectedDestination != null) {
                                routeInfo = calculateRoute(selectedOrigin!!, selectedDestination!!)
                            }
                        },
                        onClearRoute = {
                            showRoute = false
                            selectedOrigin = null
                            selectedDestination = null
                            routeInfo = null
                        },
                        isDarkMode = themeState.isDarkMode,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor
                    )

            Spacer(modifier = Modifier.height(8.dp))

            // Mapa de Google Maps (previsualización)
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box {
                            // Mapa simplificado con fallback
                            SimpleMapView(
                                selectedOrigin = selectedOrigin,
                                selectedDestination = selectedDestination,
                                showRoute = showRoute,
                                selectedLines = selectedLines,
                                userLocation = userLocation,
                                hasLocationPermission = hasLocationPermission,
                                cardColor = cardColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                    
                    // Botón para agrandar el mapa
                    FloatingActionButton(
                        onClick = { 
                            // Navegar a pantalla de mapa completo
                            navController.navigate(Screen.Maps.route)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        containerColor = MetroOrange,
                        contentColor = White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Agrandar mapa"
                        )
                    }
                    
                    // Botón pequeño de ubicación (solo visible en mapa agrandado)
                    if (showGoogleMap) {
                        FloatingActionButton(
                            onClick = {
                                if (hasLocationPermission) {
                                    try {
                                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                            location?.let {
                                                userLocation = LatLng(it.latitude, it.longitude)
                                                useCurrentLocation = true
                                            }
                                        }
                                    } catch (e: SecurityException) {
                                        // Manejar error de permisos
                                    }
                                } else {
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp),
                            containerColor = MetroGreen
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = stringResource(R.string.routes_get_location),
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompactRoutePanel(
    navController: NavController,
    selectedOrigin: Station?,
    selectedDestination: Station?,
    selectedLines: Set<String>,
    routeInfo: RouteInfo?,
    onOriginSelected: (Station) -> Unit,
    onDestinationSelected: (Station) -> Unit,
    onLineToggle: (String) -> Unit,
    onShowRoute: () -> Unit,
    onClearRoute: () -> Unit,
    isDarkMode: Boolean,
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
            // Título del panel
            Text(
                text = "Planifica tu trayecto",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filtros de líneas
            LineFilterButtons(
                selectedLines = selectedLines,
                onLineToggle = onLineToggle,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selectores de origen y destino en una fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Selector de origen
                CompactStationSelector(
                    label = "Origen",
                    selectedStation = selectedOrigin,
                    onStationSelected = onOriginSelected,
                    stations = getFilteredStations(selectedLines),
                    icon = Icons.Default.FlightTakeoff,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )

                // Selector de destino
                CompactStationSelector(
                    label = "Destino",
                    selectedStation = selectedDestination,
                    onStationSelected = onDestinationSelected,
                    stations = getFilteredStations(selectedLines),
                    icon = Icons.Default.FlightLand,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información de la ruta compacta
            if (routeInfo != null) {
                CompactRouteInfo(
                    routeInfo = routeInfo,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botones de acción compactos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onShowRoute,
                    enabled = selectedOrigin != null && selectedDestination != null,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MetroOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Route, contentDescription = null, tint = if (isDarkMode) MetroOrange else LightIconRoute, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver Ruta")
                }


                OutlinedButton(
                    onClick = onClearRoute,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun CompactStationSelector(
    label: String,
    selectedStation: Station?,
    onStationSelected: (Station) -> Unit,
    stations: List<Station>,
    icon: ImageVector,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
            
            // Indicador de estaciones disponibles
            Text(
                text = "${stations.size} estaciones",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = secondaryTextColor
                )
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MetroOrange,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = selectedStation?.name ?: "Seleccionar",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (selectedStation != null) textColor else secondaryTextColor,
                        fontWeight = if (selectedStation != null) FontWeight.Bold else FontWeight.Normal
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Cerrar" else "Abrir",
                    tint = secondaryTextColor,
                    modifier = Modifier.size(16.dp)
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
                                                    "Línea 3" -> Color(0xFF2196F3)
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
                                tint = MetroOrange,
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

@Composable
fun CompactRouteInfo(
    routeInfo: RouteInfo,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
                modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MetroOrange.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Distancia",
                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                )
                Text(
                    text = routeInfo.distance,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }
            
            Column {
                Text(
                    text = "Tiempo",
                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                )
                Text(
                    text = routeInfo.duration,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }
            
            Column {
                Text(
                    text = "Pasos",
                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                )
                Text(
                    text = "${routeInfo.steps.size}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }
        }
    }
}


@Composable
fun SimpleMapView(
    selectedOrigin: Station?,
    selectedDestination: Station?,
    showRoute: Boolean,
    selectedLines: Set<String>,
    userLocation: LatLng?,
    hasLocationPermission: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    var showGoogleMap by remember { mutableStateOf(true) }
    
    if (showGoogleMap) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = if (userLocation != null) {
                        // Centrar entre usuario y estaciones si hay ruta
                        if (showRoute && selectedOrigin != null && selectedDestination != null) {
                            val centerLat = (userLocation.latitude + selectedOrigin.latitude + selectedDestination.latitude) / 3
                            val centerLng = (userLocation.longitude + selectedOrigin.longitude + selectedDestination.longitude) / 3
                            CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 12f)
                        } else {
                            CameraPosition.fromLatLngZoom(userLocation, 14f)
                        }
                    } else {
                        CameraPosition.fromLatLngZoom(MockStations.LIMA_CENTER, MockStations.LIMA_ZOOM)
                    }
                },
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = false,
                    myLocationButtonEnabled = hasLocationPermission
                )
            ) {
                // Marcador de ubicación del usuario
                userLocation?.let { userLoc ->
                    Marker(
                        state = MarkerState(position = userLoc),
                        title = stringResource(R.string.routes_your_location),
                        snippet = stringResource(R.string.routes_current_position)
                    )
                }

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

                // Ruta desde ubicación del usuario
                if (showRoute && userLocation != null && selectedOrigin != null) {
                    Polyline(
                        points = listOf(userLocation, LatLng(selectedOrigin.latitude, selectedOrigin.longitude)),
                        color = MetroGreen,
                        width = 6f
                    )
                }

                // Líneas del Metro
                MetroLinesOverlay(selectedLines = selectedLines)
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
fun MetroLinesOverlay(selectedLines: Set<String>) {
    // Si no hay líneas seleccionadas, mostrar todas
    val linesToShow = if (selectedLines.isEmpty()) {
        setOf("Línea 1", "Línea 2", "Línea 3")
    } else {
        selectedLines
    }

    // Línea 1 - Naranja
    if (linesToShow.contains("Línea 1")) {
        val linea1Stations = MockStations.getStationsByLine("Línea 1")
        if (linea1Stations.isNotEmpty()) {
            val linea1Points = linea1Stations.map { LatLng(it.latitude, it.longitude) }
            Polyline(
                points = linea1Points,
                color = MetroOrange,
                width = 6f
            )
        }
    }

    // Línea 2 - Verde
    if (linesToShow.contains("Línea 2")) {
        val linea2Stations = MockStations.getStationsByLine("Línea 2")
        if (linea2Stations.isNotEmpty()) {
            val linea2Points = linea2Stations.map { LatLng(it.latitude, it.longitude) }
            Polyline(
                points = linea2Points,
                color = MetroGreen,
                width = 6f
            )
        }
    }

    // Línea 3 - Azul
    if (linesToShow.contains("Línea 3")) {
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
}

@Composable
fun LineFilterButtons(
    selectedLines: Set<String>,
    onLineToggle: (String) -> Unit,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = "Filtrar Líneas del Metro",
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
                        Text("Línea 1")
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
                        Text("Línea 2")
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
                        Text("Línea 3")
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
        
        // Botón "Próximamente" para futuras líneas
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = { /* Futuras líneas */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = secondaryTextColor
            )
        ) {
            Icon(
                imageVector = Icons.Default.Construction,
                contentDescription = "Próximamente",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Más líneas próximamente")
        }
    }
}

// Función para obtener estaciones filtradas por líneas seleccionadas
fun getFilteredStations(selectedLines: Set<String>): List<Station> {
    return if (selectedLines.isEmpty()) {
        // Si no hay líneas seleccionadas, mostrar todas las estaciones
        MockStations.stations
    } else {
        // Filtrar estaciones por las líneas seleccionadas
        MockStations.stations.filter { station ->
            selectedLines.contains(station.line)
        }
    }
}

// Función para calcular ruta (simula Directions API)
fun calculateRoute(origin: Station, destination: Station): RouteInfo {
    // Simulación de cálculo de distancia y tiempo
    val distance = "${(1..15).random()} km"
    val duration = "${(5..45).random()} min"
    val steps = listOf(
        "1. Dirígete a ${origin.name}",
        "2. Toma el Metro hacia ${destination.name}",
        "3. Baja en ${destination.name}",
        "4. Has llegado a tu destino"
    )
    return RouteInfo(distance, duration, steps)
}

@Composable
fun CurrentLocationSection(
    userLocation: LatLng?,
    hasLocationPermission: Boolean,
    useCurrentLocation: Boolean,
    onGetLocation: () -> Unit,
    onUseCurrentLocation: (Boolean) -> Unit,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MetroGreen.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = stringResource(R.string.routes_your_location),
                    tint = MetroGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.routes_current_location),
                    color = textColor,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (userLocation != null) {
                // Ubicación obtenida
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.routes_location_obtained),
                        tint = MetroGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.routes_location_obtained),
                        color = MetroGreen,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Switch para usar ubicación actual
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.routes_use_current_location),
                        color = textColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = useCurrentLocation,
                        onCheckedChange = onUseCurrentLocation,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = MetroGreen,
                            uncheckedThumbColor = secondaryTextColor,
                            uncheckedTrackColor = secondaryTextColor.copy(alpha = 0.3f)
                        )
                    )
                }
            } else {
                // No hay ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOff,
                        contentDescription = stringResource(R.string.routes_no_location),
                        tint = secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.routes_no_location),
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para obtener ubicación
                Button(
                    onClick = onGetLocation,
                    colors = ButtonDefaults.buttonColors(containerColor = MetroGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = stringResource(R.string.routes_get_location),
                        tint = White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.routes_get_location),
                        color = White,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
