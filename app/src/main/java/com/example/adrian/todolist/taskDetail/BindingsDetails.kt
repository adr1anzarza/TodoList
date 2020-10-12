package com.example.adrian.todolist.taskDetail

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.adrian.todolist.database.Task

@BindingAdapter("setTaskTitleText")
fun EditText.setTaskTitleText(task: Task?) {
    task?.let {
        post {setText(task.titleTask)}

    }
}

@BindingAdapter("setTaskDescriptionText")
fun EditText.setTaskDescriptionText(task: Task?) {
    task?.let {
        setText(task.descriptionTask)
    }
}