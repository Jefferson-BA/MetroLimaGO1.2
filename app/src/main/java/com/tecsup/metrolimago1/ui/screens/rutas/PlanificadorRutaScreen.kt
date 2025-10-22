package com.tecsup.metrolimago1.ui.screens.rutas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificadorRutaScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val blancoYNegro = themeState.blancoYNegro
    val darkMode = themeState.isDarkMode

    val backgroundColor = if (blancoYNegro) Color.White else if (darkMode) Color(0xFF121212) else Color(0xFFF5F5F5)
    val textColor = if (blancoYNegro) Color.Black else if (darkMode) Color.White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (blancoYNegro) Color.DarkGray else if (darkMode) LightGray else Color(0xFF666666)
    val surfaceColor = if (blancoYNegro) Color(0xFFF0F0F0) else if (darkMode) Color(0xFF1E1E1E) else Color(0xFFFFFFFF)

    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planificador de Rutas", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Planifica tu trayecto 🚉",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = origen,
                onValueChange = { origen = it },
                label = { Text("Estación de origen", color = secondaryTextColor) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MetroOrange,
                    unfocusedBorderColor = LightGray,
                    containerColor = surfaceColor,
                    cursorColor = MetroOrange,
                    focusedLabelColor = MetroOrange,
                    unfocusedLabelColor = secondaryTextColor
                ),
                textStyle = LocalTextStyle.current.copy(color = textColor),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Estación de destino", color = secondaryTextColor) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MetroOrange,
                    unfocusedBorderColor = LightGray,
                    containerColor = surfaceColor,
                    cursorColor = MetroOrange,
                    focusedLabelColor = MetroOrange,
                    unfocusedLabelColor = secondaryTextColor
                ),
                textStyle = LocalTextStyle.current.copy(color = textColor),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    mensaje = if (origen.isNotBlank() && destino.isNotBlank()) {
                        "Ruta planificada de $origen a $destino"
                    } else {
                        "Por favor ingresa ambas estaciones"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MetroOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = "Calcular Ruta")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (mensaje.isNotBlank()) {
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanificadorRutaScreenPreview() {
    val navController = rememberNavController()
    MetroLimaGO1Theme(blancoYNegro = true) {
        PlanificadorRutaScreen(navController = navController)
    }
}
