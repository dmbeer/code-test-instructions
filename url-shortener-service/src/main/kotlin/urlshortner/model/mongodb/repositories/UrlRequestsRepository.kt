package com.example.urlshortner.model.mongodb.repositories

import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
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

    fun findAll() = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find().toList()

    fun findByAlias(alias: String): UrlRequests? {
        val result = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find(Filters.eq("alias", alias))
        return result.firstOrNull()
    }

    fun deleteByAlias(alias: String): Boolean {
        try {
            mongoDatabase.getCollection<UrlRequests>(COLLECTION).deleteOne(Filters.eq("alias", alias))
            return true
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
        }

        return false
    }

    fun findByFullUrl(fullURL: String) : UrlRequests? {
        val result = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find(Filters.eq("fullUrl", fullURL))
        return result.firstOrNull()
    }

}
