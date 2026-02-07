package com.example.urlshortner

import com.example.urlshortner.config.configureDI
import com.example.urlshortner.config.configureHTTP
import com.example.urlshortner.config.configureRequestValidation
import com.example.urlshortner.config.configureSerialization
import com.example.urlshortner.config.configureStatusPage
import com.example.urlshortner.routes.configureRouting
import com.example.urlshortner.service.ShortenerService
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.di.dependencies
import java.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val clock = Clock.systemUTC()
    dependencies {
        provide(ShortenerService::class)
    }
    configureSerialization()
    configureHTTP()
    configureRouting(clock, shortenerService = ShortenerService())
    configureRequestValidation()
    configureStatusPage()
}
