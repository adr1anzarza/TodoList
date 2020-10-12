package com.example.adrian.todolist.taskDetail

import android.text.Editable
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

    var lastTask = MutableLiveData<Task?>()

    var title = MutableLiveData<String>()

    @Bindable var taskTitleWord : String? = title.value
        set(value) {
            if (field != value) {
                field = value
            }
        }

    @Bindable var taskDescriptionWord : String? = title.value
        set(value) {
            if (field != value) {
                field = value
            }
        }

    @Bindable
    fun getTask() = task

    init {
        task = database.getTaskWithId(taskKey)
        title.value = task.value?.titleTask

        initializeLastTask()
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

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }

    private val _navigateToTaskFragment =  MutableLiveData<Boolean?>()

    val navigateToTaskFragment: LiveData<Boolean?> = _navigateToTaskFragment

    fun doneNavigating() {
        _navigateToTaskFragment.value = null
    }

    fun onUpdateTask() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if(database.get(taskKey)==null){
                    val lastTask = lastTask
                    lastTask.value?.titleTask = taskTitleWord.toString()
                    lastTask.value?.descriptionTask = taskDescriptionWord.toString()
                    database.update(lastTask.value!!)
                } else {
                    val lastTask = database.get(taskKey) ?: return@withContext
                    lastTask.titleTask = taskTitleWord.toString()
                    lastTask.descriptionTask = taskDescriptionWord.toString()
                    database.update(lastTask)
                }
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