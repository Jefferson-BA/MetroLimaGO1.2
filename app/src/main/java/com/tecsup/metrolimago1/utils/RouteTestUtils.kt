package com.tecsup.metrolimago1.utils

import com.tecsup.metrolimago1.domain.services.RouteCalculationService

object RouteTestUtils {
    
    fun testRouteCalculations() {
        println("=== PRUEBAS DE CÁLCULO DE RUTAS DEL METRO DE LIMA ===")
        
        // Prueba 1: Misma línea (Línea 1)
        println("\n1. Ruta en la misma línea (Línea 1):")
        val tiempo1 = RouteCalculationService.calcularTiempoEstimado("Villa El Salvador", "San Borja")
        val pasos1 = RouteCalculationService.generarPasosRecorrido("Villa El Salvador", "San Borja")
        println("Tiempo estimado: $tiempo1 minutos")
        println("Pasos del recorrido:")
        pasos1.forEach { paso ->
            val transferencia = if (paso.esTransferencia) " (TRANSFERENCIA)" else ""
            println("  - ${paso.estacion} (${paso.linea})$transferencia")
        }
        
        // Prueba 2: Misma línea (Línea 2)
        println("\n2. Ruta en la misma línea (Línea 2):")
        val tiempo2 = RouteCalculationService.calcularTiempoEstimado("Ate Vitarte", "Miraflores")
        val pasos2 = RouteCalculationService.generarPasosRecorrido("Ate Vitarte", "Miraflores")
        println("Tiempo estimado: $tiempo2 minutos")
        println("Pasos del recorrido:")
        pasos2.forEach { paso ->
            val transferencia = if (paso.esTransferencia) " (TRANSFERENCIA)" else ""
            println("  - ${paso.estacion} (${paso.linea})$transferencia")
        }
        
        // Prueba 3: Diferentes líneas (con transferencia)
        println("\n3. Ruta entre diferentes líneas (con transferencia):")
        val tiempo3 = RouteCalculationService.calcularTiempoEstimado("Villa El Salvador", "Ate Vitarte")
        val pasos3 = RouteCalculationService.generarPasosRecorrido("Villa El Salvador", "Ate Vitarte")
        println("Tiempo estimado: $tiempo3 minutos")
        println("Pasos del recorrido:")
        pasos3.forEach { paso ->
            val transferencia = if (paso.esTransferencia) " (TRANSFERENCIA)" else ""
            println("  - ${paso.estacion} (${paso.linea})$transferencia")
        }
        
        // Prueba 4: Ruta corta
        println("\n4. Ruta corta (2 estaciones):")
        val tiempo4 = RouteCalculationService.calcularTiempoEstimado("Central", "San Borja")
        val pasos4 = RouteCalculationService.generarPasosRecorrido("Central", "San Borja")
        println("Tiempo estimado: $tiempo4 minutos")
        println("Pasos del recorrido:")
        pasos4.forEach { paso ->
            val transferencia = if (paso.esTransferencia) " (TRANSFERENCIA)" else ""
            println("  - ${paso.estacion} (${paso.linea})$transferencia")
        }
        
        // Prueba 5: Misma estación
        println("\n5. Misma estación:")
        val tiempo5 = RouteCalculationService.calcularTiempoEstimado("Central", "Central")
        val pasos5 = RouteCalculationService.generarPasosRecorrido("Central", "Central")
        println("Tiempo estimado: $tiempo5 minutos")
        println("Pasos del recorrido:")
        pasos5.forEach { paso ->
            val transferencia = if (paso.esTransferencia) " (TRANSFERENCIA)" else ""
            println("  - ${paso.estacion} (${paso.linea})$transferencia")
        }
        
        println("\n=== FIN DE PRUEBAS ===")
    }
}
