package com.example.grzeniec

import android.graphics.fonts.Font
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.grzeniec.data.SpendingEntity
import com.example.grzeniec.ui.theme.AddButtonColorContainer
import com.example.grzeniec.ui.theme.AddButtonColorContent
import com.example.grzeniec.ui.theme.NavigatorBarLight
import com.example.grzeniec.ui.theme.PlaceHolderColorForSpending
import com.example.grzeniec.viewmodel.SpendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(onNavigateToProfile: () -> Unit, viewModel: SpendingViewModel) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val spending by viewModel.allSpendings.collectAsState()
    val maxTiles = 5

    var newSpendName by remember { mutableStateOf("") }
    var newSpendAmount by remember { mutableStateOf("") }
    var newSpendCategory by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp))
        {
            Text(text = "Ostatnie", fontWeight = FontWeight.Bold)
            TilesView(spending, maxTiles)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
        {
            AddButton { showBottomSheet = true }
        }
        if (showBottomSheet) {
            var expanded by remember { mutableStateOf(false) }

            val categoriesPlaceholder = listOf("Jedzenie", "Transport", "Rozrywka", "Rachunki")

            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .background(NavigatorBarLight)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Dodaj nowy element",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = newSpendName,
                        onValueChange = { newSpendName = it },
                        label = { Text("Nazwa np. Rachunki") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newSpendAmount,
                        onValueChange = { newSpendAmount = it },
                        label = { Text("Cena np. 2.50") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    @OptIn(ExperimentalMaterial3Api::class)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = newSpendCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kategoria") },
                            placeholder = { Text("Wybierz kategorię...") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categoriesPlaceholder.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        newSpendCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val parsedPrice = newSpendAmount.replace(",", ".").toDoubleOrNull() ?: 0.0
                            viewModel.addSpending(newSpendName, newSpendAmount.toDouble(), newSpendCategory)

                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = newSpendName.isNotBlank() && newSpendAmount.isNotBlank() && newSpendAmount.toFloatOrNull() != null && newSpendCategory.isNotBlank()
                    ) {
                        Text("Zapisz")
                    }
                }
            }
        }
    }
}


@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = AddButtonColorContainer,
        contentColor = AddButtonColorContent,
    ) {
        Icon(Icons.Filled.Add, "Add new expense")
    }
}

@Composable
fun TilesView(spending: List<SpendingEntity>, maxTiles: Int) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items = spending.take(maxTiles)) { spend ->
            Row(modifier = Modifier.background(PlaceHolderColorForSpending)
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Column(modifier = Modifier.weight(1f)
                    .padding(10.dp)
                ) {
                    Text(text = spend.name)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = spend.amount.toString() + " zł")
                }
                Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Szczegóły",
                tint = Color.Black
            )
            }
        }
    }
}
