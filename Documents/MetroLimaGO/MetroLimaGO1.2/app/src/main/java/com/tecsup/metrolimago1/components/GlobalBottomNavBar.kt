package com.tecsup.metrolimago1.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun GlobalBottomNavBar(navController: NavController, currentRoute: String) {
    // Animación para el efecto liquid glass
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_glass")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        // Fondo con efecto liquid glass
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.25f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f * shimmerOffset, 1000f * shimmerOffset)
                    )
                )
                .graphicsLayer {
                    alpha = 0.9f
                    scaleX = pulseScale
                    scaleY = pulseScale
                }
        )
        
        // Contenido de la barra de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Home
            LiquidGlassButton(
                isSelected = currentRoute == Screen.Home.route,
                onClick = { navController.navigate(Screen.Home.route) },
                icon = Icons.Default.Home,
                contentDescription = "Inicio"
            )

            // Botón Estaciones
            LiquidGlassButton(
                isSelected = currentRoute == Screen.Estaciones.route,
                onClick = { navController.navigate(Screen.Estaciones.route) },
                icon = Icons.Default.LocationOn,
                contentDescription = "Ubicación"
            )

            // Botón Rutas
            LiquidGlassButton(
                isSelected = currentRoute == Screen.Rutas.route,
                onClick = { navController.navigate(Screen.Rutas.route) },
                icon = Icons.Default.Route,
                contentDescription = "Rutas"
            )

            // Botón Vivo
            LiquidGlassButton(
                isSelected = currentRoute == Screen.Vivo.route,
                onClick = { navController.navigate(Screen.Vivo.route) },
                icon = Icons.Default.Wifi,
                contentDescription = "En vivo"
            )

            // Botón Configuración
            LiquidGlassButton(
                isSelected = currentRoute == Screen.Configuracion.route,
                onClick = { navController.navigate(Screen.Configuracion.route) },
                icon = Icons.Default.Settings,
                contentDescription = "Configuración"
            )
        }
    }
}

@Composable
fun LiquidGlassButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")
    
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = if (isSelected) 1.1f else 1f,
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )
    
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = buttonScale
                scaleY = buttonScale
            }
    ) {
        if (isSelected) {
            // Fondo con efecto liquid glass para botón seleccionado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MetroOrange.copy(alpha = 0.8f + glowIntensity * 0.2f),
                                MetroOrange.copy(alpha = 0.6f),
                                MetroOrange.copy(alpha = 0.4f)
                            ),
                            radius = 30f
                        )
                    )
            )
            
            // Efecto de brillo interno
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.White.copy(alpha = 0.0f)
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(48f, 48f)
                        )
                    )
            )
        }
        
        // Botón clickeable
        IconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) White else LightGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalBottomNavBarPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E1E1E),
                            Color(0xFF2D2D2D),
                            Color(0xFF1E1E1E)
                        )
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            GlobalBottomNavBar(
                navController = navController,
                currentRoute = "home"
            )
        }
    }
}