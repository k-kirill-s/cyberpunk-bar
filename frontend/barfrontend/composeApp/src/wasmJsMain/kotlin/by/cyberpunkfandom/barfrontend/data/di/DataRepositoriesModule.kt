package by.cyberpunkfandom.barfrontend.data.di

import by.cyberpunkfandom.barfrontend.data.mappers.OrderFullMapper
import by.cyberpunkfandom.barfrontend.data.mappers.OrderMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionExtraItemMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionExtraMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionItemMapper
import by.cyberpunkfandom.barfrontend.data.mappers.PositionMapper
import by.cyberpunkfandom.barfrontend.data.mappers.WorkerMapper
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionExtraItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionExtraRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataRepositoriesModule = module {
    singleOf(::PositionMapper)
    singleOf(::PositionItemMapper)
    singleOf(::PositionExtraMapper)
    singleOf(::PositionExtraItemMapper)
    singleOf(::OrderMapper)
    singleOf(::OrderFullMapper)
    singleOf(::WorkerMapper)

    singleOf(::OrdersRepository)
    singleOf(::PositionItemsRepository)
    singleOf(::PositionExtraItemsRepository)
    singleOf(::PositionsRepository)
    singleOf(::PositionExtraRepository)
    singleOf(::WorkersRepository)
}
