package com.example.urlshortner.routes

import com.example.urlshortner.model.ShortUrlResponse
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.service.ShortenerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.shortenRoutes() {
    val shortenerService by inject<ShortenerService>()
    post("/shorten") {
        val request = call.receive<URLShortRequest>()
        call.application.environment.log.info("${request.fullUrl} ")
        val shortenedAlias = shortenerService.shortenURL(request.fullUrl, request.customAlias)
        if (shortenedAlias.isEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Invalid input or alias already taken")
        } else {
            call.respond(HttpStatusCode.Created, ShortUrlResponse(shortenedAlias))
        }
    }
}