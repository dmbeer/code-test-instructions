package com.example.urlshortner.model

import kotlinx.serialization.Serializable

@Serializable
data class UrlRequestsResponse(
    val alias: String,
    val fullUrl: String,
    val shortUrl: String,
) {
}