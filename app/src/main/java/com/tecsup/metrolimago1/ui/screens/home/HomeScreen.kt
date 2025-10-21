package com.tecsup.metrolimago1.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Título principal
            Text(
                text = "MetroLima GO",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = White
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Barra de búsqueda
            SearchBar(
                onSearchClick = { /* TODO: Implementar búsqueda */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección Próximas Llegadas
            NextArrivalsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Notificaciones
            NotificationsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Sección IA
            AISection(navController)
        }
    }
}

@Composable
fun SearchBar(onSearchClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = LightGray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "¿A dónde vas?",
                color = LightGray,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun NextArrivalsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardGray),
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
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Reloj",
                    tint = MetroOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Próximas Llegadas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                )
            }
            
            Text(
                text = "Información en tiempo real",
                style = MaterialTheme.typography.bodySmall.copy(color = LightGray),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de llegadas
            ArrivalItem(
                stationName = "Villa El Salvador",
                direction = "hacia San Martin",
                time = "3 min"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ArrivalItem(
                stationName = "Angamos",
                direction = "hacia San Martin", 
                time = "5 min"
            )
        }
    }
}

@Composable
fun ArrivalItem(stationName: String, direction: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Línea naranja vertical
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(MetroOrange, RoundedCornerShape(2.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stationName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Dirección",
                        tint = LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = direction,
                        style = MaterialTheme.typography.bodySmall.copy(color = LightGray)
                    )
                }
            }
            
            // Indicador de tiempo
            Card(
                colors = CardDefaults.cardColors(containerColor = MetroGreen),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = time,
                    color = White,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardGray),
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
                Text(
                    text = "Notificaciones",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MetroOrange
                    )
                )
                Text(
                    text = "Ver más",
                    style = MaterialTheme.typography.bodySmall.copy(color = LightGray)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            NotificationItem(
                title = "Servicio Normal",
                description = "Todas las líneas operando con normalidad"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            NotificationItem(
                title = "Mantenimiento Programado",
                description = "Línea 1: Horario reducido el domingo 20 de octubre"
            )
        }
    }
}

@Composable
fun NotificationItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = White
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(color = LightGray)
        )
    }
}

@Composable
fun AISection(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pregúntale a la IA !",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MetroOrange
                    )
                )
                Text(
                    text = "Usar un chat de ayuda o asistente.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = White),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = { /* TODO: Implementar chat IA */ },
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Iniciar Conversación",
                        color = DarkGray,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            
            // Icono de robot (usando emoji como placeholder)
            Text(
                text = "🤖",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = CardGray,
        contentColor = White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                selectedTextColor = White,
                indicatorColor = MetroOrange
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Ubicación") },
            label = { Text("Ubicación") },
            selected = false,
            onClick = { navController.navigate(Screen.Estaciones.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                selectedTextColor = White,
                unselectedIconColor = LightGray,
                unselectedTextColor = LightGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Directions, contentDescription = "Rutas") },
            label = { Text("Rutas") },
            selected = false,
            onClick = { navController.navigate(Screen.Rutas.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                selectedTextColor = White,
                unselectedIconColor = LightGray,
                unselectedTextColor = LightGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.SignalCellular4Bar, contentDescription = "Conexión") },
            label = { Text("Conexión") },
            selected = false,
            onClick = { /* TODO: Implementar pantalla de conexión */ },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                selectedTextColor = White,
                unselectedIconColor = LightGray,
                unselectedTextColor = LightGray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            label = { Text("Configuración") },
            selected = false,
            onClick = { navController.navigate(Screen.Configuracion.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                selectedTextColor = White,
                unselectedIconColor = LightGray,
                unselectedTextColor = LightGray
            )
        )
    }
}