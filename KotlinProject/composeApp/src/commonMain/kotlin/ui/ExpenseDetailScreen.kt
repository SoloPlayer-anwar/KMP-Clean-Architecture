package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.TitleTopBarTypes
import getColorTheme
import kotlinx.coroutines.launch
import model.Expense
import model.ExpenseCategory

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun expenseDetailScreen(
    expenseToEdit: Expense? = null,
    categoryList: List<ExpenseCategory> = emptyList(),
    addExpenseAndNavigateBack: (Expense) -> Unit,
) {
    val colors = getColorTheme()
    var price by remember {
        mutableStateOf(expenseToEdit?.amount ?: 0.0)
    }
    var description by remember {
        mutableStateOf(expenseToEdit?.description ?: "")
    }
    var expenseCategory by remember {
        mutableStateOf(expenseToEdit?.category?.name ?: "")
    }
    var categorySelected by remember {
        mutableStateOf(expenseToEdit?.category?.name ?: "Select a category")
    }
    val sheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
        )
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == ModalBottomSheetValue.Expanded) {
            keyboardController?.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            categoryBottomSheetContent(categoryList) {
                expenseCategory = it.name
                categorySelected = it.name
                scope.launch {
                    sheetState.hide()
                }
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp),
        )
        {
           expenseAmount(
               priceContent = price,
               onPriceChange = {
                   price = it
               },
               keyboardController = keyboardController
           )
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            expenseTypeSelected(categorySelected = categorySelected, openBottomSheet =  {
                scope.launch {
                    sheetState.show()
                }
            })
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            expenseDescription(
                descriptionContent = description,
                onDescriptionChange = {
                    description = it
                },
                keyboardController = keyboardController
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(45)),
                onClick = {
                    val expense = Expense(
                        amount = price,
                        category = ExpenseCategory.valueOf(expenseCategory),
                        description = description
                    )
                    val expenseFromEdit = expenseToEdit?.id.let { expense.copy(id = it) }
                    addExpenseAndNavigateBack(expenseFromEdit ?: expense)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.purple,
                    contentColor = Color.White
                ),
                enabled = price != 0.0 && description.isNotBlank() && expenseCategory.isNotBlank()
            ) {
                expenseToEdit?.let {
                    Text(text = TitleTopBarTypes.EDIT.value)
                    return@Button
                }
                Text(text = TitleTopBarTypes.ADD.value)
            }
        }
    }
}

@Composable
private fun expenseAmount(
    priceContent: Double,
    onPriceChange: (Double) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    val colors = getColorTheme()
    var text by remember { mutableStateOf("$priceContent") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Amount",
            fontSize = 20.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )

        Row( verticalAlignment = Alignment.CenterVertically )
        {
            Text(text = text , fontSize = 25.sp, color = colors.textColor, fontWeight = FontWeight.Bold)
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { newText ->
                    val numericText = newText.filter { it.isDigit() || it == '.' }
                    text = if (numericText.isNotEmpty() && numericText.count { it == '.' } <= 1)
                        try {
                            val newValue = numericText.toDouble()
                            onPriceChange(newValue)
                            numericText
                        } catch (e: NumberFormatException) {
                            ""
                        }.toString()
                    else {
                        onPriceChange(0.0)
                        ""
                    }
                },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction =  ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone =  {keyboardController?.hide()}
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = colors.textColor,
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(fontSize = 35.sp , fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "IDR",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Gray
                )
        }
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Composable
private fun categoryBottomSheetContent(
    categories: List<ExpenseCategory>,
    onCategorySelected: (ExpenseCategory) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
    ) {
        items(categories) { category ->
            categoryItems(category, onCategorySelected = onCategorySelected)
        }
    }
}

@Composable
private fun categoryItems(
    category: ExpenseCategory,
    onCategorySelected: (ExpenseCategory) -> Unit,
) {
    Column(
        modifier =
            Modifier.fillMaxWidth().padding(8.dp).clickable {
                onCategorySelected(category)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = category.icon,
            contentDescription = "category icon",
            modifier = Modifier.size(40.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = category.name,
        )
    }
}

@Composable
private fun expenseTypeSelected(
    categorySelected:String,
    openBottomSheet: () -> Unit
) {
    val colors = getColorTheme()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Expense made for",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color =  Color.Gray
            )
            Text(
                text = categorySelected,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color =  colors.textColor
            )
        }
        IconButton(
            modifier = Modifier.clip(RoundedCornerShape(35))
                .background(color = colors.colorArrowRound),
            onClick = {
                openBottomSheet.invoke()
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Button Expense Type",
                tint = colors.textColor
            )
        }
    }
}

@Composable
fun expenseDescription(
    descriptionContent :String,
    onDescriptionChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?) {

    val colors = getColorTheme()
    var text by remember { mutableStateOf(descriptionContent) }

    Column {
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange =  { newText ->
                if (newText.length <= 200)
                    text = newText
                onDescriptionChange(newText)
            },
            value = text,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = colors.textColor,
                backgroundColor = colors.backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
            ),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            ),
            keyboardActions = KeyboardActions(
                onDone =  {
                    keyboardController?.hide()
                }
            )
        )
        Divider(
            color = Color.Black, thickness = 2.dp
        )
    }
}
