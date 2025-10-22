package com.tecsup.metrolimago1.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.ui.theme.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val themeState = LocalThemeState.current

    val backgroundColor = if (themeState.isDarkMode) DarkGray else Color(0xFFF5F5F5)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    Scaffold(
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Home.route)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "MetroLima GO",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.padding(bottom = 22.dp)
            )

            SearchBar(
                onSearchClick = { query ->
                    navController.navigate(Screen.Estaciones.route)
                },
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = CardGray)
            ) {
                NextArrivalsSection(textColor = textColor, secondaryTextColor = secondaryTextColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = CardGray)
            ) {
                NotificationsSection(textColor = textColor, secondaryTextColor = secondaryTextColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = CardGray)
            ) {
                AISection(navController, textColor, secondaryTextColor)
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearchClick: (String) -> Unit,
    textColor: Color,
    secondaryTextColor: Color
) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        shape = RoundedCornerShape(28.dp),
        onValueChange = { searchQuery = it },
        placeholder = {
            Text(
                text = "¿A dónde vas?",
                color = secondaryTextColor
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = secondaryTextColor
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchClick(searchQuery) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Buscar",
                        tint = MetroOrange
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MetroOrange,
            unfocusedBorderColor = LightGray,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        ),
        singleLine = true
    )
}

@Composable
fun NextArrivalsSection(textColor: Color, secondaryTextColor: Color) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                    color = textColor
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Información en tiempo real",
            style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
        )

        Spacer(modifier = Modifier.height(16.dp))
        ArrivalItem("Villa El Salvador", "hacia San Martin", "3 min", textColor, secondaryTextColor)
        Spacer(modifier = Modifier.height(8.dp))
        ArrivalItem("Angamos", "hacia San Martin", "5 min", textColor, secondaryTextColor)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
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

            Text(
                text = time,
                color = White,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .background(MetroGreen, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun NotificationsSection(textColor: Color, secondaryTextColor: Color) {
    Column(modifier = Modifier.padding(16.dp)) {
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
                style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        NotificationItem(
            "Servicio Normal",
            "Todas las líneas operando con normalidad",
            textColor,
            secondaryTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        NotificationItem(
            "Mantenimiento Programado",
            "Línea 1: Horario reducido el domingo 20 de octubre",
            textColor,
            secondaryTextColor
        )
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
    textColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Pregúntale a la IA",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MetroOrange
                )
            )
            Text(
                text = "Usa el chat de ayuda inteligente.",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = { navController.navigate(Screen.Chat.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MetroOrange,
                    contentColor = White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar conversación")
            }
        }

        val infiniteTransition = rememberInfiniteTransition(label = "shine")

        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alphaAnim"
        )

        Image(
            painter = painterResource(id = R.drawable.ia),
            contentDescription = "Robot IA",
            modifier = Modifier
                .size(128.dp)
                .alpha(alpha)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}
