package com.example.grzeniec.ui.theme
import androidx.compose.runtime.Composable
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.graphics.Color
val NavigatorBarLight = Color(0xFFB1E7FD)
val NavSelectedColor = Color(0xFF0028A9)
val NavUnselectedColor = Color.Black
val NavIndicatorColor = Color(0xFFFFFFFF)

val AddButtonColorContainer = Color(0xFF36EAF3)

val AddButtonColorContent = Color(0xFF234EF1)

@Composable
fun navigationBarItemColors(): NavigationBarItemColors{
    return NavigationBarItemDefaults.colors(
    selectedIconColor = NavSelectedColor,
    selectedTextColor = NavSelectedColor,
    indicatorColor = NavIndicatorColor,
    unselectedIconColor = NavUnselectedColor,
    unselectedTextColor = NavUnselectedColor
    )
}