package com.example.urlshortner.routes

import com.example.urlshortner.service.ShortenerService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.time.Clock

fun Application.configureRouting(clock: Clock, shortenerService: ShortenerService) {
    routing {
        shortenRoutes(shortenerService)
    }
}