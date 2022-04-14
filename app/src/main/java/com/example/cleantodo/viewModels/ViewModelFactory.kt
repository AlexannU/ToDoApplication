package com.example.cleantodo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cleantodo.database.LocalDatabaseRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val localDatabaseRepository: LocalDatabaseRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return  MainViewModel(localDatabaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
     }
}