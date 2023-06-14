package com.example.android.habittracker.habitlist.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.habittracker.Functions
import com.example.android.habittracker.R
import com.example.android.habittracker.databinding.FragmentHabitInfoBinding
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModel
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModelFactory
import com.example.habittracer.database.Habit
import com.example.habittracer.database.HabitDatabase

/**
 * Fragment pre zobrazenie podrobných dát o zvolenom zvyku.
 * Metódy na boj s nepriateľmi a na splnenie a nesplnenie zvykov.
 */
class HabitInfo: Fragment() {

    private lateinit var binding: FragmentHabitInfoBinding

    private var today: String = ""

    private val args: HabitInfoArgs by navArgs()

    private lateinit var viewModel: HabitListViewModel


    /**
     * Metóda, ktorá sa volá pri vytvorení pohľadu fragmentu.
     * Nastavujú sa tu informácie o zvykoch pre jednotlivé polia.
     * Nastavujú sa tu poslúchači kliknutí pre všetky tlačidlá.
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_info, container, false)

        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val habitDao = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitListViewModelFactory(habitDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitListViewModel::class.java)

        today = Functions.getTodaysDate()
        var daysWithoutFail = Functions.calculateDaysBetweenDates(args.habit.habit_last_failed_date)
        if (args.habit.habit_clicked_date.equals(today) || args.habit.habit_last_failed_date.equals(today)) {
            setInvisibleFullfillbutton()
            setInvisibleFailButton()
        } else {
            daysWithoutFail -= 1
        }

        binding.habitName.setText(args.habit.habit_name)
        binding.habitDays.setText("Number of days without fail: ${daysWithoutFail.toString()}")
        binding.habitInfo.setText("Info: ${args.habit.habit_info}")
        binding.habitDate.setText("Start date: ${args.habit.habit_start_date}")
        binding.habitFailDate.setText("Last fail date: ${args.habit.habit_last_failed_date}")

        binding.buttonFullfill.setOnClickListener {
            fulfillButtonActivated()
        }

        binding.buttonFail.setOnClickListener {
            failButtonActivated()
        }

        if (args.habit.habit_7) {
            binding.buttonBoss7.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss7.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss7.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss7.setText("")
        } else {
            binding.buttonBoss7.setOnClickListener {
                bossButtonActivated(7, "7 Days boss!")
            }
        }
        if (args.habit.habit_30) {
            binding.buttonBoss30.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss30.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss30.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss30.setText("")
        } else {
            binding.buttonBoss30.setOnClickListener {
                bossButtonActivated(30, "30 Days boss!")
            }
        }
        if (args.habit.habit_60) {
            binding.buttonBoss60.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss60.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss60.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss60.setText("")
        } else {
            binding.buttonBoss60.setOnClickListener {
                bossButtonActivated(60, "60 Days boss!")
            }
        }
        if (args.habit.habit_90) {
            binding.buttonBoss90.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss90.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss90.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss90.setText("")
        } else {
            binding.buttonBoss90.setOnClickListener {
                bossButtonActivated(90, "90 Days boss!")
            }
        }

        if (args.habit.habit_180) {
            binding.buttonBoss180.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss180.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss180.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss180.setText("")
        } else {
            binding.buttonBoss180.setOnClickListener {
                bossButtonActivated(180, "180 Days boss!")
            }
        }

        if (args.habit.habit_365) {
            binding.buttonBoss365.setBackgroundResource(R.drawable.ic_done)
            binding.buttonBoss365.layoutParams.width = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss365.layoutParams.height = resources.getDimensionPixelSize(R.dimen.icon_size)
            binding.buttonBoss365.setText("")
        } else {
            binding.buttonBoss365.setOnClickListener {
                bossButtonActivated(365, "365 Days boss!")
            }
        }

        return binding.root
    }

    /**
     * Funkcia, ktorá sa volá po aktivácii tlačidla pre boj s nepriateľmi.
     * @param days Počet dní, za ktorý je potrebné mať pre výhru.
     * @param bossTitle Názov nepriateľa.
     */
    private fun bossButtonActivated(days: Int, bossTitle: String) {
        AlertDialog.Builder(context)
            .setTitle(bossTitle)
            .setMessage("Do you really want to fight this boss?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                val numberOfdaysWithoutFail = Functions.calculateDaysBetweenDates(args.habit.habit_last_failed_date)
                if (numberOfdaysWithoutFail < days) {
                    viewModel.changePersonHealth(-50)
                    Toast.makeText(context, "You lost", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.changePersonExperiences(100)
                    when (days) {
                        7 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            true, args.habit.habit_30, args.habit.habit_60, args.habit.habit_90, args.habit.habit_180, args.habit.habit_365))
                        30 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            args.habit.habit_7, true, args.habit.habit_60, args.habit.habit_90, args.habit.habit_180, args.habit.habit_365))
                        60 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            args.habit.habit_7, args.habit.habit_30, true, args.habit.habit_90, args.habit.habit_180, args.habit.habit_365))
                        90 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            args.habit.habit_7, args.habit.habit_30, args.habit.habit_60, true, args.habit.habit_180, args.habit.habit_365))
                        180 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            args.habit.habit_7, args.habit.habit_30, args.habit.habit_60, args.habit.habit_90, true, args.habit.habit_365))
                        365 -> viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                            args.habit.habit_start_date, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                            args.habit.habit_7, args.habit.habit_30, args.habit.habit_60, args.habit.habit_90, args.habit.habit_180, true))
                    }
                    Toast.makeText(context, "Congratulations", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigate(R.id.action_habitInfo_to_habitList)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }

    /**
     * Funkcia, pre splnenie zvyku.
     */
    private fun fulfillButtonActivated() {
        AlertDialog.Builder(context)
            .setTitle("Warning!")
            .setMessage("Do you really want to fulfill this habit for today?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                                        today, args.habit.habit_last_failed_date, args.habit.habit_difficulty,
                                        args.habit.habit_7, args.habit.habit_30, args.habit.habit_60, args.habit.habit_90, args.habit.habit_180, args.habit.habit_365))
                setInvisibleFullfillbutton()
                setInvisibleFailButton()
                if (args.habit.habit_difficulty) {
                    viewModel.changePersonExperiences(25)
                } else {
                    viewModel.changePersonExperiences(10)
                }
                Toast.makeText(context, "Congratulation!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_habitInfo_to_habitList)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }

    /**
     * Funkcia, pre nesplnenie zvyku.
     */
    private fun failButtonActivated() {
        AlertDialog.Builder(context)
            .setTitle("Warning!")
            .setMessage("Do you really want to fail this habit for today?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.onUpdate(Habit(args.habit.id, args.habit.habit_name, args.habit.habit_info, args.habit.habit_start_date,
                                        args.habit.habit_clicked_date, today, args.habit.habit_difficulty,
                                        args.habit.habit_7, args.habit.habit_30, args.habit.habit_60, args.habit.habit_90, args.habit.habit_180, args.habit.habit_365))
                setInvisibleFullfillbutton()
                setInvisibleFailButton()
                if (args.habit.habit_difficulty) {
                    viewModel.changePersonHealth(-5)
                } else {
                    viewModel.changePersonHealth(-10)
                }
                Toast.makeText(context, "X", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_habitInfo_to_habitList)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }

    /**
     * Funkcia, ktorá zneviditeľní a znemožní stláčanie tlačidla buttonFullfill.
     */
    private fun setInvisibleFullfillbutton()  {
        binding.buttonFullfill.isClickable = false
        binding.buttonFullfill.isVisible = false
    }

    /**
     * Funkcia, ktorá zneviditeľní a znemožní stláčanie tlačidla buttonFail.
     */
    private fun setInvisibleFailButton()  {
        binding.buttonFail.isVisible = false
        binding.buttonFail.isClickable = false
    }

    /**
     * Metóda, ktorá vytvára menu pre fragment.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
    }

    /**
     * Metóda, ktorá sa volá po kliknutí na položku v menu.
     * Po stlačení môžem aktualizovať zvyk.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_habit -> {
                val action = HabitInfoDirections.actionHabitInfoToHabitUpdate(args.habit)
                this.findNavController().navigate(action)
                viewModel.onHabitUpdateNavigated()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}