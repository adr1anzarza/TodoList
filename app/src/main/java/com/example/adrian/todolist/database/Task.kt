package com.example.adrian.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_task_table")
data class Task  @JvmOverloads constructor(
    @ColumnInfo(name = "title_task")
    var titleTask: String ,

    @ColumnInfo(name = "description_task")
    var descriptionTask: String,

    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L
)