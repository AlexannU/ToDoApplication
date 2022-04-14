package com.example.cleantodo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleantodo.database.dao.ToDoDao
import com.example.cleantodo.database.entities.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase :RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}