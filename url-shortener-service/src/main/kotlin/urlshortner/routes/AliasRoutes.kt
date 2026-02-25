package com.example.urlshortner.routes

import com.example.urlshortner.service.ShortenerService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.aliasRoutes() {
    val shortenerService by inject<ShortenerService>()
    delete("/{alias}") {
        val result = shortenerService.deleteAlias(call.parameters["alias"] as String)
        if (result) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}