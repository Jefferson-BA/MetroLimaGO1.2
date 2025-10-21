package com.tecsup.metrolimago1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.metrolimago1.navigation.MainNavGraph
import com.tecsup.metrolimago1.ui.theme.MetroLimaGO1Theme
import com.tecsup.metrolimago1.ui.theme.ThemeState
import com.tecsup.metrolimago1.ui.theme.LocalThemeState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeState = ThemeState()
            CompositionLocalProvider(LocalThemeState provides themeState) {
                MetroLimaGO1Theme(darkTheme = LocalThemeState.current.isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainNavGraph()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MetroLimaGO1Theme {
        MainNavGraph()
    }
}