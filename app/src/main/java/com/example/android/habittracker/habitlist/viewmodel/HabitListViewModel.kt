package com.example.android.habittracker.habitlist.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracer.database.Habit
import com.example.habittracer.database.HabitDatabaseDao
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * ViewModel, zodpovedný za spracovanie logiky a interakciu s databázou pre zoznam návykov.
 */
class HabitListViewModel(habitDao: HabitDatabaseDao, application: Application) : ViewModel() {

    val database = habitDao
    val habits = database.getAllHabits()

    val sharedPreferences: SharedPreferences = application.getSharedPreferences("person", Context.MODE_PRIVATE)

    private val _navigateToHabitAdd = MutableLiveData<Habit?>()
    val navigateToHabitAdd: LiveData<Habit?> get() = _navigateToHabitAdd

    private val _navigateToHabitUpdate = MutableLiveData<Habit?>()

    private val _navigateToHabitInfo = MutableLiveData<Habit?>()
    val navigateToHabitInfo: LiveData<Habit?> get() = _navigateToHabitInfo
    fun onhabitIsClicked(habit: Habit) {
        _navigateToHabitInfo.value = habit
    }

    /**
     * Pridanie do databázy.
     */
    private suspend fun insert(habit: Habit) {
        database.insertHabit(habit);
    }

    private suspend fun update(habit: Habit) {
        database.updateHabit(habit);
    }

    private suspend fun delete(habit: Habit) {
        database.deleteHabit(habit);
    }

    private suspend fun clear() {
        database.clear();
    }

    /**
     * Pridanie do databázy pomocou viewModelScope.launch, zabezpečuje asynchrónne vykonanie operácie insert
     */
    fun onAdd(habit: Habit) {
        viewModelScope.launch {
            insert(habit)
        }
    }

    fun onDelete(habit: Habit) {
        viewModelScope.launch {
            delete(habit)
        }
    }

    fun onUpdate(habit: Habit) {
        viewModelScope.launch {
            update(habit)
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
        }
    }

    /**
     * Ukončenie navigácie
     */
    fun onHabitAddNavigated() {
        _navigateToHabitAdd.value = null
    }

    /**
     * Ukončenie navigácie
     */
    fun onHabitInfoNavigated() {
        _navigateToHabitInfo.value = null
    }

    /**
     * Ukončenie navigácie
     */
    fun onHabitUpdateNavigated() {
        _navigateToHabitUpdate.value = null
    }

    /**
     * Metóda, ktorá obnovuje údaje o osobe na predvolené hodnoty.
     */
    fun resetPersonData() {
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("health_key", 50)
            putInt("level_key", 1)
            putInt("experiences_key", 0)
        }.apply()
    }

    /**
     * Metóda, ktorá mení hodnotu zdravia.
     * @param size veľkosť zmeny
     */
    fun changePersonHealth(size: Int) {
        var personHealth = sharedPreferences.getInt("health_key", 0)

        if ((personHealth + size) >= 50) {
            personHealth = 50
        } else if ((personHealth + size) <= 0) {
            personHealth = 0
            onClear()
        } else {
            personHealth += size
        }

        sharedPreferences.edit().apply() {
            putInt("health_key", personHealth)
        }.apply()
    }

    /**
     * Metóda, ktorá mení veľkosť levlu.
     * @param size veľkosť zmeny, len väčšia ako nula
     */
    fun changePersonLevel(size: Int) {
        var personLevel = sharedPreferences.getInt("level_key", 0)
        if (size >= 0) {
            personLevel += size
            val changeHealth = Random.nextInt(1, 15)
            changePersonHealth(changeHealth)
        }

        sharedPreferences.edit().apply() {
            putInt("level_key", personLevel)
        }.apply()
    }

    /**
     * Metóda, ktorá mení hodnotu skúseností.
     * @param size veľkosť zmeny
     */
    fun changePersonExperiences(size: Int) {
        var personexperiences = sharedPreferences.getInt("experiences_key", 0)
        if ((personexperiences + size) >= 100) {
            changePersonLevel(1)
            personexperiences = 0
        } else {
            personexperiences += size
        }
        sharedPreferences.edit().apply() {
            putInt("experiences_key", personexperiences)
        }.apply()
    }
}