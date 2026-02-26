package com.example.urlshortner.model.mongodb.repositories

import com.example.urlshortner.model.mongodb.CustomAlias
import com.example.urlshortner.model.mongodb.UrlRequests
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.ClientSession
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.util.valuesOf

class UrlRequestsRepository(val mongoDatabase: MongoDatabase) {
    val COLLECTION = "url_requests"

    fun insertOne(value: UrlRequests) : Boolean {
        return insertOne(null, value)
    }

    fun insertOne(session: ClientSession?, value: UrlRequests): Boolean {
        return try {
            val collection = mongoDatabase.getCollection<UrlRequests>(COLLECTION)
            if (session != null) {
                collection.insertOne(session, value)
            } else {
                collection.insertOne(value)
            }
            true
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
            false
        }
    }

    fun findAll() = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find().toList()

    fun findByAlias(alias: String): UrlRequests? {
        val result = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find(Filters.eq("alias", alias))
        return result.firstOrNull()
    }

    fun deleteByAlias(alias: String): Boolean {
        return deleteByAlias(null, alias)
    }
    fun deleteByAlias(session: ClientSession?, alias: String): Boolean {
        return try {
            val collection = mongoDatabase.getCollection<UrlRequests>(COLLECTION)
            if (session != null) {
                collection.deleteOne(session, Filters.eq("alias", alias))
            } else {
                collection.deleteOne(Filters.eq("alias", alias))
            }
            true
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
            false
        }
    }

    fun findByFullUrl(fullURL: String) : UrlRequests? {
        val result = mongoDatabase.getCollection<UrlRequests>(COLLECTION).find(Filters.eq("fullUrl", fullURL))
        return result.firstOrNull()
    }

}
