package com.example.adrian.todolist.taskDetail

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
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

    private val _navigateToTaskFragment =  MutableLiveData<Boolean?>()

    val navigateToTaskFragment: LiveData<Boolean?> = _navigateToTaskFragment

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean> = _showSnackbarEvent

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

    fun doneNavigating() {
        _navigateToTaskFragment.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun onUpdateTask() {
        if(taskTitleWord == null || taskDescriptionWord == null){
            _showSnackbarEvent.value = true
        } else {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    if (database.get(taskKey) == null) {
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
    }

    fun onClearItem() {
        uiScope.launch {
            clearTaskWithId()
            lastTask.value = null
        }
        //_showSnackbarEvent.value = true
        _navigateToTaskFragment.value = true
    }

    private suspend fun clearTaskWithId() {
        withContext(Dispatchers.IO) {
            database.deleteWithId(taskKey)
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

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}