package com.example.urlshortner.config

import com.example.urlshortner.model.URLShortRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import java.net.URI

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<URLShortRequest> { request ->
//            if (uRLShortRequest.fullUrl.isEmpty()) {
//                ValidationResult.Invalid("A URL is required to Shorten")
//            } else ValidationResult.Valid
            val errors = mutableListOf<String>()

            when {
                request.fullUrl.isBlank() ->
                    errors.add("A URL is required to shorten")
                !request.fullUrl.startsWith("http://") && !request.fullUrl.startsWith("https://") ->
                    errors.add("URL must start with http:// or https://")
                else -> runCatching {
                    val uri = URI(request.fullUrl)
                    if (!uri.isAbsolute) errors.add("URL must be absolute")
                    if (uri.host == null) errors.add("URL must contain a valid host")
                }.onFailure {
                    errors.add("URL is not valid")
                }
            }

            // customAlias validation
            if (request.customAlias.isNotEmpty()) {
                when {
                    request.customAlias.length < 3 ->
                        errors.add("Custom alias must be at least 3 characters")
                    request.customAlias.length > 20 ->
                        errors.add("Custom alias must not exceed 20 characters")
                    !request.customAlias.matches(Regex("^[a-zA-Z0-9-_]+$")) ->
                        errors.add("Custom alias must only contain letters, numbers, hyphens and underscores")
                }
            }

            if (errors.isEmpty()) ValidationResult.Valid
            else ValidationResult.Invalid(errors.joinToString(", "))
        }
    }
}