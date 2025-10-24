package com.tecsup.metrolimago1.ui.screens.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import com.tecsup.metrolimago1.utils.TranslationUtils
import com.tecsup.metrolimago1.utils.LocaleUtils
import android.app.Activity
import android.content.Context
import android.content.Intent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Estado para idioma - siempre en español por defecto
    var selectedLanguage by remember { mutableStateOf("es") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLanguageChangeAlert by remember { mutableStateOf(false) }
    var pendingLanguage by remember { mutableStateOf<String?>(null) }

    // Colores dinámicos según el tema
    val backgroundColor = if (themeState.isDarkMode) DarkGray else LightBackground
    val cardColor = if (themeState.isDarkMode) CardGray else LightCard
    val textColor = if (themeState.isDarkMode) White else LightTextPrimary
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else LightTextSecondary

    Scaffold(
        bottomBar = {
            GlobalBottomNavBar(navController = navController, currentRoute = Screen.Configuracion.route)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Título principal
            Text(
                text = TranslationUtils.getText(context, "settings"),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Sección Apariencia
            AppearanceSection(
                isDarkMode = themeState.isDarkMode,
                onDarkModeToggle = { themeState.updateDarkMode(it) },
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                context = context
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Idioma
            LanguageSection(
                selectedLanguage = selectedLanguage,
                onLanguageChange = { language ->
                    selectedLanguage = language
                    // Guardar preferencia de idioma
                    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("selected_language", language).apply()
                    // Aplicar cambio de idioma
                    TranslationUtils.changeLanguage(context, language)
                },
                isDarkMode = themeState.isDarkMode,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                context = context
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Acerca de
            AboutSection(
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                context = context
            )
        }
    }

    // Diálogo de selección de idioma
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = selectedLanguage,
            onLanguageSelected = { language ->
                selectedLanguage = language
                // Guardar idioma seleccionado
                showLanguageDialog = false

                // Mostrar alert de cambio de idioma
                pendingLanguage = language
                showLanguageChangeAlert = true
            },
            onDismiss = { showLanguageDialog = false },
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            context = context
        )
    }

    // Card Alert de cambio de idioma
    if (showLanguageChangeAlert) {
        LanguageChangeAlert(
            onRestartNow = {
                showLanguageChangeAlert = false
                pendingLanguage = null
                // Reiniciar la actividad para aplicar el cambio de idioma
                if (context is Activity) {
                    val intent = Intent(context, context.javaClass)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("language_changed", true)
                    context.startActivity(intent)
                    context.finish()
                }
            },
            onRestartLater = {
                showLanguageChangeAlert = false
                pendingLanguage = null
            },
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            context = context
        )
    }
}

@Composable
fun AppearanceSection(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Column {
        Text(
            text = TranslationUtils.getText(context, "appearance"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(28.dp),
            border = if (!isDarkMode) BorderStroke(1.dp, LightBorderStrong) else null
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = TranslationUtils.getText(context, "dark_mode"),
                    tint = if (isDarkMode) textColor else LightIconSettings,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = TranslationUtils.getText(context, "dark_mode"),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Text(
                        text = TranslationUtils.getText(context, "change_theme"),
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        checkedTrackColor = MetroOrange,
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
    isDarkMode: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Column {
        Text(
            text = TranslationUtils.getText(context, "language"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(28.dp),
            border = if (!isDarkMode) BorderStroke(1.dp, LightBorderStrong) else null
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = TranslationUtils.getText(context, "language"),
                        tint = if (isDarkMode) textColor else LightIconLanguage,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = TranslationUtils.getText(context, "language"),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                        Text(
                            text = TranslationUtils.getText(context, "select_language"),
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
                        onClick = { 
                            onLanguageChange("es")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLanguage == "es") MetroOrange else secondaryTextColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = TranslationUtils.getText(context, "spanish"),
                            color = if (selectedLanguage == "es") White else textColor
                        )
                    }

                    Button(
                        onClick = { 
                            onLanguageChange("en")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedLanguage == "en") MetroOrange else secondaryTextColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = TranslationUtils.getText(context, "english"),
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
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Column {
        Text(
            text = TranslationUtils.getText(context, "about"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = TranslationUtils.getText(context, "app_name"),
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
                        text = "${TranslationUtils.getText(context, "version")} 1.0.0",
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = TranslationUtils.getText(context, "app_description"),
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = secondaryTextColor, thickness = 1.dp)

                Spacer(modifier = Modifier.height(12.dp))

                // Contacto
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = TranslationUtils.getText(context, "contact"),
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = TranslationUtils.getText(context, "contact"),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }

                Text(
                    text = "soporte@metrolimago.pe",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Desarrollador
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = TranslationUtils.getText(context, "developer"),
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = TranslationUtils.getText(context, "developer"),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }

                Text(
                    text = TranslationUtils.getText(context, "development_team"),
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                    modifier = Modifier.padding(start = 28.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun LanguageSection(
    selectedLanguage: String,
    onLanguageClick: () -> Unit,
    isDarkMode: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    Column {
        Text(
            text = TranslationUtils.getText(context, "language"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLanguageClick() },
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = TranslationUtils.getText(context, "language"),
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = TranslationUtils.getText(context, "language"),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Text(
                        text = if (selectedLanguage == "es") TranslationUtils.getText(context, "spanish") else TranslationUtils.getText(context, "english"),
                        style = MaterialTheme.typography.bodySmall.copy(color = secondaryTextColor)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = TranslationUtils.getText(context, "back"),
                    tint = secondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = TranslationUtils.getText(context, "language"),
                color = textColor
            )
        },
        text = {
            Column {
                listOf("es" to TranslationUtils.getText(context, "spanish"), "en" to TranslationUtils.getText(context, "english")).forEach { (code, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(code) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == code,
                            onClick = { onLanguageSelected(code) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = name,
                            color = textColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(TranslationUtils.getText(context, "ok"))
            }
        },
        containerColor = cardColor
    )
}

@Composable
fun LanguageChangeAlert(
    onRestartNow: () -> Unit,
    onRestartLater: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    context: android.content.Context
) {
    AlertDialog(
        onDismissRequest = onRestartLater,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = TranslationUtils.getText(context, "language"),
                    tint = MetroOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = TranslationUtils.getText(context, "language"),
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Column {
                Text(
                    text = TranslationUtils.getText(context, "language_change_message"),
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = TranslationUtils.getText(context, "language_change_question"),
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRestartNow,
                colors = ButtonDefaults.buttonColors(containerColor = MetroOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = TranslationUtils.getText(context, "language_restart_now"),
                    color = White,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onRestartLater,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = textColor
                )
            ) {
                Text(
                    text = TranslationUtils.getText(context, "language_restart_later"),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        containerColor = cardColor
    )
}