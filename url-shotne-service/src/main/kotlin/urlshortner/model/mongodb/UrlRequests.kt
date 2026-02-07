package com.example.urlshortner.model.mongodb

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UrlRequests(
    @BsonId
    val id: ObjectId ? = null,
    val fullUrl: String,
    val alias: String,
    val shortUrl: String,
) {
}