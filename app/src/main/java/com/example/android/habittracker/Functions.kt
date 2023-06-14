package com.example.android.habittracker


import java.text.SimpleDateFormat
import java.util.*

object Functions {
    /**
     * funkcia ktorá vyráta počet dní medzi zadaným dátumom a dnešným dátumom
     * @param date1 zadaný dátum - musí byť vo formáte - dd/mm/yyyy
     */
    fun calculateDaysBetweenDates(date1: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val parsedDate = Calendar.getInstance().apply {
            time = dateFormat.parse(date1) ?: return -1
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val millisecondsBetween = Math.abs(parsedDate.timeInMillis - currentDate.timeInMillis)
        return (millisecondsBetween / (1000 * 60 * 60 * 24)).toInt()
    }

    /**
     * funkcia ktorá vráti dnešný dátum vo formáte dd/mm/yyyy
     */
    fun getTodaysDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * funkcia ktorá vráti včerajší dátum vo formáte dd/mm/yyyy
     */
    fun getYesterdayOfDate(date: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date)

        calendar.add(Calendar.DAY_OF_YEAR, -1)

        return dateFormat.format(calendar.time)
    }
}