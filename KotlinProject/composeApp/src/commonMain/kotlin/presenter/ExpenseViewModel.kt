package presenter

import data.ExpenseRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Expense
import model.ExpenseCategory
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    var total: Double = 0.0,
)

class ExpenseViewModel(
    private val repo: ExpenseRepoImpl,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState = _uiState.asStateFlow()

    private val allExpense = repo.getAllExpense()

    init {
        getAllExpenses()
    }

    private fun updateState() {
        _uiState.update { state ->
            state.copy(expenses = allExpense, total = allExpense.sumOf { it.amount })
        }
    }

    private fun getAllExpenses() {
        viewModelScope.launch {
            updateState()
        }
    }

    fun addExpenses(expense: Expense) {
        viewModelScope.launch {
            repo.addExpense(expense = expense)
            updateState()
        }
    }

    fun editExpenses(expense: Expense) {
        viewModelScope.launch {
            repo.editExpense(expense = expense)
            updateState()
        }
    }

    fun deleteExpenses(expense: Expense) {
        viewModelScope.launch {
            repo.deleteExpense(expense = expense)
            updateState()
        }
    }

    fun getExpensesWithID(id: Long): Expense = allExpense.first { it.id == id }

    fun getCategories(): List<ExpenseCategory> = repo.getCategories()
}
