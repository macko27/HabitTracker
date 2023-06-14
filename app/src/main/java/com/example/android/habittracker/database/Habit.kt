package com.example.habittracer.database

/**
 * entita Habit, ktorá reprezentuje jednu položku v databáze.
 * Trieda je Parcelable, takže je možné ju posielať cez navArgs vo fragmentoch.
 */
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity(tableName = "habit_table")
data class Habit (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "habit_name")
    val habit_name: String,

    @ColumnInfo(name = "habit_info")
    val habit_info: String,

    @ColumnInfo(name = "habit_start_date")
    val habit_start_date: String,

    @ColumnInfo(name = "habit_clicked_date")
    val habit_clicked_date: String,

    @ColumnInfo(name = "habit_last_failed_date")
    val habit_last_failed_date: String,

    @ColumnInfo(name = "habit_difficulty")
    val habit_difficulty: Boolean,

    @ColumnInfo(name = "habit_7")
    val habit_7: Boolean,

    @ColumnInfo(name = "habit_30")
    val habit_30: Boolean,

    @ColumnInfo(name = "habit_60")
    val habit_60: Boolean,

    @ColumnInfo(name = "habit_90")
    val habit_90: Boolean,

    @ColumnInfo(name = "habit_180")
    val habit_180: Boolean,

    @ColumnInfo(name = "habit_365")
    val habit_365: Boolean,
): Parcelable

