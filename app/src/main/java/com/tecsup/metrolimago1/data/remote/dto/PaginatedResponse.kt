package com.tecsup.metrolimago1.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta paginada de Django REST Framework
 */
data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

