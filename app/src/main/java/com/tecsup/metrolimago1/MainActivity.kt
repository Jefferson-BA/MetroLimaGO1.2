package com.tecsup.metrolimago1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme // Necesario si no está
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface // Necesario si no está
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.navigation.Screen // Importa el sealed class que creaste
import com.tecsup.metrolimago1.ui.screens.HomeScreen // Importa la pantalla que creaste en Commit 2
import com.tecsup.metrolimago1.ui.theme.MetroLimaGO1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetroLimaGO1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MetroLimagoApp()
                }
            }
        }
    }
}

@Composable
fun MetroLimagoApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Las demás rutas (Estaciones, Rutas, Configuración) se añaden en el Commit 4
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MetroLimaGO1Theme {
        MetroLimagoApp()
    }
}