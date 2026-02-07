package com.example.urlshortner.utils

import java.security.SecureRandom

private val random = SecureRandom()

fun generateRandomString(length: Int = 8): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return buildString(length) {
        repeat(length) {
            append(allowedChars[random.nextInt(allowedChars.size)])
        }
    }
}