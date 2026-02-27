package com.example.urlshortner.config

import com.example.urlshortner.model.exceptions.ExceptionResponse
import com.example.urlshortner.model.exceptions.ParsingException
import com.example.urlshortner.model.exceptions.ValidationException
import com.mongodb.MongoException
import com.mongodb.MongoSocketException
import com.mongodb.MongoSocketOpenException
import com.mongodb.MongoTimeoutException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<MongoTimeoutException> { call, cause ->
            call.respond(HttpStatusCode.ServiceUnavailable, mapOf("status" to "unhealthy", "error" to cause.message))
        }
        exception<MongoException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, mapOf("status" to "error", "error" to cause.message))
        }
        exception< MongoSocketOpenException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, mapOf("status" to "error", "error" to cause.message))
        }
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is RequestValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse(throwable.reasons.joinToString(", "), throwable.cause.toString())
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