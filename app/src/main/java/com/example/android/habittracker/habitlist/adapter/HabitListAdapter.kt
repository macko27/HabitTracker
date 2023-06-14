package com.example.android.habittracker.habitlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.habittracker.databinding.HabitItemBinding
import com.example.habittracer.database.Habit

/**
 * Tieto triedy umožňujú správne zobrazenie zvykov v zozname návykov v RecyclerView.
 * Je to návrhový vzor
 */
class HabitListAdapter(val clickListener: HabitListListener) : ListAdapter<Habit, HabitListAdapter.ViewHolder>(HabitDiffCallback()) {

    class ViewHolder private constructor(val binding: HabitItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit, clickListener: HabitListListener) {
            binding.habit = habit
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HabitItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oneHabit = getItem(position)!!
        holder.bind(oneHabit, clickListener)
    }
}

class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }

}

class HabitListListener(val clickListener: (habit: Habit) -> Unit) {
    fun itemIsClicked(habit: Habit) = clickListener(habit)
}