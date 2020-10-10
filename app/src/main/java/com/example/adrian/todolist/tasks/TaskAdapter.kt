package com.example.adrian.todolist.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adrian.todolist.database.Task
import com.example.adrian.todolist.databinding.ListItemTaskBinding

class TaskAdapter(val clickListener: TaskListener) : ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)

    }

    class ViewHolder private constructor(val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.titleItem
        private val description: TextView = binding.descriptionItem
        private val checkBox: CheckBox = binding.checkboxItem

        fun bind(sleepNightListener: TaskListener, item: Task) {
            binding.task = item
            binding.clickListener = sleepNightListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    class TaskListener(val clickListener: (sleepId: Long) -> Unit) {
        fun onClick(task: Task) = clickListener(task.taskId)
    }

}

