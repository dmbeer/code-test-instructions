package com.example.urlshortner.model

import kotlinx.serialization.Serializable

@Serializable
data class ShortUrlResponse(
    val shortUrl: String,
)