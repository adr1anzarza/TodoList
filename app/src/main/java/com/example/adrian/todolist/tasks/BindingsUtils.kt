package com.example.adrian.todolist.tasks

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.adrian.todolist.database.Task

@BindingAdapter("descriptionTaskString")
fun TextView.setDescriptionTaskString(item: Task?) {
    item?.let {
        text = item.descriptionTask
    }
}
@BindingAdapter("titleTaskString")
fun TextView.setTitleTaskString(item: Task?) {
    item?.let {
        text = item.titleTask
    }
}