package previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import data.ExpenseManager
import presenter.ExpenseUiState
import ui.expensesItem
import ui.expensesScreen

@Preview(showBackground = true)
@Composable
fun ExpenseItemPreviews() {
    Box(modifier = Modifier.padding(10.dp)) {
        expensesItem(expense = ExpenseManager.fakeExpenseList[0], onExpenseClick = {

        })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenPreview() {
    expensesScreen(uiState = ExpenseUiState(
        expenses = ExpenseManager.fakeExpenseList,
        total = 700.0
    ), onExpenseClick = {})
}