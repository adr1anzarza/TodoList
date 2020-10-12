package com.example.adrian.todolist.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.adrian.todolist.database.Task
import com.example.adrian.todolist.database.TaskDatabaseDAO
import kotlinx.coroutines.*

class TaskViewModel(val database: TaskDatabaseDAO,
                    application: Application) : AndroidViewModel(application){

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastTask = MutableLiveData<Task?>()

    private var _tasks = database.getAllTasks()

    val tasks : LiveData<List<Task>> = _tasks

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean> = _showSnackbarEvent

    private val _navigateToTaskDetail = MutableLiveData<Long>()
    val navigateToTaskDetail= _navigateToTaskDetail


    init {
        initializeLastTask()
    }

    fun onTaskClicked(taskId: Long){
        _navigateToTaskDetail.value = taskId
    }

    fun onTaskDetailNavigated() {
        _navigateToTaskDetail.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    private fun initializeLastTask() {
        uiScope.launch {
            lastTask.value = getLastTaskFromDB()
        }
    }

    private suspend fun getLastTaskFromDB(): Task? {
        return withContext(Dispatchers.IO){
            var task = database.getLastTask()
            task
        }
    }

    fun onStartTask() {
        uiScope.launch {
            val newTask = Task("", "")
            insert(newTask)
            lastTask.value = getLastTaskFromDB()
            onTaskClicked(newTask.taskId)
        }

    }

    suspend fun insert(night: Task){
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            lastTask.value = null
        }
        _showSnackbarEvent.value = true
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
}