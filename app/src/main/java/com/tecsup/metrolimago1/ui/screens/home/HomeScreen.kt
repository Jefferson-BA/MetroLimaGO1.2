package com.tecsup.metrolimago1.ui.screens.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.domain.services.RouteCalculationService
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.utils.TranslationUtils
import com.tecsup.metrolimago1.utils.LocaleUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    val cardColor = if (themeState.isDarkMode) CardGray else LightCard
    val textColor = if (themeState.isDarkMode) White else LightTextPrimary
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else LightTextSecondary

    GradientBackground(isDarkMode = themeState.isDarkMode) {
        Scaffold(
            bottomBar = {
                GlobalBottomNavBar(
                    navController = navController,
                    currentRoute = Screen.Home.route
                )
            },
            containerColor = Color.Transparent, // importante para que se vea el gradiente
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    )
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = "MetroLima GO",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                SearchBar(
                    onSearchClick = { _ -> navController.navigate(Screen.Estaciones.route) },
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    context = context
                )

                Spacer(modifier = Modifier.height(24.dp))

                NextArrivalsSection(
                    isDarkMode = themeState.isDarkMode,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    context = context
                )

                Spacer(modifier = Modifier.height(16.dp))

                NotificationsSection(
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    context = context
                )

                Spacer(modifier = Modifier.height(16.dp))

                AISection(
                    navController = navController,
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                RouteTestSection(
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    onCardClick = {
                        println("Card de rutas clickeada!")
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearchClick: (String) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    var searchQuery by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = TranslationUtils.getText(context, "search"),
                tint = secondaryTextColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
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
                    onClick = { onSearchClick(searchQuery) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = TranslationUtils.getText(context, "search"),
                        tint = MetroOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NextArrivalsSection(
    isDarkMode: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
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
                    contentDescription = TranslationUtils.getText(context, "clock"),
                    tint = if (isDarkMode) MetroOrange else LightIconTime,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Próximas Llegadas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }

            Text(
                text = "Información en tiempo real",
                style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ArrivalItem(
                stationName = "Villa El Salvador",
                direction = "hacia San Martin",
                time = "3 min",
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            ArrivalItem(
                stationName = "Angamos",
                direction = "hacia San Martin",
                time = "5 min",
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
        }
    }
}

@Composable
fun ArrivalItem(
    stationName: String,
    direction: String,
    time: String,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (textColor == White) DarkGray else Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        color = textColor
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Dirección",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = direction,
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MetroGreen),
                shape = RoundedCornerShape(10.dp)
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
fun NotificationsSection(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
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
                    text = TranslationUtils.getText(context, "notifications"),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MetroOrange
                    )
                )
                Text(
                    text = "Ver más",
                    style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            NotificationItem(
                title = "Servicio Normal",
                description = "Todas las líneas operando con normalidad",
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            NotificationItem(
                title = "Mantenimiento Programado",
                description = "Línea 1: Horario reducido el domingo 20 de octubre",
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    description: String,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
        )
    }
}

@Composable
fun AISection(
    navController: NavController,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
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
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                val infiniteTransition = rememberInfiniteTransition(label = "ledGlow")

                val glowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "glowAnim"
                )

                val glowingColor = MetroBrightOrange.copy(alpha = glowAlpha)

                Button(
                    onClick = { navController.navigate(Screen.Chat.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = glowingColor),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Iniciar Conversación",
                        color = White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            RobotImage(
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun RobotImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ia),
        contentDescription = "Robot IA",
        modifier = modifier.size(130.dp)
    )
}

@Composable
fun RouteTestSection(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onCardClick: () -> Unit = {}
) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDetails = !showDetails
                onCardClick()
            },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Route,
                    contentDescription = "Rutas",
                    tint = MetroOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pruebas de Cálculo de Rutas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }

            Text(
                text = "Calcula el tiempo de viaje y muestra las estaciones del recorrido",
                style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val tiempoEjemplo = RouteCalculationService.calcularTiempoEstimado("Villa El Salvador", "Miraflores")
            val pasosEjemplo = RouteCalculationService.generarPasosRecorrido("Villa El Salvador", "Miraflores")

            Text(
                text = "Ejemplo: Villa El Salvador → Miraflores",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Tiempo estimado: $tiempoEjemplo minutos",
                style = MaterialTheme.typography.bodyMedium.copy(color = MetroGreen),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Pasos: ${pasosEjemplo.size} estaciones",
                style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
            )

            if (showDetails) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Detalles del recorrido:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                pasosEjemplo.forEachIndexed { index, paso ->
                    val transferencia = if (paso.esTransferencia) " " else ""
                    Text(
                        text = "${index + 1}. ${paso.estacion} (${paso.linea})$transferencia",
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca la card para ocultar detalles",
                    style = MaterialTheme.typography.bodySmall.copy(color = MetroOrange)
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca la card para ver detalles del recorrido",
                    style = MaterialTheme.typography.bodySmall.copy(color = MetroOrange)
                )
            }
        }
    }
}

@Composable
fun HomeScreenPreview(isDarkMode: Boolean) {
    val themeState = ThemeState().apply {
        updateDarkMode(isDarkMode)
    }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            GradientBackground(isDarkMode = themeState.isDarkMode) {
                HomeScreen(navController = rememberNavController())
            }
        }
    }
}

@Preview(
    name = "Modo Claro ☀️",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PreviewHomeLight() {
    HomeScreenPreview(isDarkMode = false)
}

@Preview(
    name = "Modo Oscuro 🌙",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PreviewHomeDark() {
    HomeScreenPreview(isDarkMode = true)
}
