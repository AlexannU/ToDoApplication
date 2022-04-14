package com.example.cleantodo.models

import com.example.cleantodo.database.entities.ToDoEntity

data class ToDoItem(
    var title:String,
    var isChecked:Boolean,
    var isConfirm:Boolean,
)
fun ToDoItem.toEntity():ToDoEntity{
    return ToDoEntity(
        title = this.title,
        isChecked = this.isChecked
    )
}
fun ToDoEntity.toBaseModel():ToDoItem{
    return  ToDoItem(
        title = this.title,
        isChecked = this.isChecked,
        isConfirm = true
    )
}