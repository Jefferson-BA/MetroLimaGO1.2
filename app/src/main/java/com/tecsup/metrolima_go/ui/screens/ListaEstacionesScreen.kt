package com.tecsup.metrolima_go.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.metrolima_go.data.Estacion
import com.tecsup.metrolima_go.data.EstacionesMock
import com.tecsup.metrolima_go.ui.components.EstacionCard
import com.tecsup.metrolima_go.ui.theme.MetroLima_GoTheme
import com.tecsup.metrolima_go.ui.theme.MetroLine1Green
import com.tecsup.metrolima_go.ui.theme.MetroLine2Orange

@Composable
fun ListaEstacionesScreen(
    onEstacionClick: (Estacion) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var busqueda by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todas") }
    
    val filtros = listOf("Todas", "Línea 1", "Línea 2", "Metropolitano")
    
    val estacionesFiltradas = EstacionesMock.estaciones.filter { estacion ->
        val coincideBusqueda = estacion.nombre.contains(busqueda, ignoreCase = true) ||
                estacion.subtexto.contains(busqueda, ignoreCase = true)
        
        val coincideFiltro = when (filtroSeleccionado) {
            "Todas" -> true
            "Línea 1" -> estacion.linea == "Línea 1"
            "Línea 2" -> estacion.linea == "Línea 2"
            "Metropolitano" -> estacion.linea == "Metropolitano"
            else -> true
        }
        
        coincideBusqueda && coincideFiltro
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar estación...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtros.forEach { filtro ->
                val isSelected = filtro == filtroSeleccionado
                val color = when (filtro) {
                    "Línea 1" -> MetroLine1Green
                    "Línea 2" -> MetroLine2Orange
                    else -> MaterialTheme.colorScheme.primary
                }
                
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filtroSeleccionado = filtro },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) color else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 4.dp else 2.dp
                    )
                ) {
                    Text(
                        text = filtro,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de estaciones
        if (estacionesFiltradas.isNotEmpty()) {
            Text(
                text = "• ${filtroSeleccionado}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estacionesFiltradas) { estacion ->
                    EstacionCard(
                        nombre = estacion.nombre,
                        subtexto = estacion.subtexto,
                        tiempo = estacion.tiempo,
                        onClick = { onEstacionClick(estacion) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron estaciones",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaEstacionesScreenPreview() {
    MetroLima_GoTheme {
        ListaEstacionesScreen()
    }
}
