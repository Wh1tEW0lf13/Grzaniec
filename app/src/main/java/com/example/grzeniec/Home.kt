package com.example.grzeniec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grzeniec.ui.theme.AddButtonColorContainer
import com.example.grzeniec.ui.theme.AddButtonColorContent
import androidx.compose.runtime.*
import com.example.grzeniec.ui.theme.NavigatorBarLight
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import com.example.grzeniec.data.SpendingEntity
import com.example.grzeniec.viewmodel.SpendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(onNavigateToProfile: () -> Unit, viewModel: SpendingViewModel){

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val spending by viewModel.allSpendings.collectAsState()
    val maxTiles = 5

    Box(modifier = Modifier.fillMaxSize())
    {

        Box(
            modifier = Modifier
                .align (Alignment.Center)
                .padding(10.dp)
        )
        {
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
            var name by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }

            var expanded by remember { mutableStateOf(false) }
            var selectedCategory by remember { mutableStateOf("") }

            // Placeholder
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
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nazwa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Cena") },
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
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true, // Blokuje ręczne wpisywanie, wymusza wybór z listy
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
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val parsedPrice = price.replace(",", ".").toDoubleOrNull() ?: 0.0
                            viewModel.addSpending(name, price.toDouble(), selectedCategory)

                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotBlank() && price.isNotBlank() && selectedCategory.isNotBlank()
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
fun TilesView(spending: List<SpendingEntity>, maxTiles: Int)
{
    LazyColumn {
        items(items = spending.take(maxTiles)){
            spend -> Text(text = spend.name)
        }
    }
}