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
            name = "Metropolitano",
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
            stations = MockStations.stations.filter { it.line == "Metropolitano" }
        ),
        Line(
            id = "CORREDOR_MORADO",
            name = "Corredor Morado",
            color = "#9C27B0", // Morado
            status = LineStatus.OPERATIONAL,
            description = "Rutas 404, 405, 409 - Conecta San Juan de Lurigancho con San Isidro y Magdalena",
            openingDate = "Operativo",
            operatingHours = "05:00 - 23:00",
            route = listOf(
                LatLng(-11.9685000, -77.0001000), // La Capilla (Terminal Norte SJL)
                LatLng(-11.9560733, -76.9940003), // Bayóvar (Integración Línea 1)
                LatLng(-12.0520000, -77.0295000), // Mercado de Flores (Troncal)
                LatLng(-12.0560000, -77.0310000), // Grau (Nuevo)
                LatLng(-12.0620000, -77.0375000), // Plaza Bolognesi
                LatLng(-12.0640000, -77.0320000), // 28 de Julio
                LatLng(-12.0785000, -77.0310000), // México
                LatLng(-12.0945000, -77.0360000), // Javier Prado
                LatLng(-12.1005000, -77.0395000), // Carnaval y Moreyra (Terminal Sur San Isidro)
                LatLng(-12.0950000, -77.0760000)  // La Virgen (Terminal Oeste Magdalena)
            ),
            stations = MockStations.stations.filter { it.line == "Corredor Morado" }
        ),
        Line(
            id = "CORREDOR_AZUL",
            name = "Corredor Azul",
            color = "#2196F3", // Azul
            status = LineStatus.OPERATIONAL,
            description = "Servicio Troncal 301 y Rutas Complementarias 303, 336 - Conecta Rímac con Barranco",
            openingDate = "Operativo",
            operatingHours = "05:00 - 23:00",
            route = listOf(
                LatLng(-12.01630, -77.02100), // Amancaes (Terminal Norte Rímac)
                LatLng(-12.02550, -77.02750), // La Colonia
                LatLng(-12.03450, -77.03350), // Chira
                LatLng(-12.04000, -77.03480), // Virú
                LatLng(-12.06200, -77.03900), // E. Villar
                LatLng(-12.07250, -77.03780), // Manuel Segura
                LatLng(-12.08310, -77.03850), // Tomás Guido
                LatLng(-12.10090, -77.03510), // Angamos
                LatLng(-12.11050, -77.03250), // Piura
                LatLng(-12.11500, -77.03050), // Pardo (Terminal 336)
                LatLng(-12.12050, -77.02700), // Larco
                LatLng(-12.13700, -77.02270), // Balta
                LatLng(-12.13810, -77.02250)  // Plaza Butters (Terminal Sur Barranco)
            ),
            stations = MockStations.stations.filter { it.line == "Corredor Azul" }
        )
    )

    fun findById(id: String?): Line? {
        return lines.find { it.id == id }
    }

    fun findByStationLine(line: String?): Line? {
        return lines.find { it.name == line }
    }
}
