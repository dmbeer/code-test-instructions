package com.example.urlshortner.config

import com.example.urlshortner.model.mongodb.repositories.CustomAliasRepository
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.service.ShortenerService
import org.koin.dsl.module

// Constructor DSL
val appModule = module {
    single { UrlRequestsRepository(get()) }
    single { CustomAliasRepository(get()) }
    single { ShortenerService(get(), get(), get(), get()) }
}