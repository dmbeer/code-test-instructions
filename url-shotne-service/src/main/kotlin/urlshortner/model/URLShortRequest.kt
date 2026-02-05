package com.example.urlshortner.model

import kotlinx.serialization.Serializable

@Serializable
data class URLShortRequest(
    val fullUrl: String,
    val customAlias: String = ""
)
