package com.example.urlshortner.service

import com.example.urlshortner.utils.generateRandomString
import java.net.URI

class ShortenerService {

    fun shortenURL(fullURL: String, alias: String): String {
        var shortened = ""
        val fullURI = URI.create(fullURL)
        val host = fullURI.scheme + "://" + fullURI.host
        if (alias.isEmpty()) {
            val alias: String = generateRandomString()
            val uri = URI(host)
            shortened = uri.resolve(alias).toString()
        } else {
            val uri = URI(host)
            shortened = uri.resolve(alias).toString()
        }
        return shortened
    }
}