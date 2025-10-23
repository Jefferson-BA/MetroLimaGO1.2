package com.tecsup.metrolimago1.data.local

import com.tecsup.metrolimago1.domain.models.Station

object MetroLimaStations {
    
    // Estaciones de la Línea 1 del Metro de Lima (de Villa El Salvador a San Juan de Lurigancho)
    val linea1Stations = listOf(
        Station("VES", "Villa El Salvador", "Línea 1", "Av. Villa El Salvador", -12.2167, -77.0167, "Terminal sur de la Línea 1"),
        Station("PUM", "Pumacahua", "Línea 1", "Av. Pumacahua", -12.2000, -77.0167, "Estación Pumacahua"),
        Station("VIL", "Villa María del Triunfo", "Línea 1", "Av. Villa María del Triunfo", -12.1833, -77.0167, "Estación Villa María del Triunfo"),
        Station("MAR", "María Auxiliadora", "Línea 1", "Av. María Auxiliadora", -12.1667, -77.0167, "Estación María Auxiliadora"),
        Station("SAG", "San Juan de Miraflores", "Línea 1", "Av. San Juan de Miraflores", -12.1500, -77.0167, "Estación San Juan de Miraflores"),
        Station("JAV", "Javier Prado", "Línea 1", "Av. Javier Prado", -12.1333, -77.0167, "Estación Javier Prado"),
        Station("ATE", "Ate", "Línea 1", "Av. Ate", -12.1167, -77.0167, "Estación Ate"),
        Station("SAN", "San Borja", "Línea 1", "Av. San Borja", -12.1000, -77.0167, "Estación San Borja"),
        Station("LAV", "La Victoria", "Línea 1", "Av. La Victoria", -12.0833, -77.0167, "Estación La Victoria"),
        Station("GRA", "Gamarra", "Línea 1", "Av. Gamarra", -12.0667, -77.0167, "Estación Gamarra"),
        Station("UNI", "Universidad", "Línea 1", "Av. Universitaria", -12.0500, -77.0167, "Estación Universidad"),
        Station("CEN", "Central", "Línea 1", "Av. Central", -12.0333, -77.0167, "Estación Central"),
        Station("SJI", "San Juan de Lurigancho", "Línea 1", "Av. San Juan de Lurigancho", -12.0167, -77.0167, "Terminal norte de la Línea 1")
    )
    
    // Estaciones de la Línea 2 del Metro de Lima (de Ate Vitarte a Callao)
    val linea2Stations = listOf(
        Station("AVI", "Ate Vitarte", "Línea 2", "Av. Ate Vitarte", -12.0167, -76.9500, "Terminal este de la Línea 2"),
        Station("SAN2", "Santa Anita", "Línea 2", "Av. Santa Anita", -12.0333, -76.9500, "Estación Santa Anita"),
        Station("ELM", "El Milagro", "Línea 2", "Av. El Milagro", -12.0500, -76.9500, "Estación El Milagro"),
        Station("SAG2", "Sagrado Corazón", "Línea 2", "Av. Sagrado Corazón", -12.0667, -76.9500, "Estación Sagrado Corazón"),
        Station("MIG", "Miguel Grau", "Línea 2", "Av. Miguel Grau", -12.0833, -76.9500, "Estación Miguel Grau"),
        Station("JAV2", "Javier Prado", "Línea 2", "Av. Javier Prado", -12.1000, -76.9500, "Estación Javier Prado (Transferencia Línea 1)"),
        Station("MIR", "Miraflores", "Línea 2", "Av. Miraflores", -12.1167, -76.9500, "Estación Miraflores"),
        Station("SAN3", "San Isidro", "Línea 2", "Av. San Isidro", -12.1333, -76.9500, "Estación San Isidro"),
        Station("LAR", "Larco", "Línea 2", "Av. Larco", -12.1500, -76.9500, "Estación Larco"),
        Station("BARR", "Barranco", "Línea 2", "Av. Barranco", -12.1667, -76.9500, "Estación Barranco"),
        Station("CHOR", "Chorrillos", "Línea 2", "Av. Chorrillos", -12.1833, -76.9500, "Estación Chorrillos"),
        Station("CAL", "Callao", "Línea 2", "Av. Callao", -12.2000, -76.9500, "Terminal oeste de la Línea 2")
    )
    
    // Todas las estaciones combinadas
    val allStations = linea1Stations + linea2Stations
    
    // Estación de transferencia entre líneas
    val transferStation = "Javier Prado"
    
    fun findStationByName(name: String): Station? {
        return allStations.find { it.name.equals(name, ignoreCase = true) }
    }
    
    fun getStationsByLine(line: String): List<Station> {
        return when (line) {
            "Línea 1" -> linea1Stations
            "Línea 2" -> linea2Stations
            else -> emptyList()
        }
    }
    
    fun isTransferStation(stationName: String): Boolean {
        return stationName.equals(transferStation, ignoreCase = true)
    }
}
