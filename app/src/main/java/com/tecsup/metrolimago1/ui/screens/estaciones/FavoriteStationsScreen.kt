package com.tecsup.metrolimago1.ui.screens.estaciones

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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.FavoriteStation
import com.tecsup.metrolimago1.data.local.database.AppDatabase
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteStationsScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    // Base de datos
    val database = remember { AppDatabase.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // Observar cambios en favoritos
    val favoriteStations by remember {
        database.favoriteStationDao().getAll()
    }.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Estaciones Favoritas",
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
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Estaciones.route)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            if (favoriteStations.isEmpty()) {
                // Mostrar mensaje cuando no hay favoritos
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Sin favoritos",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay estaciones favoritas",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Agrega estaciones a favoritos desde la pantalla de detalles",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                // Mostrar lista de estaciones favoritas
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteStations) { favorite ->
                        FavoriteStationCard(
                            favorite = favorite,
                            cardColor = cardColor,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            onCardClick = {
                                navController.navigate("estaciones/${favorite.id}")
                            },
                            onDeleteClick = {
                                // Eliminar de favoritos
                                coroutineScope.launch {
                                    database.favoriteStationDao().delete(favorite.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteStationCard(
    favorite: FavoriteStation,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de estación
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Estación",
                tint = MetroOrange,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la estación
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = favorite.name,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = favorite.line,
                    color = MetroOrange,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = favorite.address,
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de eliminar de favoritos
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = Color(0xFFE53E3E)
                )
            }
        }
    }
}
