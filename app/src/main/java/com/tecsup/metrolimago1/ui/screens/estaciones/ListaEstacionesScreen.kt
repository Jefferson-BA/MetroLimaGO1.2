package com.tecsup.metrolimago1.ui.screens.estaciones

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import com.tecsup.metrolimago1.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionesScreen(navController: NavController) {
    val themeState = LocalThemeState.current

    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    var query by remember { mutableStateOf("") }
    var selectedLine by remember { mutableStateOf("Linea 1") }

    // 🔹 Fondo gradiente dinámico
    GradientBackground(isDarkMode = themeState.isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.stations_title),
                            color = textColor,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent // permite ver el gradiente
                    )
                )
            },
            bottomBar = {
                GlobalBottomNavBar(
                    navController = navController,
                    currentRoute = Screen.Estaciones.route
                )
            },
            containerColor = Color.Transparent, // importante para el fondo
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    )
                    .padding(vertical = 8.dp)
            ) {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                LineFilterButtons(
                    selectedLine = selectedLine,
                    onLineSelected = { selectedLine = it },
                    cardColor = cardColor,
                    textColor = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(secondaryTextColor, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedLine,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val filtered = MockStations.stations.filter { station ->
                    val lineMatch = when (selectedLine) {
                        "Linea 1" -> station.line == "Línea 1"
                        "Linea 2" -> station.line == "Línea 2"
                        "Metropolitano" -> station.line == "Línea 3" || station.line == "Línea 4"
                        else -> true
                    }

                    val searchMatch = if (query.isBlank()) {
                        true
                    } else {
                        station.name.contains(query, ignoreCase = true) ||
                                station.address.contains(query, ignoreCase = true) ||
                                station.description.contains(query, ignoreCase = true) ||
                                station.id.contains(query, ignoreCase = true)
                    }

                    lineMatch && searchMatch
                }

                if (filtered.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "Sin resultados",
                            tint = secondaryTextColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (query.isNotEmpty()) "No se encontraron estaciones" else "No hay estaciones en esta línea",
                            color = textColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (query.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.common_try_other_terms),
                                color = secondaryTextColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filtered) { station ->
                            StationCard(
                                station = station,
                                onClick = {
                                    navController.navigate(
                                        Screen.EstacionDetail.createRoute(station.id)
                                    )
                                },
                                cardColor = cardColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(24.dp)
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
                tint = secondaryTextColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.stations_search_placeholder),
                        color = secondaryTextColor
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )

            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LineFilterButtons(
    selectedLine: String,
    onLineSelected: (String) -> Unit,
    cardColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val lines = listOf("Linea 1", "Linea 2", "Metropolitano")

        lines.forEach { line ->
            val isSelected = selectedLine == line
            Card(
                onClick = { onLineSelected(line) },
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) cardColor.copy(alpha = 0.8f) else cardColor
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = line,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun StationCard(
    station: Station,
    onClick: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                tint = secondaryTextColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = station.address,
                        color = secondaryTextColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Dirección",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MetroGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${(3..15).random()} min",
                    color = White,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "💡 ListaEstacionesScreen - Claro")
@Composable
fun ListaEstacionesScreenPreviewLight() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(false) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ListaEstacionesScreen(navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "🌙 ListaEstacionesScreen - Oscuro")
@Composable
fun ListaEstacionesScreenPreviewDark() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(true) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ListaEstacionesScreen(navController)
        }
    }
}
