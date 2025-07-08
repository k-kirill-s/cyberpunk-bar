package by.cyberpunkfandom.barfrontend.di

import by.cyberpunkfandom.barfrontend.data.di.dataModule
import by.cyberpunkfandom.barfrontend.presentation.di.presentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        presentationModule,
        dataModule,
    )
}
