package by.cyberpunkfandom.data.di

import by.cyberpunkfandom.data.mappers.*
import by.cyberpunkfandom.data.repository.*
import by.cyberpunkfandom.domain.repository.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataKoinModule = module {
    singleOf(::PositionMapper)
    singleOf(::PositionExtraMapper)
    singleOf(::DiscountMapper)
    singleOf(::OrderMapper)
    singleOf(::OrderFullMapper)
    singleOf(::PositionItemMapper)
    singleOf(::PositionExtraItemMapper)

    singleOf(::PositionsRepositoryImpl) bind PositionsRepository::class
    singleOf(::PositionExtraRepositoryImpl) bind PositionExtraRepository::class
    singleOf(::DiscountsRepositoryImpl) bind DiscountsRepository::class
    singleOf(::OrdersRepositoryImpl) bind OrdersRepository::class
    singleOf(::PositionItemsRepositoryImpl) bind PositionItemsRepository::class
    singleOf(::PositionExtraItemsRepositoryImpl) bind PositionExtraItemsRepository::class
    singleOf(::OrderDiscountsRepositoryImpl) bind OrderDiscountsRepository::class
}
