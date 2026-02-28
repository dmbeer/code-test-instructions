package com.example.urlshortner.routes

import com.mongodb.MongoException
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.bson.Document
import org.koin.ktor.ext.getKoin
import java.time.Clock

fun Application.configureRouting(clock: Clock) {
    routing {
        get("/health") {
            val db = getKoin().get<MongoDatabase>()
            try {
                db.runCommand(Document("ping", 1))
                call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
            } catch (e: MongoException) {
                call.respond(HttpStatusCode.ServiceUnavailable, mapOf("status" to "unhealthy"))
            }
        }
        shortenRoutes()
        urlsRoutes()
        aliasRoutes()
    }
}