package com.example.urlshortner.model.mongodb.repositories

import com.example.urlshortner.model.mongodb.UrlRequests
import com.mongodb.MongoException
import com.mongodb.kotlin.client.MongoDatabase

class UrlRequestsRepository(val mongoDatabase: MongoDatabase) {
    val COLLECTION = "url_requests"

    fun insertOne(value: UrlRequests): Boolean {
        try {
            val result = mongoDatabase.getCollection<UrlRequests>(COLLECTION).insertOne(
                value
            )

            return true
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }

        return false
    }
}
