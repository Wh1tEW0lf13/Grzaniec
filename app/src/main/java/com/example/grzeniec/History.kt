package com.example.grzeniec

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grzeniec.ui.components.TransactionCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

data class MockTransaction(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val amount: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToDetail: (title: String, category: String, date: String, amount: String) -> Unit = { _, _, _, _ -> }
) {
    val mockList = listOf(
        MockTransaction(1, "Kawa", "dining", "01-01-2025", "- 22,50 zł"),
        MockTransaction(3, "Żabka", "groceries", "01-01-2025", "- 5,50 zł"),
        MockTransaction(4, "Biedronka", "groceries", "01-01-2025", "- 39,20 zł"),
        MockTransaction(5, "MPK Wrocław", "transport", "01-01-2025", "- 2,20 zł")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historia") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = mockList,
                key = { item -> item.id }
            ) { item ->
                TransactionCard(
                    title = item.title,
                    amount = item.amount,
                    onClick = {
                        onNavigateToDetail(item.title, item.category, item.date, item.amount)
                    }
                )
            }
        }
    }
}