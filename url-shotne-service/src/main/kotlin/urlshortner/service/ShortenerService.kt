package com.example.urlshortner.service

import com.example.urlshortner.utils.generateRandomString
import java.net.URI

class ShortenerService {

    fun shortenURL(fullURL: String, alias: String): String {
        var shortened = ""
        if (alias.isEmpty()) {
            val alias: String = generateRandomString()
            val fullURI = URI.create(fullURL)
            shortened = fullURI.resolve(alias).toString()
        }
        return shortened
    }
}