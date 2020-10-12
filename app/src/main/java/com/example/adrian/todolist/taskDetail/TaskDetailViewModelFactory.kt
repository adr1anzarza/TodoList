package com.example.adrian.todolist.taskDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adrian.todolist.database.TaskDatabaseDAO

class TaskDetailViewModelFactory(
    private val sleepNightKey: Long,
    private val dataSource: TaskDatabaseDAO) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
            return TaskDetailViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}