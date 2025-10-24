package com.tecsup.metrolimago1.ui.screens.configuracion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.GradientBackground
import com.tecsup.metrolimago1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    var selectedLanguage by remember { mutableStateOf("es") }

    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    Scaffold(
        bottomBar = {
            GlobalBottomNavBar(
                navController = navController,
                currentRoute = Screen.Configuracion.route
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { paddingValues ->
        GradientBackground(isDarkMode = themeState.isDarkMode) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    )
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                AppearanceSection(
                    isDarkMode = themeState.isDarkMode,
                    onDarkModeToggle = { themeState.updateDarkMode(it) },
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                LanguageSection(
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                AboutSection(
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AppearanceSection(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = "Apariencia",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Modo Oscuro",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Modo Oscuro",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Text(
                        text = "Cambia el tema de la aplicación",
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        checkedTrackColor = Black,
                        uncheckedThumbColor = secondaryTextColor,
                        uncheckedTrackColor = if (isDarkMode) DarkGray else Color(0xFFE0E0E0)
                    )
                )
            }
        }
    }
}

@Composable
fun LanguageSection(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = "Idioma",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Idioma",
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Idioma",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                        Text(
                            text = "Selecciona tu idioma preferido",
                            style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onLanguageChange("es") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLanguage == "es") MetroOrange else secondaryTextColor
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Es Español",
                            color = if (selectedLanguage == "es") White else textColor
                        )
                    }

                    Button(
                        onClick = { onLanguageChange("en") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLanguage == "en") MetroOrange else secondaryTextColor
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Us Inglés",
                            color = if (selectedLanguage == "en") White else textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutSection(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = "Acerca de",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MetroLima GO",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = if (textColor == White) DarkGray else Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Versión 1.0.0",
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "MetroLima GO es tu compañero ideal para navegar por el sistema de Metro de Lima. Planifica tus viajes, consulta horarios y encuentra la mejor ruta.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = secondaryTextColor, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = "Contacto", tint = textColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contacto", color = textColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "soporte@metrolimago.pe",
                    color = textColor,
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = "Desarrollador", tint = textColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Desarrollador", color = textColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "MetroLima Development Team",
                    color = textColor,
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "💡 ConfiguracionScreen - Modo Claro")
@Composable
fun ConfiguracionScreenPreviewLight() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(false) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ConfiguracionScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "🌙 ConfiguracionScreen - Modo Oscuro")
@Composable
fun ConfiguracionScreenPreviewDark() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(true) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ConfiguracionScreen(navController = navController)
        }
    }
}
