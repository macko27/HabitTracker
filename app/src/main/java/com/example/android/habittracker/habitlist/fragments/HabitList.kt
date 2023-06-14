package com.example.android.habittracker.habitlist.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.habittracker.R
import com.example.android.habittracker.databinding.FragmentHabitListBinding
import com.example.android.habittracker.habitlist.HabitListAdapter
import com.example.android.habittracker.habitlist.HabitListListener
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModel
import com.example.android.habittracker.habitlist.viewmodel.HabitListViewModelFactory
import com.example.habittracer.database.HabitDatabase

/**
 * Fragment, ktorý zobrazuje všetky zvyky osobné dáta postavy.
 */
class HabitList : Fragment() {

    private lateinit var viewModel: HabitListViewModel
    private lateinit var binding: FragmentHabitListBinding


    /**
     * Metóda, ktorá sa volá pri vytvorení pohľadu fragmentu.
     * Inicializuje sa tu [HabitListViewModel] a [FragmentHabitListBinding].
     * Nastavuje sa adapter pre zoznam zvykov a pozorovateľ pre [viewModel.habits].
     * Nastavuje sa aj [viewModel.navigateToHabitAdd] a [viewModel.navigateToHabitInfo] pre navigáciu.
     * Volajú sa metódy [showPersonData] a [showPersonPicture] na zobrazenie dát o osobe.
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_list, container, false)

        val application = requireNotNull(this.activity).application

        val habitDao = HabitDatabase.getInstance(application).habitDatabaseDao
        val viewModelFactory = HabitListViewModelFactory(habitDao, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitListViewModel::class.java)

        binding.habitListViewModel = viewModel


        val adapter = HabitListAdapter(HabitListListener { id ->
            viewModel.onhabitIsClicked(id)
        })
        binding.habitList.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        viewModel.navigateToHabitAdd.observe(viewLifecycleOwner, Observer { habit ->
            habit?.let {
                this.findNavController().navigate(R.id.action_habitList_to_habitAdd)
                viewModel.onHabitAddNavigated()
            }
        })

        viewModel.navigateToHabitInfo.observe(viewLifecycleOwner, Observer { habit ->
            habit?.let {
                val action = HabitListDirections.actionHabitListToHabitInfo(habit)
                this.findNavController().navigate(action)
                viewModel.onHabitInfoNavigated()
            }
        })

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_habitList_to_habitAdd)
        }

        setHasOptionsMenu(true)

        showPersonData()
        showPersonPicture()

        return binding.root
    }

    /**
     * Metóda, ktorá vytvára menu pre fragment.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    /**
     * Metóda, ktorá sa volá po kliknutí na položku v menu.
     * Buď môžem vymazať všetky zvyky alebo resetovať osobu..
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_habits -> {
                AlertDialog.Builder(context)
                    .setTitle("Warning!")
                    .setMessage("Do you really want to delete all habits?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.onClear()
                        Toast.makeText(context, "All habits have been successfully deleted!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                    }
                    .show()
            }
            R.id.reset_person -> {
                viewModel.resetPersonData()
                showPersonData()
                showPersonPicture()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Metóda, ktorá slúži na zobrazenie dát o osobe.
     * Získa dáta z [SharedPreferences] a nastaví ich na príslušné polia.
     */
    fun showPersonData() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("person", Context.MODE_PRIVATE)
        val health = sharedPreferences.getInt("health_key", 0)
        val level = sharedPreferences.getInt("level_key", 0)
        val experiences = sharedPreferences.getInt("experiences_key", 0)
        binding.personHealth.text = "Health: $health/50"
        binding.personLevel.text = "Level: $level"
        binding.personExperiences.text = "Experiences: $experiences/100"
    }

    /**
     * Metóda, ktorá slúži na zobrazenie obrázku osoby, ak je zobrazený palec hore osoba zije, ak palec dole, je mŕtva.
     * Nastavuje podľa počtu životov z [SharedPreferences].
     */
    fun showPersonPicture() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("person", Context.MODE_PRIVATE)
        val health = sharedPreferences.getInt("health_key", 0)
        if (health == 0) {
            binding.imagePerson.setImageResource(R.drawable.ic_thumb_down)
        } else {
            binding.imagePerson.setImageResource(R.drawable.ic_thumb_up)
        }
    }
}