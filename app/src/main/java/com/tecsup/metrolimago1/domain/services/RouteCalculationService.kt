package com.tecsup.metrolimago1.domain.services

import com.tecsup.metrolimago1.data.local.MetroLimaStations
import com.tecsup.metrolimago1.domain.models.PasoRuta
import com.tecsup.metrolimago1.domain.models.Station

object RouteCalculationService {
    
    /**
     * Calcula el tiempo estimado del recorrido entre dos estaciones
     * @param origen Nombre de la estación de origen
     * @param destino Nombre de la estación de destino
     * @return Tiempo estimado en minutos
     */
    fun calcularTiempoEstimado(origen: String, destino: String): Int {
        val estacionOrigen = MetroLimaStations.findStationByName(origen)
        val estacionDestino = MetroLimaStations.findStationByName(destino)
        
        if (estacionOrigen == null || estacionDestino == null) {
            return 0
        }
        
        // Si es la misma estación
        if (estacionOrigen.name == estacionDestino.name) {
            return 0
        }
        
        // Si están en la misma línea
        if (estacionOrigen.line == estacionDestino.line) {
            val estacionesIntermedias = contarEstacionesIntermedias(estacionOrigen, estacionDestino)
            return estacionesIntermedias * 2 // 2 minutos por estación
        }
        
        // Si están en líneas diferentes, necesitamos transferencia
        val estacionesHastaTransferencia = contarEstacionesHastaTransferencia(estacionOrigen)
        val estacionesDesdeTransferencia = contarEstacionesDesdeTransferencia(estacionDestino)
        
        val tiempoTotal = (estacionesHastaTransferencia + estacionesDesdeTransferencia) * 2 + 3 // +3 minutos por transferencia
        
        return tiempoTotal
    }
    
    /**
     * Genera la lista de pasos del recorrido entre dos estaciones
     * @param origen Nombre de la estación de origen
     * @param destino Nombre de la estación de destino
     * @return Lista de pasos del recorrido
     */
    fun generarPasosRecorrido(origen: String, destino: String): List<PasoRuta> {
        val estacionOrigen = MetroLimaStations.findStationByName(origen)
        val estacionDestino = MetroLimaStations.findStationByName(destino)
        
        if (estacionOrigen == null || estacionDestino == null) {
            return emptyList()
        }
        
        // Si es la misma estación
        if (estacionOrigen.name == estacionDestino.name) {
            return listOf(
                PasoRuta(
                    estacion = estacionOrigen.name,
                    linea = estacionOrigen.line,
                    esTransferencia = false
                )
            )
        }
        
        // Si están en la misma línea
        if (estacionOrigen.line == estacionDestino.line) {
            return generarPasosMismaLinea(estacionOrigen, estacionDestino)
        }
        
        // Si están en líneas diferentes, incluir transferencia
        return generarPasosConTransferencia(estacionOrigen, estacionDestino)
    }
    
    private fun contarEstacionesIntermedias(origen: Station, destino: Station): Int {
        val estacionesLinea = MetroLimaStations.getStationsByLine(origen.line)
        val indiceOrigen = estacionesLinea.indexOfFirst { it.name == origen.name }
        val indiceDestino = estacionesLinea.indexOfFirst { it.name == destino.name }
        
        if (indiceOrigen == -1 || indiceDestino == -1) return 0
        
        return kotlin.math.abs(indiceDestino - indiceOrigen)
    }
    
    private fun contarEstacionesHastaTransferencia(estacion: Station): Int {
        val estacionesLinea = MetroLimaStations.getStationsByLine(estacion.line)
        val indiceEstacion = estacionesLinea.indexOfFirst { it.name == estacion.name }
        val indiceTransferencia = estacionesLinea.indexOfFirst { 
            MetroLimaStations.isTransferStation(it.name) 
        }
        
        if (indiceEstacion == -1 || indiceTransferencia == -1) return 0
        
        return kotlin.math.abs(indiceTransferencia - indiceEstacion)
    }
    
    private fun contarEstacionesDesdeTransferencia(estacion: Station): Int {
        val estacionesLinea = MetroLimaStations.getStationsByLine(estacion.line)
        val indiceEstacion = estacionesLinea.indexOfFirst { it.name == estacion.name }
        val indiceTransferencia = estacionesLinea.indexOfFirst { 
            MetroLimaStations.isTransferStation(it.name) 
        }
        
        if (indiceEstacion == -1 || indiceTransferencia == -1) return 0
        
        return kotlin.math.abs(indiceEstacion - indiceTransferencia)
    }
    
