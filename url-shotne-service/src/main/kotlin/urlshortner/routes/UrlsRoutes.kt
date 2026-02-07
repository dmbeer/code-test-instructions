package com.example.urlshortner.routes

import com.example.urlshortner.service.ShortenerService
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.urlsRoutes() {
    val shortenerService by inject<ShortenerService>()
    get("/urls") {
        call.respond(shortenerService.listAllShortenedUrls())
    }
}