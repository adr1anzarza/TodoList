package com.example.adrian.todolist.taskDetail

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adrian.todolist.database.Task
import com.example.adrian.todolist.database.TaskDatabaseDAO
import kotlinx.coroutines.*

class TaskDetailViewModel(
    private val taskKey: Long = 0L,
    dataSource: TaskDatabaseDAO) : ViewModel() , Observable {

    val database = dataSource

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var task: LiveData<Task>

    var title = MutableLiveData<String>()

    @Bindable
    fun getTask() = task

    @Bindable
    fun getTitleTask() : String? {
        return task.value?.titleTask
    }

    init {
        task = database.getTaskWithId(taskKey)
        title.value = "task.value?.titleTask"
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }

    private val _navigateToTaskFragment =  MutableLiveData<Boolean?>()

    val navigateToTaskFragment: LiveData<Boolean?> = _navigateToTaskFragment

    fun doneNavigating() {
        _navigateToTaskFragment.value = null
    }

    fun onUpdateTask(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val lastTask = database.get(taskKey) ?: return@withContext
                lastTask.titleTask = quality.toString()
                lastTask.descriptionTask = quality.toString()
                database.update(lastTask)
            }
            _navigateToTaskFragment.value = true
        }
    }

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        //callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        //callbacks.remove(callback)
    }
}