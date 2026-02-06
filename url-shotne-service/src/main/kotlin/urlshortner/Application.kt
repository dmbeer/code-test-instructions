package com.example.urlshortner

import com.example.urlshortner.config.configureHTTP
import com.example.urlshortner.config.configureRequestValidation
import com.example.urlshortner.config.configureSerialization
import com.example.urlshortner.config.configureStatusPage
import com.example.urlshortner.routes.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*
import java.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val clock = Clock.systemUTC()
    configureSerialization()
    configureHTTP()
    configureRouting(clock)
    configureRequestValidation()
    configureStatusPage()
}
