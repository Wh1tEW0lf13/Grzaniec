package com.example.grzeniec

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grzeniec.ui.components.TransactionCard
import com.example.grzeniec.ui.theme.GrzeniecTheme
import androidx.compose.material3.Text

data class MockTransaction(
    val id: Int,
    val title: String,
    val amount: String
)

@Composable
fun History(onNavigateToProfile: () -> Unit = {}) {
    val mockList = listOf(
        MockTransaction(1, "Kawa", "- 22,50 zł"),
        MockTransaction(3, "Żabka", "- 5,50 zł"),
        MockTransaction(4, "Biedronka", "- 39,20 zł"),
        MockTransaction(5, "MPK Wrocław", "- 2,20 zł")
    )

    Text("Siemano historia")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    GrzeniecTheme {
        History()
    }
}