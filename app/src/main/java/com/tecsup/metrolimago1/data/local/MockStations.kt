package com.tecsup.metrolimago1.data.local

import com.tecsup.metrolimago1.domain.models.Station

object MockStations {
    val stations = listOf(
        Station("LIM-01", "Central", "Línea 1", "Av. Central 100", -12.0464, -77.0428, "Estación central del sistema"),
        Station("LIM-02", "San Borja", "Línea 1", "Av. San Borja 45", -12.0800, -76.9800, "Cerca al parque central"),
        Station("LIM-03", "Miraflores", "Línea 2", "C. Larco 200", -12.1214, -77.0297, "Zona turística"),
        Station("LIM-04", "Barranco", "Línea 2", "P. de la reserva 12", -12.1440, -77.0200, "Barranco cultural"),
        Station("LIM-05", "San Isidro", "Línea 1", "C. Las Flores 56", -12.0850, -77.0370, "Distrito financiero"),
        Station("LIM-06", "La Molina", "Línea 4", "Av. La Molina 300", -12.0500, -76.9500, "Zona residencial"),
        Station("LIM-07", "San Miguel", "Línea 3", "Av. La Marina 77", -12.0600, -77.1100, "Acceso a puerto"),
        Station("LIM-08", "Comas", "Línea 4", "Av. Universitaria 9", -11.9800, -77.0700, "Norte de la ciudad"),
        Station("LIM-09", "Ate", "Línea 1", "Av. Faucett 140", -12.0450, -77.0200, "Zona industrial"),
        Station("LIM-10", "Pueblo Libre", "Línea 3", "Jr. Ucayali 23", -12.0950, -77.0600, "Barrio tradicional")
    )

    fun findById(id: String?): Station? = stations.find { it.id == id }
}
