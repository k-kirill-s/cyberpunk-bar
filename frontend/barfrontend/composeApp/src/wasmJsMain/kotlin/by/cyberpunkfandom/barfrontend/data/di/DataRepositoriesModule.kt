package by.cyberpunkfandom.barfrontend.data.di

import by.cyberpunkfandom.barfrontend.data.mappers.AdminAnalyticsMapper
import by.cyberpunkfandom.barfrontend.data.mappers.OrderFullMapper
import by.cyberpunkfandom.barfrontend.data.mappers.OrderMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionItemMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionVariantMapper
import by.cyberpunkfandom.barfrontend.data.mappers.WorkerMapper
import by.cyberpunkfandom.barfrontend.data.repositories.AdminRepository
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionVariantsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataRepositoriesModule = module {
    singleOf(::AdminAnalyticsMapper)
    singleOf(::PositionMapper)
    singleOf(::PositionItemMapper)
    singleOf(::PositionVariantMapper)
    singleOf(::OrderMapper)
    singleOf(::OrderFullMapper)
    singleOf(::WorkerMapper)

    singleOf(::AdminRepository)
    singleOf(::OrdersRepository)
    singleOf(::PositionItemsRepository)
    singleOf(::PositionsRepository)
    singleOf(::PositionVariantsRepository)
    singleOf(::WorkersRepository)
}
