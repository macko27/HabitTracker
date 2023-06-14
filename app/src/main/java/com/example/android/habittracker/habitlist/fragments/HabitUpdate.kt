package com.example.android.habittracker.habitlist.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.habittracker.Functions
import com.example.android.habittracker.R
import com.example.android.habittracker.databinding.FragmentHabitUpdateBinding
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModel
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModelFactory
import com.example.habittracer.database.Habit
import com.example.habittracer.database.HabitDatabase
import java.util.*

/**
 * Fragment, ktorý umožnuje aktualizovať informácie o zvyku.
 */
class HabitUpdate: Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentHabitUpdateBinding

    private var date: String = ""

    private val args: HabitUpdateArgs by navArgs()

    private lateinit var viewModel: HabitListViewModel

    /**
     * Metóda, ktorá sa volá pri vytvorení pohľadu fragmentu.
     * Nastavujú sa tu informácie o zvykoch pre jednotlivé polia.
     * Nastavuje sa tu poslúchač pre tlačidlo upraviť, po jeho stlačení sa aktualizuje jednotlivý zvyk v databáze.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_update, container, false)

        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val habitDao = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitListViewModelFactory(habitDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitListViewModel::class.java)

        date = args.habit.habit_start_date
        binding.updateTextViewName.setText(args.habit.habit_name)
        binding.updateTextViewInfo.setText(args.habit.habit_info)
        binding.updateTextViewDate.setText("Datum: $date")
        if (args.habit.habit_difficulty == true) {
            binding.buttonEasyUpdate.isChecked = true
        } else {
            binding.buttonHardUpdate.isChecked = true
        }

        chooseDate()

        binding.updateButton.setOnClickListener {
            val name = binding.updateTextViewName.text.toString()
            val info = binding.updateTextViewInfo.text.toString()
            if (name.isEmpty() || info.isEmpty() || date.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val yesterday = Functions.getYesterdayOfDate(date)

            if (binding.buttonEasyUpdate.isChecked) {
                viewModel.onUpdate(Habit(args.habit.id, name, info, date, date, yesterday, true, args.habit.habit_7, args.habit.habit_30,args.habit.habit_60,args.habit.habit_90,args.habit.habit_180,args.habit.habit_365))
            } else {
                viewModel.onUpdate(Habit(args.habit.id, name, info, date, date, yesterday,false, args.habit.habit_7, args.habit.habit_30,args.habit.habit_60,args.habit.habit_90,args.habit.habit_180,args.habit.habit_365))
            }
            findNavController().navigate(R.id.action_habitUpdate_to_habitList)
        }

        return binding.root
    }

    /**
     * Metóda na výber dátumu pomocou DatePickerDialogu.
     */
    private fun chooseDate() {
        binding.updateButtonSaveDate.setOnClickListener {
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
        binding.updateTextViewDate.text = "Date: $date"
    }

    /**
     * Metóda, ktorá vytvára menu pre fragment.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_menu, menu)
    }

    /**
     * Metóda, ktorá sa volá po kliknutí na položku v menu.
     * Umožnuje vymazať zvyk.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_habit -> {
                AlertDialog.Builder(context)
                    .setTitle("Warning!")
                    .setMessage("Are you sure you want to delete this habit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.onDelete(args.habit)
                        Toast.makeText(context, "The habit has been successfully deleted.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_habitUpdate_to_habitList)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}