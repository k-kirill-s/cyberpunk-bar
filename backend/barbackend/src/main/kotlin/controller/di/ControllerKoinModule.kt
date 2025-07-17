package by.cyberpunkfandom.controller.di

import by.cyberpunkfandom.controller.mappers.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val controllerKoinModule = module {
    singleOf(::PositionDtoMapper)
    singleOf(::PositionExtraDtoMapper)
    singleOf(::OrderDtoMapper)
    singleOf(::OrderFullDtoMapper)
    singleOf(::PositionItemDtoMapper)
    singleOf(::PositionExtraItemDtoMapper)
    singleOf(::WorkerDtoMapper)
}
