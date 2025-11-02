package com.tecsup.metrolimago1.data.local

import com.google.android.gms.maps.model.LatLng
import com.tecsup.metrolimago1.domain.models.Line
import com.tecsup.metrolimago1.domain.models.LineStatus
import com.tecsup.metrolimago1.ui.theme.MetroOrange
import com.tecsup.metrolimago1.ui.theme.MetroGreen

object MockLines {
    val lines = listOf(
        Line(
            id = "LINEA_1",
            name = "Línea 1",
            color = MetroOrange.toString(),
            status = LineStatus.OPERATIONAL,
            description = "Une Villa El Salvador con San Juan de Lurigancho",
            openingDate = "11 de julio de 2011",
            operatingHours = "05:00 - 23:00",
            route = listOf(
                // Puntos aproximados de la Línea 1
                LatLng(-12.1939, -76.9399), // Villa El Salvador
                LatLng(-12.1391, -77.0287), // Centro
                LatLng(-12.0563, -77.1184)  // San Juan de Lurigancho
            ),
            stations = MockStations.stations.filter { it.line == "Línea 1" }
        ),
        Line(
            id = "LINEA_2",
            name = "Línea 2",
            color = MetroGreen.toString(),
            status = LineStatus.OPERATIONAL,
            description = "Conecta el Callao con Ate",
            openingDate = "11 de abril de 2014",
            operatingHours = "05:00 - 23:00",
            route = listOf(
                // Puntos aproximados de la Línea 2
                LatLng(-12.0677, -77.1519), // Callao
                LatLng(-12.0563, -77.1184), // Centro
                LatLng(-11.9800, -77.0700)  // Ate
            ),
            stations = MockStations.stations.filter { it.line == "Línea 2" }
        ),
        Line(
            id = "LINEA_3",
            name = "Línea 3",
            color = "#2196F3", // Azul
            status = LineStatus.CONSTRUCTION,
            description = "En construcción - Conectará el Callao con San Juan de Lurigancho",
            openingDate = "2025 (estimado)",
            operatingHours = "Próximamente",
            route = listOf(
                LatLng(-12.0677, -77.1519), // Callao
                LatLng(-12.0563, -77.1184), // Centro
                LatLng(-11.9800, -77.0700)  // San Juan de Lurigancho
            ),
            stations = MockStations.stations.filter { it.line == "Línea 3" }
        )
    )

    fun findById(id: String?): Line? {
        return lines.find { it.id == id }
    }

    fun findByStationLine(line: String?): Line? {
        return lines.find { it.name == line }
    }
}
