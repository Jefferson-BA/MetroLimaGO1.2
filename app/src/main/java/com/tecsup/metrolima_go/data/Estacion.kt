package com.tecsup.metrolima_go.data

data class Estacion(
    val id: String,
    val nombre: String,
    val subtexto: String,
    val linea: String,
    val tiempo: String,
    val distancia: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val servicios: List<String> = emptyList(),
    val estado: EstadoEstacion = EstadoEstacion.OPERANDO
)

enum class EstadoEstacion {
    OPERANDO,
    MANTENIMIENTO,
    CERRADO
}

object EstacionesMock {
    val estaciones = listOf(
        Estacion(
            id = "1",
            nombre = "Villa El Salvador",
            subtexto = "Villa El Salvador",
            linea = "Línea 1",
            tiempo = "3 min",
            direccion = "Av. Separadora Industrial con Av. Pachacútec",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi")
        ),
        Estacion(
            id = "2",
            nombre = "Villa María del Triunfo",
            subtexto = "Villa María del Triunfo",
            linea = "Línea 1",
            tiempo = "5 min",
            direccion = "Av. Separadora Industrial con Av. Los Héroes",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería")
        ),
        Estacion(
            id = "3",
            nombre = "San Juan",
            subtexto = "San Juan de Miraflores",
            linea = "Línea 1",
            tiempo = "9 min",
            direccion = "Av. Separadora Industrial con Av. San Juan",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi")
        ),
        Estacion(
            id = "4",
            nombre = "Atocongo",
            subtexto = "San Juan de Miraflores",
            linea = "Línea 1",
            tiempo = "10 min",
            direccion = "Av. Separadora Industrial con Av. Atocongo",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería")
        ),
        Estacion(
            id = "5",
            nombre = "San Borja Sur",
            subtexto = "San Borja",
            linea = "Línea 1",
            tiempo = "2 min",
            direccion = "Av. Javier Prado Este con Av. San Borja Sur",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi", "Cajero automático")
        ),
        Estacion(
            id = "6",
            nombre = "San Borja Norte",
            subtexto = "San Borja",
            linea = "Línea 1",
            tiempo = "1 min",
            direccion = "Av. Javier Prado Este con Av. San Borja Norte",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi")
        ),
        Estacion(
            id = "7",
            nombre = "Angamos",
            subtexto = "Miraflores",
            linea = "Línea 1",
            tiempo = "5 min",
            direccion = "Av. Angamos con Av. Arequipa",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi", "Cajero automático")
        ),
        Estacion(
            id = "8",
            nombre = "Grau",
            subtexto = "Lima",
            linea = "Línea 1",
            tiempo = "7 min",
            direccion = "Av. Grau con Av. Abancay",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi"),
            estado = EstadoEstacion.MANTENIMIENTO
        ),
        Estacion(
            id = "9",
            nombre = "Javier Prado",
            subtexto = "San Isidro",
            linea = "Línea 1",
            tiempo = "4 min",
            direccion = "Av. Javier Prado con Av. República de Panamá",
            horario = "Lun-Vie: 5:30-23:00, Sáb-Dom: 6:00-22:00",
            servicios = listOf("Ascensores", "Escaleras eléctricas", "Boletería", "WiFi", "Cajero automático")
        ),
        Estacion(
            id = "10",
            nombre = "Miguel Grau",
            subtexto = "Lima",
            linea = "Línea 2",
            tiempo = "En construcción",
            direccion = "Av. Miguel Grau con Av. Abancay",
            horario = "En construcción - Apertura estimada 2026",
            servicios = listOf("En construcción"),
            estado = EstadoEstacion.CERRADO
        )
    )
}