    private fun generarPasosMismaLinea(origen: Station, destino: Station): List<PasoRuta> {
        val estacionesLinea = MetroLimaStations.getStationsByLine(origen.line)
        val indiceOrigen = estacionesLinea.indexOfFirst { it.name == origen.name }
        val indiceDestino = estacionesLinea.indexOfFirst { it.name == destino.name }
        
        if (indiceOrigen == -1 || indiceDestino == -1) return emptyList()
        
        val pasos = mutableListOf<PasoRuta>()
        
        val direccion = if (indiceDestino > indiceOrigen) 1 else -1
        var indiceActual = indiceOrigen
        
        while (indiceActual != indiceDestino) {
            val estacion = estacionesLinea[indiceActual]
            pasos.add(
                PasoRuta(
                    estacion = estacion.name,
                    linea = estacion.line,
                    esTransferencia = MetroLimaStations.isTransferStation(estacion.name)
                )
            )
            indiceActual += direccion
        }
        
        // Agregar la estación destino
        val estacionDestino = estacionesLinea[indiceDestino]
        pasos.add(
            PasoRuta(
                estacion = estacionDestino.name,
                linea = estacionDestino.line,
                esTransferencia = MetroLimaStations.isTransferStation(estacionDestino.name)
            )
        )
        
        return pasos
    }
    
    private fun generarPasosConTransferencia(origen: Station, destino: Station): List<PasoRuta> {
        val pasos = mutableListOf<PasoRuta>()
        
        // Pasos desde origen hasta transferencia
        val pasosHastaTransferencia = generarPasosHastaTransferencia(origen)
        pasos.addAll(pasosHastaTransferencia)
        
        // Pasos desde transferencia hasta destino
        val pasosDesdeTransferencia = generarPasosDesdeTransferencia(destino)
        pasos.addAll(pasosDesdeTransferencia)
        
        return pasos
    }
    
    private fun generarPasosHastaTransferencia(estacion: Station): List<PasoRuta> {
        val estacionesLinea = MetroLimaStations.getStationsByLine(estacion.line)
        val indiceEstacion = estacionesLinea.indexOfFirst { it.name == estacion.name }
        val indiceTransferencia = estacionesLinea.indexOfFirst { 
            MetroLimaStations.isTransferStation(it.name) 
        }
        
        if (indiceEstacion == -1 || indiceTransferencia == -1) return emptyList()
        
        val pasos = mutableListOf<PasoRuta>()
        val direccion = if (indiceTransferencia > indiceEstacion) 1 else -1
        var indiceActual = indiceEstacion
        
        while (indiceActual != indiceTransferencia) {
            val estacionActual = estacionesLinea[indiceActual]
            pasos.add(
                PasoRuta(
                    estacion = estacionActual.name,
                    linea = estacionActual.line,
                    esTransferencia = false
                )
            )
            indiceActual += direccion
        }
        
        // Agregar la estación de transferencia
        val estacionTransferencia = estacionesLinea[indiceTransferencia]
        pasos.add(
            PasoRuta(
                estacion = estacionTransferencia.name,
                linea = estacionTransferencia.line,
                esTransferencia = true
            )
        )
        
        return pasos
    }
    
    private fun generarPasosDesdeTransferencia(estacion: Station): List<PasoRuta> {
        val estacionesLinea = MetroLimaStations.getStationsByLine(estacion.line)
        val indiceEstacion = estacionesLinea.indexOfFirst { it.name == estacion.name }
        val indiceTransferencia = estacionesLinea.indexOfFirst { 
            MetroLimaStations.isTransferStation(it.name) 
        }
        
        if (indiceEstacion == -1 || indiceTransferencia == -1) return emptyList()
        
        val pasos = mutableListOf<PasoRuta>()
        val direccion = if (indiceEstacion > indiceTransferencia) 1 else -1
        var indiceActual = indiceTransferencia + direccion // Empezar después de la transferencia
        
        while (indiceActual != indiceEstacion) {
            val estacionActual = estacionesLinea[indiceActual]
            pasos.add(
                PasoRuta(
                    estacion = estacionActual.name,
                    linea = estacionActual.line,
                    esTransferencia = false
                )
            )
            indiceActual += direccion
        }
        
        // Agregar la estación destino
        val estacionDestino = estacionesLinea[indiceEstacion]
        pasos.add(
            PasoRuta(
                estacion = estacionDestino.name,
                linea = estacionDestino.line,
                esTransferencia = false
            )
        )
        
        return pasos
    }
}
