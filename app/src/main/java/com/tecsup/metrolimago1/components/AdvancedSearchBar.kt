package com.tecsup.metrolimago1.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.tecsup.metrolimago1.data.local.MockStations
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.ui.theme.*

@Composable
fun AdvancedSearchBar(
    onSearchClick: (String) -> Unit,
    onStationSelect: (Station) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }
    
    // Filtrar estaciones basado en la búsqueda
    val suggestions = remember(searchQuery) {
        if (searchQuery.length >= 2) {
            MockStations.stations.filter { station ->
                station.name.contains(searchQuery, ignoreCase = true) ||
                station.address.contains(searchQuery, ignoreCase = true) ||
                station.description.contains(searchQuery, ignoreCase = true)
            }.take(5) // Mostrar máximo 5 sugerencias
        } else {
            emptyList()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
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
                    tint = secondaryTextColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        showSuggestions = it.length >= 2
                    },
                    placeholder = {
                        Text(
                            text = "¿A dónde vas?",
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
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true
                )
                
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { 
                            onSearchClick(searchQuery)
                            showSuggestions = false
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Buscar",
                            tint = MetroOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
        
        // Sugerencias de autocompletado
        if (showSuggestions && suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .zIndex(1f),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(suggestions) { station ->
                        SuggestionItem(
                            station = station,
                            onClick = {
                                onStationSelect(station)
                                searchQuery = station.name
                                showSuggestions = false
                            },
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(
    station: Station,
    onClick: () -> Unit,
    textColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Ubicación",
            tint = MetroOrange,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = station.name,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = station.address,
                color = secondaryTextColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Text(
            text = station.line,
            color = secondaryTextColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
