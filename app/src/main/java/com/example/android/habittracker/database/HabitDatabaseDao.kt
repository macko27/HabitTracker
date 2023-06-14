package com.example.habittracer.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Rôzne metódy pre prístup k databáze a vykonávanie operácií s entitou Habit.
 */
@Dao
interface HabitDatabaseDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE FROM habit_table")
    suspend fun clear()

    @Query("SELECT * FROM habit_table WHERE id = :key")
    suspend fun get(key: Int): Habit?

    @Query("SELECT * FROM habit_table ORDER BY id DESC")
    fun getAllHabits(): LiveData<List<Habit>>
}