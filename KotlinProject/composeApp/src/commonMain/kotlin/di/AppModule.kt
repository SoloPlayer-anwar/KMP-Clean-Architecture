package di

import data.ExpenseManager
import data.ExpenseRepoImpl
import domain.ExpenseRepository
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import presenter.ExpenseViewModel

fun appModule() = module {
    single<ExpenseRepository> { ExpenseRepoImpl(expenseManager = get()) }
    single { ExpenseManager }.withOptions { createdAtStart() }
    factory { ExpenseViewModel(get()) }
}