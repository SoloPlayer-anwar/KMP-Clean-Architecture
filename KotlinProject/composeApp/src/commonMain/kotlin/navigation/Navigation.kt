package navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import data.ExpenseManager
import data.ExpenseRepoImpl
import getColorTheme
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.viewmodel.viewModel
import org.koin.core.parameter.parametersOf
import presenter.ExpenseViewModel
import ui.expenseDetailScreen
import ui.expensesScreen

@Composable
fun navigation(navigator: Navigator) {
    val colors = getColorTheme()
    val viewModel = koinViewModel(ExpenseViewModel::class) { parametersOf()}

    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/home",
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsState()
            expensesScreen(uiState = uiState) { expense ->
                navigator.navigate(route = "/addExpenses/${expense.id}")
            }
        }

        scene(route = "/addExpenses/{id}?") { backStackEntry ->
            val idFromPath = backStackEntry.path<Long>("id")
            val expenseToEdit =
                idFromPath?.let { id ->
                    viewModel.getExpensesWithID(id)
                }

            expenseDetailScreen(expenseToEdit = expenseToEdit, categoryList = viewModel.getCategories()) { expense ->
                if (expenseToEdit == null) viewModel.addExpenses(expense) else viewModel.editExpenses(expense = expense)
                navigator.popBackStack()
            }

//            Create ExpenseDetailScreen
        }
    }
}
