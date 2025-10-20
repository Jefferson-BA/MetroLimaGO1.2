package com.tecsup.metrolimago1.ui.screens.estaciones

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tecsup.metrolimago1.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionDetailScreen(navController: NavController, estacionId: String?) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Detalle estación",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Detalle de la estación: ${estacionId ?: "-"}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
