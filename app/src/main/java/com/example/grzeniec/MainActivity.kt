package com.example.grzeniec

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.History
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grzeniec.data.AppDatabase
import androidx.navigation.navArgument
import com.example.grzeniec.ui.theme.NavigatorBarLight
import com.example.grzeniec.ui.theme.GrzeniecTheme
import com.example.grzeniec.ui.theme.navigationBarItemColors
import com.example.grzeniec.viewmodel.SpendingViewModel
import com.example.grzeniec.ui.TransactionDetails

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.spendingDao()

        val viewModel: SpendingViewModel by viewModels {
            SpendingViewModel.provideFactory(dao)
        }
        enableEdgeToEdge()
        setContent {
            GrzeniecTheme {
                ScaffoldView(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ScaffoldView(viewModel: SpendingViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val itemColors = navigationBarItemColors()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = NavigatorBarLight) {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home")},
                    icon = {Icon(Icons.Default.Home, contentDescription = "Strona główna")},
                    label = {Text("Strona Główna")},
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == "history",
                    onClick = { navController.navigate("history")},
                    icon = {Icon(Icons.Default.History, contentDescription = "Historia")},
                    label = {Text("Historia")},
                    colors = itemColors
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Home(onNavigateToProfile = {
                    navController.navigate("home")
                }, viewModel)
                }
                onNavigateToCharts = {
                        navController.navigate("chart_page")
                })
            }
            composable("history") {
                History(
                    onNavigateToProfile = {
                        navController.navigate("history")
                    },
                    onNavigateToDetail = { title, category, date, amount ->
                        navController.navigate(
                            "details/${Uri.encode(title)}/${Uri.encode(category)}/${Uri.encode(date)}/${Uri.encode(amount)}"
                        )
                    }
                )
            }

            composable("chart_page") {
                ChartPage(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "details/{title}/{category}/{date}/{amount}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("category") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("amount") { type = NavType.StringType }
                )
            ) { entry ->
                TransactionDetails(
                    title = entry.arguments?.getString("title") ?: "",
                    category = entry.arguments?.getString("category") ?: "",
                    date = entry.arguments?.getString("date") ?: "",
                    amount = entry.arguments?.getString("amount") ?: "",
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

    }
}
