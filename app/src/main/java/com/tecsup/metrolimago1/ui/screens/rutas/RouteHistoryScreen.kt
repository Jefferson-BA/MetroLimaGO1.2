package com.tecsup.metrolimago1.ui.screens.rutas

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.RouteHistory
import com.tecsup.metrolimago1.data.local.database.AppDatabase
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteHistoryScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    val routeHistory by database.routeHistoryDao().getRecent(20).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historial de Rutas",
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
                actions = {
                    if (routeHistory.isNotEmpty()) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                database.routeHistoryDao().deleteAll()
                            }
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Limpiar historial",
                                tint = textColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
            )
        },
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = "")
        }
    ) { paddingValues ->
        if (routeHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Sin historial",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay historial de rutas",
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Las rutas que consultes aparecerán aquí",
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(routeHistory) { route ->
                    RouteHistoryItemCard(
                        route = route,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        onDeleteClick = {
                            coroutineScope.launch {
                                database.routeHistoryDao().delete(route)
                            }
                        },
                        onClick = {
                            // Navegar de vuelta al planificador con esta ruta
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RouteHistoryItemCard(
    route: RouteHistory,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Route,
                    contentDescription = "Ruta",
                    tint = MetroOrange,
                    modifier = Modifier.size(32.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${route.originName} → ${route.destinationName}",
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Tiempo",
                            tint = secondaryTextColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = route.duration,
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Straighten,
                            contentDescription = "Distancia",
                            tint = secondaryTextColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = route.distance,
                            color = secondaryTextColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFE53E3E)
                )
            }
        }
    }
}

