package by.cyberpunkfandom.barfrontend.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(dataRepositoriesModule, dataNetworkModule)
}
