package com.tecsup.metrolimago1.ui.screens.maps

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
import com.tecsup.metrolimago1.utils.TranslationUtils
import com.tecsup.metrolimago1.utils.LocaleUtils

// Clase para información de rutas
data class RouteInfo(
    val distance: String,
    val duration: String,
    val steps: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else LightBackground
    val cardColor = if (themeState.isDarkMode) CardGray else LightCard
    val textColor = if (themeState.isDarkMode) White else LightTextPrimary
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else LightTextSecondary

    // Estado para filtros de líneas
    var selectedLines by remember { mutableStateOf(setOf<String>()) }
    var showGoogleMap by remember { mutableStateOf(true) }
    
    // Estado para ubicación del usuario
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var useCurrentLocation by remember { mutableStateOf(true) } // Activado por defecto
    
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
    var showNearbyPlaces by remember { mutableStateOf(false) }
    var routeInfo by remember { mutableStateOf<RouteInfo?>(null) }

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
                        text = stringResource(R.string.map_full_view_title),
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.common_back), tint = textColor)
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
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Maps.route)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            if (showGoogleMap) {
                // Mapa de Google Maps completo
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = if (userLocation != null) {
                            CameraPosition.fromLatLngZoom(userLocation!!, 15f)
                        } else {
                            CameraPosition.fromLatLngZoom(limaPosition, MockStations.LIMA_ZOOM)
                        }
                    },
                    properties = MapProperties(
                        isMyLocationEnabled = hasLocationPermission,
                        mapType = MapType.NORMAL,
                        isTrafficEnabled = false
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        compassEnabled = true,
                        myLocationButtonEnabled = hasLocationPermission
                    )
                ) {
                    // Marcador de ubicación del usuario
                    userLocation?.let { userLoc ->
                        Marker(
                            state = MarkerState(position = userLoc),
                            title = stringResource(R.string.maps_your_location),
                            snippet = stringResource(R.string.maps_current_position)
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

                    // Líneas del Metro
                    MetroLinesOverlay(selectedLines = selectedLines)
                }
                
                // Botones de control del mapa
                MapControlButtons(
                    showGoogleMap = showGoogleMap,
                    userLocation = userLocation,
                    hasLocationPermission = hasLocationPermission,
                    onGetLocation = {
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
                    },
                    onCenterLocation = {
                        // Centrar en ubicación del usuario
                        if (userLocation != null) {
                            // La cámara se centrará automáticamente en userLocation
                        }
                    }
                )
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
                            contentDescription = "Mapa",
                            tint = MetroOrange,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Mapa del Metro de Lima",
                            color = textColor,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Vista completa del sistema de Metro",
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodyLarge
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
                            Text(TranslationUtils.getText(context, "activate_map"))
                        }
                    }
                }
            }
            
            // Panel de filtros flotante (optimizado)
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Filtrar Líneas",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Botones de filtro de líneas
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            onClick = { 
                                selectedLines = if (selectedLines.contains("Línea 1")) {
                                    selectedLines - "Línea 1"
                                } else {
                                    selectedLines + "Línea 1"
                                }
                            },
                            label = { Text("L1") },
                            selected = selectedLines.contains("Línea 1"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MetroOrange,
                                selectedLabelColor = White
                            )
                        )
                        
                        FilterChip(
                            onClick = { 
                                selectedLines = if (selectedLines.contains("Línea 2")) {
                                    selectedLines - "Línea 2"
                                } else {
                                    selectedLines + "Línea 2"
                                }
                            },
                            label = { Text("L2") },
                            selected = selectedLines.contains("Línea 2"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MetroGreen,
                                selectedLabelColor = White
                            )
                        )
                        
                        FilterChip(
                            onClick = { 
                                selectedLines = if (selectedLines.contains("Metropolitano")) {
                                    selectedLines - "Metropolitano"
                                } else {
                                    selectedLines + "Metropolitano"
                                }
                            },
                            label = { Text("Metro") },
                            selected = selectedLines.contains("Metropolitano"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = White
                            )
                        )
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
    selectedLine: String?,
    showNearbyPlaces: Boolean,
    routeInfo: RouteInfo?,
    onOriginSelected: (Station) -> Unit,
    onDestinationSelected: (Station) -> Unit,
    onLineSelected: (String?) -> Unit,
    onShowRoute: () -> Unit,
    onShowNearbyPlaces: () -> Unit,
    onClearRoute: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
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
            // Título y filtros en una fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Planifica tu trayecto",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
                
                // Botón de IA integrado
                Button(
                    onClick = { navController.navigate(Screen.Chat.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = MetroOrange),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "IA",
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filtros por línea compactos
            LineFilterSection(
                selectedLine = selectedLine,
                onLineSelected = onLineSelected,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                context = context
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
                    stations = if (selectedLine != null) MockStations.getStationsByLine(selectedLine) else MockStations.stations,
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
                    stations = if (selectedLine != null) MockStations.getStationsByLine(selectedLine) else MockStations.stations,
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
                    secondaryTextColor = secondaryTextColor,
                    context = context
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
                    Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(TranslationUtils.getText(context, "see_route"))
                }

                OutlinedButton(
                    onClick = onShowNearbyPlaces,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (showNearbyPlaces) MetroOrange else textColor
                    )
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(TranslationUtils.getText(context, "places"))
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
fun MetroLinesOverlay(selectedLines: Set<String>) {
    // Si no hay líneas seleccionadas, mostrar todas
    val linesToShow = if (selectedLines.isEmpty()) {
        setOf("Línea 1", "Línea 2", "Metropolitano")
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

    // Metropolitano - Azul
    if (linesToShow.contains("Metropolitano")) {
        val linea3Stations = MockStations.getStationsByLine("Metropolitano")
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
fun LineFilterSection(
    selectedLine: String?,
    onLineSelected: (String?) -> Unit,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Column {
        Text(
            text = "Filtrar por Línea",
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
            // Botón "Todas"
            FilterChip(
                onClick = { onLineSelected(null) },
                label = { Text(TranslationUtils.getText(context, "all")) },
                selected = selectedLine == null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MetroOrange,
                    selectedLabelColor = White
                )
            )
            
            // Botón "Línea 1"
            FilterChip(
                onClick = { onLineSelected("Línea 1") },
                label = { Text("Línea 1") },
                selected = selectedLine == "Línea 1",
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MetroOrange,
                    selectedLabelColor = White
                )
            )
            
            // Botón "Línea 2"
            FilterChip(
                onClick = { onLineSelected("Línea 2") },
                label = { Text("Línea 2") },
                selected = selectedLine == "Línea 2",
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MetroGreen,
                    selectedLabelColor = White
                )
            )
            
            // Botón "Metropolitano"
            FilterChip(
                onClick = { onLineSelected("Metropolitano") },
                label = { Text("Metropolitano") },
                selected = selectedLine == "Metropolitano",
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2196F3),
                    selectedLabelColor = White
                )
            )
        }
    }
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = TranslationUtils.getText(context, "distance"),
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                    Text(
                        text = routeInfo.distance,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }
                
                Column {
                    Text(
                        text = TranslationUtils.getText(context, "time"),
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                    Text(
                        text = routeInfo.duration,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Instrucciones:",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
            
            routeInfo.steps.forEach { step ->
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

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
            modifier = Modifier.fillMaxWidth()
        ) {
            stations.forEach { station ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = station.name,
                                color = textColor,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = station.line,
                                color = secondaryTextColor,
                                style = MaterialTheme.typography.bodySmall
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
    secondaryTextColor: Color,
    context: android.content.Context
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
                        text = TranslationUtils.getText(context, "steps"),
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
fun MapControlButtons(
    showGoogleMap: Boolean,
    userLocation: LatLng?,
    hasLocationPermission: Boolean,
    onGetLocation: () -> Unit,
    onCenterLocation: () -> Unit
) {
    if (showGoogleMap) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón para obtener ubicación
            FloatingActionButton(
                onClick = onGetLocation,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                containerColor = MetroGreen
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = stringResource(R.string.maps_get_location),
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Botón para centrar en ubicación actual
            if (userLocation != null) {
                FloatingActionButton(
                    onClick = onCenterLocation,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    containerColor = MetroOrange
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = stringResource(R.string.maps_center_location),
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Función auxiliar para simular búsqueda de lugares
// En producción, esto debería usar la API de Google Places
fun simulatePlaceSearch(query: String): List<Pair<String, LatLng>> {
    val places = listOf(
        "Mall Plaza San Miguel" to LatLng(-12.0784, -77.0932),
        "Larcomar, Miraflores" to LatLng(-12.1325, -77.0230),
        "Plaza Mayor de Lima" to LatLng(-12.0464, -77.0428),
        "Malecón de Miraflores" to LatLng(-12.1187, -77.0362),
        "Museo de Arte de Lima" to LatLng(-12.0702, -77.0373),
        "Kennedy Park" to LatLng(-12.1256, -77.0239),
        "San Borja" to LatLng(-12.1028, -76.9542),
        "Centro de Lima" to LatLng(-12.0464, -77.0428),
        "Callao" to LatLng(-12.0567, -77.1184),
        "San Isidro" to LatLng(-12.0971, -77.0304)
    )
    
    return places.filter { 
        it.first.contains(query, ignoreCase = true) 
    }.take(5)
}
