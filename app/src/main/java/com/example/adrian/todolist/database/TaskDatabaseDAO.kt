package com.example.adrian.todolist.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDatabaseDAO{

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Query("SELECT * FROM todo_task_table WHERE taskId = :key")
    fun get(key: Long): Task?

    @Query("DELETE FROM todo_task_table")
    fun clear()

    @Query("SELECT * FROM todo_task_table ORDER BY taskId DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM todo_task_table ORDER BY taskId DESC LIMIT 1")
    fun getLastTask(): Task?

    @Query("SELECT * FROM todo_task_table WHERE taskId = :key")
    fun getTaskWithId(key: Long): LiveData<Task>
}
