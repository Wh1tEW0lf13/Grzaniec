package com.example.grzeniec

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grzeniec.ui.components.TransactionCard
import com.example.grzeniec.viewmodel.SpendingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    viewModel: SpendingViewModel,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToDetail: (title: String, category: String, date: String, amount: String) -> Unit = { _, _, _, _ -> }
) {
    val spendings by viewModel.allSpendings.collectAsState()

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
                items = spendings,
                key = { item -> item.id }
            ) { item ->
                val formattedAmount = String.format(
                    Locale.forLanguageTag("pl-PL"),
                    "%.2f zł",
                    item.amount
                )
                val formattedDate = item.date.toFormattedDate()

                TransactionCard(
                    title = item.name,
                    amount = formattedAmount,
                    onClick = {
                        onNavigateToDetail(item.name, item.category, formattedDate, formattedAmount)
                    }
                )
            }
        }
    }
}