package com.example.urlshortner.model.mongodb.repositories

import com.example.urlshortner.model.mongodb.CustomAlias
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.ClientSession
import com.mongodb.kotlin.client.MongoDatabase

class CustomAliasRepository(val mongoDatabase: MongoDatabase) {
    val COLLECTION = "custom_alias"

    fun insertOne(value: CustomAlias): Boolean {
        return insertOne(null, value)
    }

    fun insertOne(session: ClientSession?, value: CustomAlias): Boolean {
        return try {
            val collection = mongoDatabase.getCollection<CustomAlias>(COLLECTION)
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

    fun findByAlias(alias: String): CustomAlias? {
        val result = mongoDatabase.getCollection<CustomAlias>(COLLECTION).find(Filters.eq("alias", alias))
        return result.firstOrNull()
    }

    fun deleteByAlias(alias: String): Boolean {
        return deleteByAlias(null, alias)
    }
    fun deleteByAlias(session: ClientSession?, alias: String): Boolean {
        return try {
            val collection = mongoDatabase.getCollection<CustomAlias>(COLLECTION)
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
}
