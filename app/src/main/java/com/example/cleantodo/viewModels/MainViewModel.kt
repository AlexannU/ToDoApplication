package com.example.cleantodo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleantodo.database.LocalDatabaseRepository
import com.example.cleantodo.models.ToDoItem
import kotlinx.coroutines.launch

class MainViewModel(
        private val localDatabaseRepository: LocalDatabaseRepository
    ) : ViewModel() {
    val toDoList: MutableLiveData<MutableList<ToDoItem>> by lazy {
        MutableLiveData<MutableList<ToDoItem>>(mutableListOf())
    }
    private var _toDoMainList = mutableListOf<ToDoItem>()

    fun getDatabaseData() {
        viewModelScope.launch {
            _toDoMainList = localDatabaseRepository.getAllDo() as MutableList<ToDoItem>
            toDoList.value = _toDoMainList
        }
    }

    fun addEmptyDo(toDoItem: ToDoItem) {
        _toDoMainList.add(toDoItem)
        toDoList.value = _toDoMainList

    }

    fun deleteDo(position: Int) {
        viewModelScope.launch{
            localDatabaseRepository.delete(_toDoMainList[position])
        }
        _toDoMainList.removeAt(position)
        toDoList.value = _toDoMainList
    }

    fun changeStatus(position: Int) {
        _toDoMainList[position].isChecked = !_toDoMainList[position].isChecked
        toDoList.value = _toDoMainList
        viewModelScope.launch {
            localDatabaseRepository.update(_toDoMainList[position])
        }
    }

    fun confirm(position: Int, title: String) {
        _toDoMainList[position].title = title
        _toDoMainList[position].isConfirm = true
        toDoList.value = _toDoMainList
        viewModelScope.launch {
            localDatabaseRepository.insertDo(_toDoMainList[position])
        }

    }
}