package com.example.android.habittracker.habitlist.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracer.database.HabitDatabaseDao

/**
 * Trieda, ktorá slúži ako továreň na vytváranie inštancií.
 * Je to návrhový vzor
 */
class HabitListViewModelFactory(private val habitDao: HabitDatabaseDao,
                                private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitListViewModel::class.java)) {
            return HabitListViewModel(habitDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}