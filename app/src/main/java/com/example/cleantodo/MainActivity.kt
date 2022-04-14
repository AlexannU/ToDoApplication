package com.example.cleantodo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cleantodo.adapters.ToDoRecyclerViewAdapter
import com.example.cleantodo.database.LocalDatabase
import com.example.cleantodo.database.LocalDatabaseRepository
import com.example.cleantodo.database.LocalDatabaseRepositoryImpl
import com.example.cleantodo.databinding.ActivityMainBinding
import com.example.cleantodo.models.ToDoItem
import com.example.cleantodo.viewModels.MainViewModel
import com.example.cleantodo.viewModels.ViewModelFactory
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var localDatabaseRepository: LocalDatabaseRepository
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareCommonClasses()

        viewModel.getDatabaseData()


        val adapter = ToDoRecyclerViewAdapter(
            { view,position -> deleteDo(view,position) },
            { position -> viewModel.changeStatus(position) },
            { view, position -> confirmAdded(view, position) },
            { editTextIsFocused -> binding.addButton.isEnabled = !editTextIsFocused }
        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.recyclerView.adapter = adapter
        adapter.attachToRecyclerView(binding.recyclerView)

        viewModel.toDoList.observe(this) {
            adapter.setItems(it)
        }

        binding.addButton.setOnClickListener {


            if (isEmptyDoAlreadyCreate(viewModel.toDoList.value!!)) {
                Toast.makeText(this, R.string.toast_text, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addEmptyDo(
                    ToDoItem(
                        title = "",
                        isChecked = false, isConfirm = false
                    )
                )
            }

        }


    }

    private fun prepareCommonClasses(){
        val database = Room.databaseBuilder(
            applicationContext, LocalDatabase::class.java, "local_database"
        ).build()

        localDatabaseRepository = LocalDatabaseRepositoryImpl(database)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(localDatabaseRepository)
        )[MainViewModel::class.java]
    }

    private fun isEmptyDoAlreadyCreate(list: List<ToDoItem>): Boolean {
        if (list.isNotEmpty()) {
            if (list[list.lastIndex].title == "") {
                return true
            }
        }
        return false
    }

    private fun deleteDo(view: View,position: Int){
        viewModel.deleteDo(position)
        hideKeyboard(view)
    }

    private fun confirmAdded(view: View, position: Int) {
        viewModel.confirm(position, (view as EditText).text.toString())
        hideKeyboard(view)
        view.isEnabled = false
        view.isFocusable = false

    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}