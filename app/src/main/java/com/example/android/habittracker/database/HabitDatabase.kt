package com.example.habittracer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Tento kód definuje triedu HabitDatabase, ktorá je potomkom RoomDatabase a slúži na vytvorenie a správu relačnej databázy pomocou knižnice Room.
 * Je to návrhový vzor
 */
@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {

    abstract val habitDatabaseDao: HabitDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getInstance(context: Context): HabitDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, HabitDatabase::class.java, "habit_table")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}