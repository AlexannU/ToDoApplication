package com.example.cleantodo.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    val title:String,
    @ColumnInfo(name = "is_checked")
    var isChecked:Boolean
)
