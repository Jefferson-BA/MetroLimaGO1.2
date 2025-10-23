package com.tecsup.metrolima_go.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.metrolima_go.data.Estacion
import com.tecsup.metrolima_go.data.EstadoEstacion
import com.tecsup.metrolima_go.ui.theme.MetroLima_GoTheme
import com.tecsup.metrolima_go.ui.theme.MetroError
import com.tecsup.metrolima_go.ui.theme.MetroLine1Green
import com.tecsup.metrolima_go.ui.theme.MetroLine2Orange
import com.tecsup.metrolima_go.ui.theme.MetroSuccess
import com.tecsup.metrolima_go.ui.theme.MetroWarning

@Composable
fun DetalleEstacionScreen(
    estacion: Estacion,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header con nombre y estado
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = when (estacion.linea) {
                                "Línea 1" -> MetroLine1Green
                                "Línea 2" -> MetroLine2Orange
                                else -> MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = estacion.nombre,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = estacion.subtexto,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        // Estado de la estación
                        val estadoColor = when (estacion.estado) {
                            EstadoEstacion.OPERANDO -> MetroSuccess
                            EstadoEstacion.MANTENIMIENTO -> MetroWarning
                            EstadoEstacion.CERRADO -> MetroError
                        }
                        
                        val estadoText = when (estacion.estado) {
                            EstadoEstacion.OPERANDO -> "Operando"
                            EstadoEstacion.MANTENIMIENTO -> "Mantenimiento"
                            EstadoEstacion.CERRADO -> "Cerrado"
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(estadoColor)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = estadoText,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        
        // Información de llegada
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Próximas Llegadas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = when (estacion.linea) {
                                "Línea 1" -> MetroLine1Green
                                "Línea 2" -> MetroLine2Orange
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Tren llegando en ${estacion.tiempo}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        
        // Información de la estación
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información de la Estación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Línea
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        title = "Línea",
                        value = estacion.linea,
                        iconColor = when (estacion.linea) {
                            "Línea 1" -> MetroLine1Green
                            "Línea 2" -> MetroLine2Orange
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                    
                    // Dirección
                    estacion.direccion?.let { direccion ->
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            title = "Dirección",
                            value = direccion
                        )
                    }
                    
                    // Horario
                    estacion.horario?.let { horario ->
                        InfoRow(
                            icon = Icons.Default.AccessTime,
                            title = "Horario",
                            value = horario
                        )
                    }
                }
            }
        }
        
        // Servicios disponibles
        if (estacion.servicios.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Servicios Disponibles",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(estacion.servicios) { servicio ->
                                ServiceChip(servicio = servicio)
                            }
                        }
                    }
                }
            }
        }
        
        // Alertas y avisos
        if (estacion.estado != EstadoEstacion.OPERANDO) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (estacion.estado) {
                            EstadoEstacion.MANTENIMIENTO -> MetroWarning.copy(alpha = 0.1f)
                            EstadoEstacion.CERRADO -> MetroError.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Alertas y Avisos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = when (estacion.estado) {
                                EstadoEstacion.MANTENIMIENTO -> MetroWarning
                                EstadoEstacion.CERRADO -> MetroError
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = when (estacion.estado) {
                                EstadoEstacion.MANTENIMIENTO -> "Mantenimiento Programado: ${estacion.nombre} - ${estacion.horario ?: "Horario reducido"}"
                                EstadoEstacion.CERRADO -> "Estación cerrada temporalmente. ${estacion.horario ?: "Reapertura programada"}"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ServiceChip(
    servicio: String,
    modifier: Modifier = Modifier
) {
    val icon = when (servicio) {
        "WiFi" -> Icons.Default.Wifi
        "Ascensores" -> Icons.Default.AccessTime
        "Escaleras eléctricas" -> Icons.Default.AccessTime
        "Boletería" -> Icons.Default.Phone
        "Cajero automático" -> Icons.Default.Phone
        else -> Icons.Default.Phone
    }
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = servicio,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            Text(
                text = servicio,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleEstacionScreenPreview() {
    MetroLima_GoTheme {
        DetalleEstacionScreen(
            estacion = com.tecsup.metrolima_go.data.EstacionesMock.estaciones[0]
        )
    }
}
