package com.example.cleantodo.database

import androidx.lifecycle.LiveData
import com.example.cleantodo.database.dao.ToDoDao
import com.example.cleantodo.database.entities.ToDoEntity
import com.example.cleantodo.models.ToDoItem
import com.example.cleantodo.models.toBaseModel
import com.example.cleantodo.models.toEntity

interface LocalDatabaseRepository {
    suspend fun getAllDo(): List<ToDoItem>
    suspend fun insertDo(toDoItem: ToDoItem)
    suspend fun delete(toDoItem: ToDoItem)
    suspend fun update(toDoItem: ToDoItem)
}

class LocalDatabaseRepositoryImpl(private val database: LocalDatabase):LocalDatabaseRepository{
    private val toDoDao = database.toDoDao()

    override suspend fun getAllDo(): List<ToDoItem> {
        return toDoDao.getAllDo().map {
            it.toBaseModel()
        }
    }

    override suspend fun insertDo(toDoItem: ToDoItem) {
        toDoDao.insertDo(toDoItem.toEntity())
    }

    override suspend fun delete(toDoItem: ToDoItem) {
        toDoDao.deleteDo(toDoItem.toEntity())
    }

    override suspend fun update(toDoItem: ToDoItem) {
        toDoDao.update(toDoItem.toEntity())
    }

}