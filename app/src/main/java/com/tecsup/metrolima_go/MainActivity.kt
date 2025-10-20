package com.tecsup.metrolima_go

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.metrolima_go.data.Estacion
import com.tecsup.metrolima_go.ui.screens.DetalleEstacionScreen
import com.tecsup.metrolima_go.ui.screens.ListaEstacionesScreen
import com.tecsup.metrolima_go.ui.theme.MetroLima_GoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetroLima_GoTheme {
                MetroLimaApp()
            }
        }
    }
}

@Composable
fun MetroLimaApp() {
    var estacionSeleccionada by remember { mutableStateOf<Estacion?>(null) }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (estacionSeleccionada != null) {
            DetalleEstacionScreen(
                estacion = estacionSeleccionada!!,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            ListaEstacionesScreen(
                onEstacionClick = { estacion ->
                    estacionSeleccionada = estacion
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MetroLimaAppPreview() {
    MetroLima_GoTheme {
        MetroLimaApp()
    }
}