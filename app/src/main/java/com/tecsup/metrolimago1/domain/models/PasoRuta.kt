package com.tecsup.metrolimago1.domain.models

data class PasoRuta(
    val estacion: String,
    val linea: String,
    val esTransferencia: Boolean = false
)
