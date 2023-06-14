package com.example.android.habittracker.habitlist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.habittracker.Functions
import com.example.android.habittracker.Functions.calculateDaysBetweenDates
import com.example.habittracer.database.Habit

/**
 * dynamické nastavovanie textových hodnôt v TextView pomocou hodnôt z objektu Habit a využívajú sa v spojení s databindingom, keď sa vykresľuje položka zoznamu návykov v RecyclerView.
 */
@BindingAdapter("text_view_name")
fun TextView.setTextViewName(oneHabit: Habit?) {
    text = oneHabit?.habit_name
}

@BindingAdapter("text_view_days_without_fail")
fun TextView.setTextViewDaysWithoutFail(oneHabit: Habit?) {
    var days = calculateDaysBetweenDates(oneHabit!!.habit_last_failed_date)
    val date = Functions.getTodaysDate()
    if (!oneHabit.habit_clicked_date.equals(date) && !oneHabit.habit_last_failed_date.equals(date)) {
        days -= 1
    }
    text = "Pocet dni: " + days.toString()
}