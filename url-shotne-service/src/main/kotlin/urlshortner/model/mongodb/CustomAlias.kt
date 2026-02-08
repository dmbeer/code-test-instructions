package com.example.urlshortner.model.mongodb

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CustomAlias(
    @BsonId
    val id: ObjectId ? = null,
    val alias: String
)
