package com.tecsup.metrolimago1.ui.screens.vivo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.components.GlobalBottomNavBar
import com.tecsup.metrolimago1.navigation.Screen
import com.tecsup.metrolimago1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VivoScreen(navController: NavController) {
    val themeState = LocalThemeState.current

    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    GradientBackground(isDarkMode = themeState.isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.live_title),
                            color = textColor,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            bottomBar = {
                GlobalBottomNavBar(
                    navController = navController,
                    currentRoute = Screen.Vivo.route
                )
            },
            containerColor = Color.Transparent,
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
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ServiceStatusCard(
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                OperatingHoursCard(
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )

                AlertsCard(
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
fun ServiceStatusCard(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Estado",
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.live_service_status),
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Text(
                text = stringResource(R.string.live_real_time_status),
                color = secondaryTextColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ServiceLineItem(
                lineName = "Línea 1",
                status = "Servicio operando con normalidad",
                isOperating = true,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            ServiceLineItem(
                lineName = "Línea 2",
                status = "En construcción - Apertura estimada 2026",
                isOperating = false,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
        }
    }
}

@Composable
fun ServiceLineItem(
    lineName: String,
    status: String,
    isOperating: Boolean,
    textColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isOperating) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = "Estado",
            tint = if (isOperating) MetroGreen else Color(0xFFE53E3E),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lineName,
                color = textColor,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = status,
                color = secondaryTextColor,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isOperating) MetroGreen else Color(0xFFE53E3E)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isOperating) "Operando" else "Cerrado",
                color = White,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun OperatingHoursCard(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Horario",
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.live_operating_hours),
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            OperatingHoursItem(
                dayType = stringResource(R.string.live_weekdays),
                description = stringResource(R.string.live_weekdays_description),
                hours = stringResource(R.string.live_weekdays_hours),
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            OperatingHoursItem(
                dayType = stringResource(R.string.live_saturday),
                description = stringResource(R.string.live_saturday_description),
                hours = stringResource(R.string.live_saturday_hours),
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            OperatingHoursItem(
                dayType = stringResource(R.string.live_sunday_holidays),
                description = stringResource(R.string.live_sunday_holidays_description),
                hours = stringResource(R.string.live_sunday_hours),
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
        }
    }
}

@Composable
fun OperatingHoursItem(
    dayType: String,
    description: String,
    hours: String,
    textColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dayType,
                color = textColor,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = description,
                color = secondaryTextColor,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = hours,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun AlertsCard(
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alertas",
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.live_alerts_notices),
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            AlertItem(
                title = stringResource(R.string.live_scheduled_maintenance),
                description = stringResource(R.string.live_maintenance_description_1),
                date = stringResource(R.string.live_maintenance_date),
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertItem(
                title = stringResource(R.string.live_scheduled_maintenance),
                description = stringResource(R.string.live_maintenance_description_2),
                date = stringResource(R.string.live_maintenance_date),
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )
        }
    }
}

@Composable
fun AlertItem(
    title: String,
    description: String,
    date: String,
    textColor: Color,
    secondaryTextColor: Color
) {
    Column {
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = secondaryTextColor,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date,
            color = secondaryTextColor,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "💡 VivoScreen - Modo Claro")
@Composable
fun VivoScreenPreviewLight() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(false) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            VivoScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "🌙 VivoScreen - Modo Oscuro")
@Composable
fun VivoScreenPreviewDark() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(true) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            VivoScreen(navController = navController)
        }
    }
}
