package com.example.grzeniec.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Spending")
data class SpendingEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val category: String,
    val date: Long = System.currentTimeMillis()
)