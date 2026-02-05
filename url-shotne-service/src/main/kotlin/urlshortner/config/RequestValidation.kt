package com.example.urlshortner.config

import com.example.urlshortner.model.URLShortRequest
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<URLShortRequest> { uRLShortRequest ->
            if (uRLShortRequest.fullUrl.isEmpty()) {
                ValidationResult.Invalid("A URL is required to Shorten")
            } else ValidationResult.Valid
        }
    }
}