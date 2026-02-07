package com.example.urlshortner.config

import com.example.urlshortner.model.mongodb.UrlRequests
import com.example.urlshortner.model.mongodb.repositories.UrlRequestsRepository
import com.example.urlshortner.service.ShortenerService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// Constructor DSL
val appModule = module {
//    singleOf(::ZoneRepositoryImpl) { bind<BaseRepository<Zone>>() }
//    singleOf(::TrackIdRepositoryImpl) {bind<BaseRepository<TrackID>>()}
//    singleOf(::StationRepositoryImpl) {bind<BaseRepository<Station>>()}
//    singleOf(::HazardCodesRepositoryImpl) {bind<BaseRepository<HazardCode>>()}
//    singleOf(::ELRLookupRepositoryImpl) {bind<BaseRepository<ELRLookup>>()}
    single { UrlRequestsRepository(get()) }
    single { ShortenerService(get()) }
}