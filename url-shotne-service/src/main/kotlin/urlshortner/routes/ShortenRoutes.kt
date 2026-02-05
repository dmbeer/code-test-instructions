package com.example.urlshortner.routes

import com.example.urlshortner.model.URLShortRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.shortenRoutes() {

    post("/shorten") {
        val request = call.receive<URLShortRequest>()
        call.application.environment.log.info("${request.fullUrl} ")
        call.respond(HttpStatusCode.OK)
    }
}