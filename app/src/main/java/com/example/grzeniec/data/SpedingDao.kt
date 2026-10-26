package com.example.grzeniec.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDao {
    @Query("SELECT * FROM Spending ORDER BY date DESC")
    fun getAllSpending(): Flow<List<SpendingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpending(spending: SpendingEntity)

    @Delete
    suspend fun deleteSpending(spending: SpendingEntity)
}