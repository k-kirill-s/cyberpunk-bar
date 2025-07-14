package by.cyberpunkfandom.barfrontend.presentation.di

import by.cyberpunkfandom.barfrontend.presentation.cashier.CashierViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.addposition.CashierAddPositionViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra.CashierAddPositionExtraViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.cancelorder.CashierCancelOrderViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.CashierCreateOrderViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.home.CashierHomeViewModel
import by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions.CashierTogglePositionsViewModel
import by.cyberpunkfandom.barfrontend.presentation.main.MainViewModel
import by.cyberpunkfandom.barfrontend.presentation.main.routing.MainRoutingViewModel
import by.cyberpunkfandom.barfrontend.presentation.worker.WorkerViewModel
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.WorkerAuthViewModel
import by.cyberpunkfandom.barfrontend.presentation.worker.home.WorkerHomeViewModel
import by.cyberpunkfandom.barfrontend.presentation.worker.order.WorkerOrderViewModel
import by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails.WorkerPositionDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::MainRoutingViewModel)

    viewModelOf(::CashierViewModel)
    viewModelOf(::CashierHomeViewModel)
    viewModel { parameters ->
        CashierCreateOrderViewModel(
            orderId = parameters.get(),
            ordersRepository = get(),
            positionItemsRepository = get(),
            positionExtraItemsRepository = get(),
        )
    }
    viewModel { parameters ->
        CashierAddPositionViewModel(
            orderId = parameters.get(),
            positionsRepository = get(),
            positionItemsRepository = get(),
        )
    }
    viewModel { parameters ->
        CashierAddPositionExtraViewModel(
            positionItemId = parameters.get(),
            positionExtraRepository = get(),
            positionExtraItemsRepository = get(),
        )
    }
    viewModelOf(::CashierCancelOrderViewModel)
    viewModel { parameters ->
        CashierTogglePositionsViewModel(
            type = parameters.get(),
            positionsRepository = get(),
            positionExtraRepository = get(),
        )
    }

    viewModelOf(::WorkerViewModel)
    viewModelOf(::WorkerAuthViewModel)
    viewModel { parameters ->
        WorkerHomeViewModel(
            workerId = parameters.get(),
            ordersRepository = get(),
            workersRepository = get(),
        )
    }
    viewModel { parameters ->
        WorkerOrderViewModel(
            orderId = parameters.get(),
            ordersRepository = get(),
        )
    }
    viewModel { parameters ->
        WorkerPositionDetailsViewModel(
            positionId = parameters.get(),
            positionsRepository = get(),
        )
    }
}
