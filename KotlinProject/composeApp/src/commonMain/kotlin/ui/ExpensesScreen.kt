package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import getColorTheme
import model.Expense
import presenter.ExpenseUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun expensesScreen(
    uiState: ExpenseUiState,
    onExpenseClick: (expense: Expense) -> Unit,
) {
    val colors = getColorTheme()
    LazyColumn(
        modifier = Modifier.padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        stickyHeader {
            Column(modifier = Modifier.background(colors.backgroundColor)) {
                expensesTotalHeader(uiState.total)
                allExpensesHeader()
            }
        }

        items(uiState.expenses) { lazyItemScope ->
            expensesItem(expense = lazyItemScope, onExpenseClick = onExpenseClick)
        }
    }
}

@Composable
fun expensesTotalHeader(total: Double) {
    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(28),
        backgroundColor = Color.Black,
        elevation = 4.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(120.dp).padding(14.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "$ $total",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(modifier = Modifier.align(Alignment.CenterEnd), text = "IDR", color = Color.Gray)
        }
    }
}

@Composable
fun allExpensesHeader() {
    val colors = getColorTheme()

    Row(modifier = Modifier.padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = "All Expenses",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = colors.textColor,
        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            enabled = false,
        ) {
            Text("View All")
        }
    }
}

@Composable
fun expensesItem(
    expense: Expense,
    onExpenseClick: (expense: Expense) -> Unit,
) {
    val colors = getColorTheme()
    Card(
        modifier =
            Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 8.dp).clickable {
                onExpenseClick(expense)
            },
        backgroundColor = colors.colorExpenseItem,
        shape = RoundedCornerShape(28),
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(50.dp), shape = RoundedCornerShape(35), color = colors.purple) {
                Image(
                    modifier = Modifier.padding(10.dp),
                    imageVector = expense.icon,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image Icon",
                )
            }

            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(
                    text = expense.category.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = colors.textColor,
                )

                Text(
                    text = expense.description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }
            Text(
                text = "IDR ${expense.amount}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = colors.textColor,
            )
        }
    }
}
