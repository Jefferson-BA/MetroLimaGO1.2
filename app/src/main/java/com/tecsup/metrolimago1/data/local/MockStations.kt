package com.tecsup.metrolimago1.data.local

import com.google.android.gms.maps.model.LatLng
import com.tecsup.metrolimago1.domain.models.*

object MockStations {
    // Coordenadas reales del Metro de Lima con datos completos
    val stations = listOf(
        // Línea 1 - Villa El Salvador a San Juan de Lurigancho
        Station(
            "LIM-01", "Villa El Salvador", "Línea 1", "Av. Villa El Salvador", -12.1939, -76.9399,
            "Terminal sur de la Línea 1", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.RESTAURANT, "Pollería Don Pollo", "Av. Villa El Salvador 123", "150m"),
                NearbyService(ServiceType.BANK, "BCP", "Av. Villa El Salvador 456", "200m"),
                NearbyService(ServiceType.PHARMACY, "Farmacia Universal", "Av. Villa El Salvador 789", "100m")
            )
        ),
        Station(
            "LIM-02", "María Auxiliadora", "Línea 1", "Av. María Auxiliadora", -12.1639, -76.9703,
            "Conexión con buses", "05:00", "23:00", StationStatus.OPERATIONAL, ""
        ),
        Station(
            "LIM-03", "La Cultura", "Línea 1", "Av. La Cultura", -12.0865, -76.9779,
            "Cerca al parque", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.PARK, "Parque La Cultura", "Av. La Cultura", "50m"),
                NearbyService(ServiceType.UNIVERSITY, "Universidad San Marcos", "Av. Universitaria", "300m")
            )
        ),
        Station(
            "LIM-04", "San Borja Sur", "Línea 1", "Av. San Borja Sur", -12.1003, -76.9961,
            "Distrito San Borja", "05:00", "23:00", StationStatus.OPERATIONAL, ""
        ),
        Station(
            "LIM-05", "Angamos", "Línea 1", "Av. Angamos", -12.1214, -77.0297,
            "Conexión con Línea 2", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.MALL, "Plaza San Miguel", "Av. La Marina", "400m"),
                NearbyService(ServiceType.HOSPITAL, "Clínica San Borja", "Av. Corpac", "500m")
            )
        ),
        Station("LIM-06", "San Borja Norte", "Línea 1", "Av. San Borja Norte", -12.0800, -76.9800, "Centro de San Borja"),
        Station("LIM-07", "La Victoria", "Línea 1", "Av. La Victoria", -12.0600, -77.0200, "Distrito comercial"),
        Station(
            "LIM-08", "Gamarra", "Línea 1", "Av. Gamarra", -12.0450, -77.0200,
            "Centro comercial", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.MALL, "Megaplaza Gamarra", "Av. Gamarra", "100m"),
                NearbyService(ServiceType.RESTAURANT, "Cevichería El Pez", "Av. Gamarra 200", "50m"),
                NearbyService(ServiceType.ATM, "Cajero BCP", "Av. Gamarra", "30m")
            )
        ),
        Station("LIM-09", "El Agustino", "Línea 1", "Av. El Agustino", -12.0200, -77.0000, "Zona residencial"),
        Station("LIM-10", "San Juan de Lurigancho", "Línea 1", "Av. San Juan de Lurigancho", -11.9800, -77.0700, "Terminal norte"),
        
        // Línea 2 - Ate Vitarte a Callao
        Station("LIM-11", "Ate Vitarte", "Línea 2", "Av. Ate Vitarte", -12.0450, -76.9500, "Terminal este"),
        Station("LIM-12", "Santa Anita", "Línea 2", "Av. Santa Anita", -12.0500, -76.9600, "Distrito Santa Anita"),
        Station("LIM-13", "La Molina", "Línea 2", "Av. La Molina", -12.0500, -76.9500, "Zona residencial"),
        Station("LIM-14", "San Luis", "Línea 2", "Av. San Luis", -12.0600, -76.9700, "Distrito San Luis"),
        Station(
            "LIM-15", "San Isidro", "Línea 2", "Av. San Isidro", -12.0850, -77.0370,
            "Distrito financiero", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.BANK, "BBVA", "Av. Las Begonias", "100m"),
                NearbyService(ServiceType.RESTAURANT, "Astrid & Gastón", "Calle Las Begonias 621", "200m"),
                NearbyService(ServiceType.ATM, "Múltiples cajeros", "Centro de Lima", "50m")
            )
        ),
        Station(
            "LIM-16", "Miraflores", "Línea 2", "Av. Miraflores", -12.1214, -77.0297,
            "Zona turística", "05:00", "23:00", StationStatus.OPERATIONAL, "",
            listOf(
                NearbyService(ServiceType.MALL, "Larcomar", "Malecón de la Reserva", "600m"),
                NearbyService(ServiceType.RESTAURANT, "Restaurante Central", "Av. Pedro de Osma", "400m"),
                NearbyService(ServiceType.PARK, "Parque Kennedy", "Av. José Larco", "300m")
            )
        ),
        Station("LIM-17", "Barranco", "Línea 2", "Av. Barranco", -12.1440, -77.0200, "Barranco cultural"),
        Station("LIM-18", "Chorrillos", "Línea 2", "Av. Chorrillos", -12.1600, -77.0100, "Distrito Chorrillos"),
        Station("LIM-19", "San Miguel", "Línea 2", "Av. San Miguel", -12.0600, -77.1100, "Acceso al puerto"),
        Station("LIM-20", "Callao", "Línea 2", "Av. Callao", -12.0500, -77.1300, "Terminal oeste"),
        
        // Metropolitano - Comas a Chorrillos (En construcción)
        Station("LIM-21", "Comas", "Metropolitano", "Av. Comas", -11.9800, -77.0700, "Norte de Lima", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-22", "Independencia", "Metropolitano", "Av. Independencia", -12.0000, -77.0500, "Distrito Independencia", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-23", "Rímac", "Metropolitano", "Av. Rímac", -12.0200, -77.0300, "Distrito Rímac", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-24", "Cercado de Lima", "Metropolitano", "Av. Cercado", -12.0464, -77.0428, "Centro histórico", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-25", "La Victoria", "Metropolitano", "Av. La Victoria", -12.0600, -77.0200, "Distrito comercial", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-26", "Lince", "Metropolitano", "Av. Lince", -12.0800, -77.0300, "Distrito Lince", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-27", "Jesús María", "Metropolitano", "Av. Jesús María", -12.0900, -77.0400, "Distrito Jesús María", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-28", "Magdalena", "Metropolitano", "Av. Magdalena", -12.1000, -77.0500, "Distrito Magdalena", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-29", "Pueblo Libre", "Metropolitano", "Av. Pueblo Libre", -12.0950, -77.0600, "Barrio tradicional", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        Station("LIM-30", "Chorrillos", "Metropolitano", "Av. Chorrillos", -12.1600, -77.0100, "Terminal sur", "05:00", "23:00", StationStatus.CONSTRUCTION, "")
    )

    // Coordenadas de Lima para centrar el mapa
    val LIMA_CENTER = LatLng(-12.0464, -77.0428)
    val LIMA_ZOOM = 12f

    // Función para obtener estaciones por línea
    fun getStationsByLine(line: String): List<Station> {
        return stations.filter { it.line == line }
    }

    // Función para obtener coordenadas de una estación
    fun getStationLatLng(stationId: String): LatLng? {
        val station = stations.find { it.id == stationId }
        return station?.let { LatLng(it.latitude, it.longitude) }
    }

    // Función para obtener todas las coordenadas de una línea
    fun getLineCoordinates(line: String): List<LatLng> {
        return getStationsByLine(line).map { LatLng(it.latitude, it.longitude) }
    }

    fun findById(id: String?): Station? = stations.find { it.id == id }
}
