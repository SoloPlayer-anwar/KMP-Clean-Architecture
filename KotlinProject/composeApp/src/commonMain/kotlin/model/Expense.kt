package model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.PartyMode
import androidx.compose.material.icons.filled.ViewCozy
import androidx.compose.ui.graphics.vector.ImageVector

data class Expense(
    val id: Long? = -1,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String
) {
    val icon = category.icon
}



enum class ExpenseCategory(val icon : ImageVector) {
    GROCERIES(icon = Icons.Default.FoodBank),
    PARTY(icon = Icons.Default.PartyMode),
    Snack(icon = Icons.Default.Fastfood),
    Coffee(icon = Icons.Default.Coffee),
    Car(icon = Icons.Default.ElectricCar),
    House(icon = Icons.Default.House),
    Other(icon = Icons.Default.ViewCozy)
}
