package com.example.urlshortner.service

import com.example.urlshortner.model.UrlRequestsResponse
import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.utils.generateRandomString
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.net.URI

class ShortenerService(val urlRequestsRepository: UrlRequestsRepository, val customAliasRepository: CustomAliasRepository) {

    fun shortenURL(fullURL: String, alias: String): String {
        var shortened = ""
        val fullURI = URI.create(fullURL)
        val host = fullURI.scheme + "://" + fullURI.host
        if (alias.isEmpty()) {
            val alias: String = generateRandomString()
            val uri = URI(host)
            shortened = uri.resolve(alias).toString()
            urlRequestsRepository.insertOne(UrlRequests(null, fullURL, alias, shortened))
        } else {
            val uri = URI(host)
            shortened = uri.resolve(alias).toString()
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