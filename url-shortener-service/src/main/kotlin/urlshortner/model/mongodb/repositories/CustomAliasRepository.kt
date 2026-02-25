package com.example.urlshortner.model.mongodb.repositories

import com.example.urlshortner.model.mongodb.CustomAlias
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoDatabase

class CustomAliasRepository(val mongoDatabase: MongoDatabase) {
    val COLLECTION = "custom_alias"

    fun insertOne(value: CustomAlias): Boolean {
        try {
            val result = mongoDatabase.getCollection<CustomAlias>(COLLECTION).insertOne(
                value
            )

            return true
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }

        return false
    }

    fun findByAlias(alias: String): CustomAlias? {
        val result = mongoDatabase.getCollection<CustomAlias>(COLLECTION).find(Filters.eq("alias", alias))
        return result.firstOrNull()
    }

    fun deleteByAlias(alias: String): Boolean {
        try {
            mongoDatabase.getCollection<CustomAlias>(COLLECTION).deleteOne(Filters.eq("alias", alias))
            return true
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
        }

        return false
    }
}
