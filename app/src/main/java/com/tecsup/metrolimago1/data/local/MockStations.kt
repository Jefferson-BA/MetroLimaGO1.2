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
        Station("LIM-30", "Chorrillos", "Metropolitano", "Av. Chorrillos", -12.1600, -77.0100, "Terminal sur", "05:00", "23:00", StationStatus.CONSTRUCTION, ""),
        
        // Corredor Morado - Rutas 404, 405, 409
        Station("CM-01", "La Capilla", "Corredor Morado", "Terminal Norte (SJL)", -11.9685000, -77.0001000, "Terminal Norte - San Juan de Lurigancho", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-02", "Montenegro", "Corredor Morado", "SJL", -11.9655000, -77.0005000, "Paradero Montenegro", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-03", "La Cuatro", "Corredor Morado", "SJL", -11.9575000, -76.9950000, "Paradero La Cuatro", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-04", "Bayóvar", "Corredor Morado", "SJL - Integración Línea 1", -11.9560733, -76.9940003, "Integración con Línea 1 Metro", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-05", "Celima", "Corredor Morado", "SJL", -11.9330000, -77.0080000, "Paradero Celima", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-06", "Mercado de Flores", "Corredor Morado", "Av. Abancay", -12.0520000, -77.0295000, "Paradero troncal compartido - Rutas 404, 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-07", "Acho", "Corredor Morado", "Av. Abancay", -12.0400000, -77.0255000, "Paradero troncal compartido - Rutas 404, 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-08", "Cuzco", "Corredor Morado", "Av. Abancay", -12.0515000, -77.0298000, "Paradero troncal compartido - Rutas 404, 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-09", "Grau (Nuevo)", "Corredor Morado", "Eje Av. Abancay", -12.0560000, -77.0310000, "Eje Av. Abancay - Ubicación Reubicada", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-10", "Plaza Bolognesi", "Corredor Morado", "Eje Central - Cruce Av. Garcilaso", -12.0620000, -77.0375000, "Eje Central, Cruce Av. Garcilaso", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-11", "28 de Julio", "Corredor Morado", "Av. 28 de Julio", -12.0640000, -77.0320000, "Paradero troncal - Rutas 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-12", "Unanue", "Corredor Morado", "Av. 28 de Julio", -12.0665000, -77.0318000, "Paradero troncal - Rutas 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-13", "Católica", "Corredor Morado", "Av. 28 de Julio", -12.0710000, -77.0315000, "Paradero troncal - Rutas 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-14", "México", "Corredor Morado", "Av. 28 de Julio", -12.0785000, -77.0310000, "Paradero troncal - Rutas 405, 409", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-15", "Hospital PNP", "Corredor Morado", "Av. 28 de Julio / Av. Arica", -12.0805000, -77.0460000, "Ruta 404 - Av. 28 de Julio / Av. Arica", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-16", "Pardo de Zela", "Corredor Morado", "Lince", -12.0835000, -77.0345000, "Paradero Pardo de Zela", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-17", "Javier Prado", "Corredor Morado", "San Isidro", -12.0945000, -77.0360000, "Paradero Javier Prado", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-18", "La Virgen", "Corredor Morado", "Terminal Oeste (Magdalena)", -12.0950000, -77.0760000, "Terminal Oeste - Magdalena", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CM-19", "Carnaval y Moreyra", "Corredor Morado", "Terminal Sur (San Isidro)", -12.1005000, -77.0395000, "Terminal Sur - San Isidro", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        
        // Corredor Azul - Servicio Troncal 301 y Rutas Complementarias
        // Dirección Rímac → Barranco (Ida/Sentido Sur)
        Station("CA-01", "Amancaes", "Corredor Azul", "Rímac", -12.01630, -77.02100, "Terminal Norte - Rímac", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-02", "La Colonia", "Corredor Azul", "Rímac", -12.02550, -77.02750, "Paradero La Colonia", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-03", "Chira", "Corredor Azul", "Rímac", -12.03450, -77.03350, "Paradero Chira", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-04", "Virú", "Corredor Azul", "Cercado de Lima", -12.04000, -77.03480, "Paradero Virú", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-05", "E. Villar", "Corredor Azul", "Lince", -12.06200, -77.03900, "Paradero E. Villar", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-06", "Manuel Segura", "Corredor Azul", "Lince", -12.07250, -77.03780, "Paradero Manuel Segura", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-07", "Tomás Guido", "Corredor Azul", "Lince", -12.08310, -77.03850, "Paradero Tomás Guido", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-08", "Angamos", "Corredor Azul", "Miraflores", -12.10090, -77.03510, "Paradero Angamos", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-09", "Piura", "Corredor Azul", "Miraflores", -12.11050, -77.03250, "Paradero Piura", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-10", "Pardo", "Corredor Azul", "Miraflores", -12.11500, -77.03050, "Paradero Pardo - Terminal 336", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-11", "Larco", "Corredor Azul", "Miraflores", -12.12050, -77.02700, "Paradero Larco", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-12", "Balta", "Corredor Azul", "Barranco", -12.13700, -77.02270, "Paradero Balta", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-13", "Plaza Butters", "Corredor Azul", "Terminal Sur (Barranco)", -12.13810, -77.02250, "Terminal Sur - Barranco", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        // Dirección Barranco → Rímac (Vuelta/Sentido Norte)
        Station("CA-14", "El Sol", "Corredor Azul", "Barranco", -12.13850, -77.02245, "Paradero El Sol (Plaza Butters inicio)", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-15", "Schell", "Corredor Azul", "Miraflores", -12.11210, -77.03150, "Paradero Schell", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-16", "Juan De Arona", "Corredor Azul", "San Isidro", -12.09100, -77.03680, "Paradero Juan De Arona", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-17", "Manuel Segura (Vuelta)", "Corredor Azul", "Lince", -12.07150, -77.03800, "Paradero Manuel Segura - Vuelta", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-18", "Saco Oliveros", "Corredor Azul", "Cercado de Lima", -12.05950, -77.03880, "Paradero Saco Oliveros", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-19", "España", "Corredor Azul", "Cercado de Lima", -12.04610, -77.03600, "Paradero España", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-20", "Pizarro", "Corredor Azul", "Rímac", -12.03700, -77.03450, "Paradero Pizarro", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-21", "24 De Junio", "Corredor Azul", "Terminal Norte (Rímac)", -12.01590, -77.02050, "Terminal Norte - Rímac", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        // Puntos clave para rutas complementarias (303, 336)
        Station("CA-22", "D. Casanova", "Corredor Azul", "Cerca de la Av. Arequipa", -12.08750, -77.03750, "Ruta 303 - Cerca de la Av. Arequipa", "05:00", "23:00", StationStatus.OPERATIONAL, ""),
        Station("CA-23", "Uruguay", "Corredor Azul", "Cerca de la Av. Arequipa", -12.05350, -77.03890, "Ruta 303 - Cerca de la Av. Arequipa", "05:00", "23:00", StationStatus.OPERATIONAL, "")
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
