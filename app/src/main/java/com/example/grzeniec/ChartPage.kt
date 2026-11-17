package com.example.grzeniec

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.example.grzeniec.ui.theme.ChartBlue
import com.example.grzeniec.ui.theme.ChartGreen
import com.example.grzeniec.ui.theme.ChartPink
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.Locale
import kotlin.math.abs
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
private data class MonthYear(val month: Int, val year: Int) {
    fun previous(): MonthYear = if (month == 1) MonthYear(12, year - 1) else MonthYear(month - 1, year)
    fun next(): MonthYear = if (month == 12) MonthYear(1, year + 1) else MonthYear(month + 1, year)

    fun displayName(): String {
        val months = listOf(
            "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec",
            "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"
        )
        return "${months[month - 1]} $year"
    }
}

private fun MockTransaction.toMonthYear(): MonthYear? {
    val parts = date.split("-")
    if (parts.size != 3) return null
    val month = parts[1].toIntOrNull() ?: return null
    val year = parts[2].toIntOrNull() ?: return null
    return MonthYear(month, year)
}

private fun MockTransaction.toPositiveAmount(): Float {
    val cleaned = amount
        .replace("zł", "")
        .replace(" ", "")
        .replace(",", ".")
        .trim()
    return abs(cleaned.toFloatOrNull() ?: 0f)
}

private fun mapCategoryLabel(rawCategory: String): String = when (rawCategory.lowercase()) {
    "dining" -> "Jedzenie"
    "groceries" -> "Zakupy"
    "transport" -> "Transport"
    else -> rawCategory.replaceFirstChar { it.uppercase() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ChartPage(
    onNavigateBack: () -> Unit = {}
) {
    val mockList = listOf(
        MockTransaction(1, "Kawa", "dining", "01-01-2025", "- 22,50 zł"),
        MockTransaction(3, "Żabka", "groceries", "01-01-2025", "- 5,50 zł"),
        MockTransaction(4, "Biedronka", "groceries", "01-01-2025", "- 39,20 zł"),
        MockTransaction(5, "MPK Wrocław", "transport", "01-01-2025", "- 2,20 zł"),
        MockTransaction(6, "Kebab", "dining", "02-02-2025", "- 48,00 zł"),
        MockTransaction(7, "Blumiś", "groceries", "02-02-2025", "- 84,30 zł")
    )

    var selectedMonth by remember {
        mutableStateOf(mockList.firstOrNull()?.toMonthYear() ?: MonthYear(1, 2025))
    }

    val pieEntries = remember(selectedMonth) {
        mockList
            .filter { it.toMonthYear() == selectedMonth }
            .groupBy { it.category }
            .map { (category, items) ->
                val sum = items.sumOf { it.toPositiveAmount().toDouble() }.toFloat()
                PieEntry(sum, mapCategoryLabel(category))
            }
            .sortedByDescending { it.value }
    }

    val totalSpent = remember(pieEntries) {
        pieEntries.sumOf { it.value.toDouble() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Podsumowanie miesiąca") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { selectedMonth = selectedMonth.previous() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Poprzedni miesiąc"
                        )
                    }

                    Text(
                        text = selectedMonth.displayName(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { selectedMonth = selectedMonth.next() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Następny miesiąc"
                        )
                    }
                }
            }

            item {
                if (pieEntries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Brak wydatków w tym miesiącu",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    CategoryPieChart(
                        entries = pieEntries,
                        centerText = String.format(
                            Locale.forLanguageTag("pl-PL"),
                            "Suma:\n%.2f zł",
                            totalSpent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    )
                }
            }

            if (pieEntries.isNotEmpty()) {
                items(pieEntries) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = entry.label,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = String.format(
                                    Locale.forLanguageTag("pl-PL"),
                                    "%.2f zł",
                                    entry.value
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
@Composable
private fun CategoryPieChart(
    entries: List<PieEntry>,
    centerText: String,
    modifier: Modifier = Modifier
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    val chartColors = remember {
        listOf(
            ChartBlue.toArgb(),
            ChartPink.toArgb(),
            ChartGreen.toArgb()
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                setUsePercentValues(true)
                isDrawHoleEnabled = true
                holeRadius = 38f
                transparentCircleRadius = 0f
                setCenterTextSize(14f)
                setEntryLabelColor(AndroidColor.WHITE)
                setEntryLabelTextSize(11f)
                setDrawEntryLabels(false)

                legend.apply {
                    isEnabled = true
                    formSize = 14f
                    textSize = 14f
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                }
            }
        },
        update = { pieChart ->
            val dataSet = PieDataSet(entries, "").apply {
                colors = chartColors
                sliceSpace = 1f
                setDrawValues(false)
            }

            pieChart.data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(pieChart))
            }

            pieChart.centerText = centerText
            pieChart.setHoleColor(surfaceColor)
            pieChart.setCenterTextColor(onSurfaceColor)
            pieChart.legend.textColor = onSurfaceColor

            pieChart.animateY(500)
            pieChart.invalidate()
        }
    )
}