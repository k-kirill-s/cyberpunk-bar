package by.cyberpunkfandom.controller.di

import by.cyberpunkfandom.controller.mappers.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val controllerKoinModule = module {
    singleOf(::PositionDtoMapper)
    singleOf(::PositionVariantDtoMapper)
    singleOf(::OrderDtoMapper)
    singleOf(::OrderFullDtoMapper)
    singleOf(::PositionItemDtoMapper)
    singleOf(::WorkerDtoMapper)
}
