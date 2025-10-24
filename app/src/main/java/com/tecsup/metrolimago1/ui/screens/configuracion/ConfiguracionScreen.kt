package com.tecsup.metrolimago1.ui.screens.configuracion

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.GradientBackground
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.utils.LocalizationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(LocalizationManager.getSavedLanguage(context)) }

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
                    text = stringResource(R.string.settings_title),
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
                    onLanguageChange = { 
                        selectedLanguage = it
                        LocalizationManager.saveLanguage(context, it)
                        // Reiniciar la actividad para aplicar el cambio
                        (context as Activity).recreate()
                    },
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
            text = stringResource(R.string.settings_theme),
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
                    contentDescription = stringResource(R.string.settings_dark_mode),
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.settings_dark_mode),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Text(
                        text = stringResource(R.string.settings_theme_description),
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
            text = stringResource(R.string.settings_language),
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
                        contentDescription = stringResource(R.string.settings_language),
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                        Text(
                            text = stringResource(R.string.settings_language_description),
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
                            text = stringResource(R.string.settings_spanish),
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
                            text = stringResource(R.string.settings_english),
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
            text = stringResource(R.string.settings_about),
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
                    text = stringResource(R.string.app_name),
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
                        text = stringResource(R.string.settings_version) + " 1.0.0",
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.settings_app_description),
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = secondaryTextColor, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = stringResource(R.string.settings_contact), tint = textColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.settings_contact), color = textColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = stringResource(R.string.settings_contact_email),
                    color = textColor,
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = stringResource(R.string.settings_developer), tint = textColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.settings_developer), color = textColor, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = stringResource(R.string.settings_developer_team),
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
