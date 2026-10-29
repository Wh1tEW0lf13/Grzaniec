package com.example.grzeniec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grzeniec.ui.theme.NavigatorBarLight
import com.example.grzeniec.ui.theme.GrzeniecTheme
import com.example.grzeniec.ui.theme.navigationBarItemColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrzeniecTheme {
                ScaffoldView()
            }
        }
    }
}

@Composable
fun ScaffoldView() {
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
                })
            }
            composable("history") {
                History(onNavigateToProfile = {
                    navController.navigate("history")
                })
            }
        }

    }
}
