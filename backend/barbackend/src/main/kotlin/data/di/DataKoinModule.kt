package by.cyberpunkfandom.data.di

import by.cyberpunkfandom.data.mappers.*
import by.cyberpunkfandom.data.repository.*
import by.cyberpunkfandom.domain.repository.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataKoinModule = module {
    singleOf(::PositionMapper)
    singleOf(::PositionVariantMapper)
    singleOf(::OrderMapper)
    singleOf(::OrderFullMapper)
    singleOf(::PositionItemMapper)
    singleOf(::WorkerMapper)

    singleOf(::PositionsRepositoryImpl) bind PositionsRepository::class
    singleOf(::PositionVariantsRepositoryImpl) bind PositionVariantsRepository::class
    singleOf(::OrdersRepositoryImpl) bind OrdersRepository::class
    singleOf(::PositionItemsRepositoryImpl) bind PositionItemsRepository::class
    singleOf(::WorkersRepositoryImpl) bind WorkersRepository::class
    singleOf(::AdminAnalyticsRepositoryImpl) bind AdminAnalyticsRepository::class
}
