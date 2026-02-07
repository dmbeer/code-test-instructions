package com.example.urlshortner.service

import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.utils.generateRandomString
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
}