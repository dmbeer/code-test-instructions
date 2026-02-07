package com.example.urlshortner.model.mongodb

import org.bson.codecs.pojo.annotations.BsonId

data class CustomAlias(
    @BsonId val id: String ? = null,
    val alias: String
)
