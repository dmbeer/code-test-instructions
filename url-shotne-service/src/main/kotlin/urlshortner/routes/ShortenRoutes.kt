package com.example.urlshortner.routes

import com.example.urlshortner.model.ShortUrlResponse
import com.example.urlshortner.model.URLShortRequest
import com.example.urlshortner.service.ShortenerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.shortenRoutes(shortenerService: ShortenerService) {

    post("/shorten") {
        val request = call.receive<URLShortRequest>()
        call.application.environment.log.info("${request.fullUrl} ")
        val shortenedAlias = shortenerService.shortenURL(request.fullUrl, request.customAlias)
        call.respond(HttpStatusCode.OK, ShortUrlResponse(shortenedAlias))
    }
}