package com.example.urlshortner.config

import com.example.urlshortner.model.exceptions.ExceptionResponse
import com.example.urlshortner.model.exceptions.ParsingException
import com.example.urlshortner.model.exceptions.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is RequestValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse(throwable.localizedMessage, throwable.cause.toString())
                    )
                }
                is ValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse(throwable.message, throwable.stackTraceToString())
                    )
                }
                is ParsingException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ExceptionResponse(throwable.message, throwable.cause.toString())
                    )
                }
                is BadRequestException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse(throwable.localizedMessage, throwable.cause.toString())
                    )
                }
            }
        }
    }
}