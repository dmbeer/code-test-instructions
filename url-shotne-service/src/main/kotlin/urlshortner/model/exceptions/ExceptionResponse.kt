package com.example.urlshortner.model.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponse(
    val message: String,
    val detailedMessage: String,
)