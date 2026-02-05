package com.example.urlshortner

import com.example.urlshortner.config.configureHTTP
import com.example.urlshortner.config.configureRouting
import com.example.urlshortner.config.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureRouting()
}
