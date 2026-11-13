package com.example.grzeniec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.grzeniec.data.SpendingDao
import com.example.grzeniec.data.SpendingEntity
import kotlinx.coroutines.launch

class SpendingViewModel(private val dao: SpendingDao) : ViewModel() {
    fun addSpending(name: String, price: Double, category: String )
    {
        viewModelScope.launch()
        {
            val newSpanding = SpendingEntity(
                name = name,
                amount = price,
                category = category
            )
            dao.insertSpending(newSpanding)
        }
    }

    companion object{
        fun provideFactory(dao: SpendingDao): ViewModelProvider.Factory = viewModelFactory{
            initializer {
                SpendingViewModel(dao)
            }
        }
    }
}
