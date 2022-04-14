package com.example.cleantodo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cleantodo.database.entities.ToDoEntity

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo")
    suspend fun getAllDo():List<ToDoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDo(toDoEntity: ToDoEntity)

    @Delete
    suspend fun deleteDo(toDoEntity: ToDoEntity)

    @Update
    suspend fun update(toDoEntity: ToDoEntity)


}