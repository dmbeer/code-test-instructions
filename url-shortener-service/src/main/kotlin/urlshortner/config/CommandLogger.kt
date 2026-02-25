package com.example.urlshortner.config

import com.mongodb.event.CommandFailedEvent
import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent
import io.ktor.util.logging.*

class CommandLogger : CommandListener {
    internal val logger = KtorSimpleLogger("com.example.urlshortner.config")

    @Synchronized
    override fun commandFailed(event: CommandFailedEvent) {
        logger.error("Failed execution of command '${event.commandName}' with id ${event.requestId}")
    }

    @Synchronized
    override fun commandStarted(event: CommandStartedEvent) {
        logger.info("Started command '${event.commandName}'")
        logger.info("event document: '${event.command}")
    }
}