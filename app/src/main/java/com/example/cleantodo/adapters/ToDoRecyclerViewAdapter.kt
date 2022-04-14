package com.example.cleantodo.adapters

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cleantodo.R
import com.example.cleantodo.databinding.ListItemBinding
import com.example.cleantodo.models.ToDoItem

class ToDoRecyclerViewAdapter(
    private val onSwipe: (View,Int) -> Unit,
    private val onChanged: (Int) -> Unit,
    private val onClickDone: (View,Int) -> Unit,
    private val disableAddButton:(Boolean) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    private val differCallback = object : DiffUtil.ItemCallback<ToDoItem>() {
        override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem == newItem
        }

    }

    private val simpleCallback = SwipeHelper { view,position -> onSwipe(view,position) }

    fun attachToRecyclerView(recyclerView: RecyclerView){
        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun setItems(list: List<ToDoItem>){
        val buffer = ArrayList(list)
        differ.submitList(buffer)
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)
        fun bind(toDoItem: ToDoItem,position: Int) {
            binding.doTitle.setText(toDoItem.title)
            binding.doTitle.isEnabled = !toDoItem.isConfirm
            binding.checkbox.isChecked = toDoItem.isChecked
            binding.doTitle.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        onClickDone(binding.doTitle,position)
                        true
                    }
                    else -> false
                }
            }
            binding.doTitle.setOnFocusChangeListener { _, isFocus ->
                disableAddButton(isFocus)
            }
            binding.checkbox.setOnCheckedChangeListener { _, _ ->
                onChanged(recyclerView.getChildLayoutPosition(itemView))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ToDoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ToDoViewHolder).bind(differ.currentList[position],position)
    }

    override fun getItemCount() = differ.currentList.size
}