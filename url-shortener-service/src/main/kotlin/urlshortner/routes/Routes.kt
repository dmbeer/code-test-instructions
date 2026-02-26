package com.example.urlshortner.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.time.Clock

fun Application.configureRouting(clock: Clock) {
    routing {
        shortenRoutes()
        urlsRoutes()
        aliasRoutes()
    }
}