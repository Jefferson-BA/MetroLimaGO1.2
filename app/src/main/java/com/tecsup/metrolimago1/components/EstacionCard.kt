package com.tecsup.metrolimago1.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.metrolimago1.domain.models.Station

@Composable
fun EstacionCard(station: Station, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(40.dp))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = station.name, style = MaterialTheme.typography.titleMedium)
                Text(text = station.line, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
