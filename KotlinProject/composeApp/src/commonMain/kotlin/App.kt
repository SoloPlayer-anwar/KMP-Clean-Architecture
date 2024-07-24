import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.TitleTopBarTypes
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.navigation
import org.koin.compose.KoinContext
import org.koin.compose.scope.KoinScope

@Composable
fun app() {
    PreComposeApp {
        KoinContext {
            val colors = getColorTheme()

            AppTheme {
                val navigator = rememberNavigator()
                val titleTopBar = getTitleTopBar(navigator)
                val isEditOrAddExpenses = titleTopBar != TitleTopBarTypes.Dashboard.value
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = titleTopBar,
                                    fontSize = 25.sp,
                                    color = colors.textColor,
                                )
                            },
                            navigationIcon = {
                                if (isEditOrAddExpenses) {
                                    IconButton(
                                        onClick = {
                                            navigator.popBackStack()
                                        },
                                    ) {
                                        Icon(
                                            modifier = Modifier.padding(start = 16.dp),
                                            imageVector = Icons.Default.ArrowBackIosNew,
                                            tint = colors.textColor,
                                            contentDescription = "Arrow Back",
                                        )
                                    }
                                } else {
                                    Icon(
                                        modifier = Modifier.padding(start = 16.dp),
                                        imageVector = Icons.Default.Apps,
                                        tint = colors.textColor,
                                        contentDescription = "Dashboard Icon",
                                    )
                                }
                            },
                            backgroundColor = colors.backgroundColor,
                        )
                    },
                    floatingActionButton = {
                        if (!isEditOrAddExpenses) {
                            FloatingActionButton(
                                modifier = Modifier.padding(10.dp),
                                onClick = {
                                    navigator.navigate("/addExpenses")
                                },
                                shape = RoundedCornerShape(50),
                                backgroundColor = colors.addIconColor,
                                contentColor = Color.White,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    tint = Color.White,
                                    contentDescription = "Floating Icon",
                                )
                            }
                        }
                    },
                ) { paddingValues ->
                    navigation(navigator)
                }
            }
        }
    }
}

@Composable
fun getTitleTopBar(navigator: Navigator): String {
    var titleTopBar = TitleTopBarTypes.Dashboard
    val isOnAddExpenses =
        navigator.currentEntry
            .collectAsState(null)
            .value
            ?.route
            ?.route
            .equals("/addExpenses/{id}?")
    if (isOnAddExpenses) titleTopBar = TitleTopBarTypes.ADD

    val isOnEditExpenses =
        navigator.currentEntry
            .collectAsState(null)
            .value
            ?.path<Long>("id")
    isOnEditExpenses?.let {
        titleTopBar = TitleTopBarTypes.EDIT
    }
    return titleTopBar.value
}
