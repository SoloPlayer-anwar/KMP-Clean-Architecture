package data

import model.Expense
import model.ExpenseCategory

object ExpenseManager {
    private var currentID = 1L

    val fakeExpenseList = mutableListOf(
        Expense(
            id = currentID++,
            amount = 900.0,
            category = ExpenseCategory.GROCERIES,
            description = "Food Bank"
        ),
        Expense(
            id = currentID++,
            amount = 200.0,
            category = ExpenseCategory.PARTY,
            description = "Weekend Party"
        ),
        Expense(
            id = currentID++,
            amount = 300.0,
            category = ExpenseCategory.Snack,
            description = "Snack"
        ),
        Expense(
            id = currentID++,
            amount = 600.0,
            category = ExpenseCategory.Coffee,
            description = "Coffee"
        ),
        Expense(
            id = currentID++,
            amount = 800.0,
            category = ExpenseCategory.Car,
            description = "Car"
        ),
        Expense(
            id = currentID++,
            amount = 500.0,
            category = ExpenseCategory.House,
            description = "Cleaning"
        ),
        Expense(
            id = currentID++,
            amount = 800.000,
            category = ExpenseCategory.Other,
            description = "Services"
        )
    )

    fun addNewExpense(expense: Expense) {
        fakeExpenseList.add(expense.copy(id = currentID++))
    }

    fun editExpense(expense: Expense) {
        val index = fakeExpenseList.indexOfFirst { fakeExpense: Expense ->
            fakeExpense.id == expense.id
        }

        if (index != -1) {
            fakeExpenseList[index] = fakeExpenseList[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )
        }
    }

    fun getCategories(): List<ExpenseCategory> {
        return listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.Snack,
            ExpenseCategory.Coffee,
            ExpenseCategory.Car,
            ExpenseCategory.House,
            ExpenseCategory.Other,
        )
    }

    fun deleteExpense(expense: Expense) {
        val index = fakeExpenseList.indexOfFirst { fakeExpense: Expense ->
            fakeExpense.id == expense.id
        }
        fakeExpenseList.removeAt(index = index)
    }
}