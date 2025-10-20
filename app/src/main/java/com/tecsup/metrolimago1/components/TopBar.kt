package com.tecsup.metrolimago1.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null
) {
    if (navigationIcon != null) {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = { if (actions != null) actions() },
            scrollBehavior = null
        )
    }
}
