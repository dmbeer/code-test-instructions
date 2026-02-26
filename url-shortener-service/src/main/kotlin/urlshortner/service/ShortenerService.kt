package com.example.urlshortner.service

import com.example.urlshortner.model.UrlRequestsResponse
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.utils.generateRandomString
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.path
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.applicationEnvironment
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.net.URI

class ShortenerService(val urlRequestsRepository: UrlRequestsRepository,
                       val customAliasRepository: CustomAliasRepository, val environment: ApplicationEnvironment ) {

    fun shortenURL(fullURL: String, alias: String): String {
        val ktorhost = environment.config.property("ktor.deployment.host").getString()
        val port = environment.config.property("ktor.deployment.port").getString().toInt()
        val baseUrl = "$ktorhost:$port"
        var shortened = ""
        if (alias.isEmpty()) {
            val alias: String = generateRandomString()
            shortened = URLBuilder().apply {
                protocol = URLProtocol.HTTP
                host = baseUrl
                path("/$alias")
            }.buildString()
            urlRequestsRepository.insertOne(UrlRequests(null, fullURL, alias, shortened))
        } else {
            shortened = URLBuilder().apply {
                protocol = URLProtocol.HTTP
                host = baseUrl
                path("/$alias")
            }.buildString()
            urlRequestsRepository.insertOne(UrlRequests(null, fullURL, alias, shortened))
            customAliasRepository.insertOne(CustomAlias(null, alias))
        }
        return shortened
    }

    fun listAllShortenedUrls(): List<UrlRequestsResponse> {
        return urlRequestsRepository.findAll().let { list ->
            list.map { urlRequest -> urlRequest.toResponse() }
        }.toList()
    }

    fun resolve(alias: String): String? {
        return urlRequestsRepository.findByAlias(alias)?.fullUrl
    }

    fun deleteAlias(alias: String): Boolean {
        var found = false
        val customAlias = customAliasRepository.findByAlias(alias)
        if (customAlias != null) {
            found = true
            urlRequestsRepository.deleteByAlias(alias)
            customAliasRepository.deleteByAlias(alias)
        } else {
            val urlrequst = urlRequestsRepository.findByAlias(alias)
            if (urlrequst != null) {
                found = true
                urlRequestsRepository.deleteByAlias(alias)
            }
        }
        return found
    }
}