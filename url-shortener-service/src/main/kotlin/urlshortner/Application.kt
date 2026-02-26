package com.example.urlshortner

import com.example.urlshortner.config.*
import com.example.urlshortner.routes.configureRouting
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.MongoClient
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

object TestModules {
    var extraModules: List<org.koin.core.module.Module> = emptyList()
}

fun Application.module() {
    val clock = Clock.systemUTC()
    val commandLogger = CommandLogger()
    val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(environment.config.propertyOrNull("ktor.mongo.uri")?.getString() ?: throw RuntimeException("Failed to access MongoDB URI.")))
        .addCommandListener(commandLogger)
        .build()
    install(Koin) {
        slf4jLogger()
        allowOverride(true)
        val dbModule = module {
            single { MongoClient.create(settings) }
            single { get<MongoClient>().getDatabase(environment.config.property("ktor.mongo.database").getString()) }
            single<ApplicationEnvironment> { environment }
        }
        modules(listOf(appModule, dbModule) + TestModules.extraModules)
    }
    configureSerialization()
    configureHTTP()
    configureCors()
    configureRouting(clock)
    configureRequestValidation()
    configureStatusPage()

}
