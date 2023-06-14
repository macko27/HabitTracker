package com.example.android.habittracker.habitlist.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.habittracker.Functions
import com.example.android.habittracker.R
import com.example.android.habittracker.databinding.FragmentHabitAddBinding
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModel
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModelFactory
import com.example.habittracer.database.Habit
import com.example.habittracer.database.HabitDatabase
import java.util.*

/**
 * Fragment pre pridanie nového zvyku.
 */
class HabitAdd: Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentHabitAddBinding

    private var date: String = ""
    private var yesterday: String = ""

    /**
     * Metóda, ktorá sa volá pri vytvorení pohľadu fragmentu.
     * Nastavuje sa tu poslúchač pre tlačidlo pridať, po jeho stlačení sa pridá zvyk do databázy.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_add, container, false)

        val application = requireNotNull(this.activity).application
        val habitDao = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitListViewModelFactory(habitDao, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HabitListViewModel::class.java)

        chooseDate()

        binding.buttonCreate.setOnClickListener {
            val name = binding.textViewName.text.toString()
            val info = binding.textViewInfo.text.toString()
            if (name.isEmpty() || info.isEmpty() || date.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            yesterday = Functions.getYesterdayOfDate(date)

            if (binding.buttonEasy.isChecked) {
                viewModel.onAdd(Habit(0, name, info, date, date, yesterday, true, false, false, false, false, false, false))
            } else {
                viewModel.onAdd(Habit(0, name, info, date, date, yesterday, false, false, false, false, false, false, false))
            }
            findNavController().navigate(R.id.action_habitAdd_to_habitList)
        }

        return binding.root
    }

    /**
     * Metóda na výber dátumu pomocou DatePickerDialogu.
     */
    private fun chooseDate() {
        binding.buttonSaveDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dayToday = calendar.get(Calendar.DAY_OF_MONTH)
            val monthToday = calendar.get(Calendar.MONTH)
            val yearToday = calendar.get(Calendar.YEAR)

            DatePickerDialog(requireContext(), this, yearToday, monthToday, dayToday).show()
        }
    }

    /**
     * Metóda sa volá po výbere dátumu v DatePickeri a aktualizuje zvolený dátum.
     * @param p0 DatePicker objekt, z ktorého je získaný vybraný dátum.
     * @param year_ Rok vybraného dátumu.
     * @param month_ Mesiac vybraného dátumu (indexovaný od 0, teda január má hodnotu 0).
     * @param day_ Deň vybraného dátumu.
     */
    override fun onDateSet(p0: DatePicker?, year_: Int, month_: Int, day_: Int) {
        var day = day_
        var month = month_ + 1
        var year = year_

        date = "$day/$month/$year"
        binding.textViewDate.text = "Date: $date"
    }

}