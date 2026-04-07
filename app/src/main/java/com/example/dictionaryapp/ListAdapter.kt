package com.example.dictionaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ListAdapter(
    private val onClick: (Long, String) -> Unit,
    private val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var items = listOf<WordList>()

    fun submitList(newList: List<WordList>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = items[position]
        holder.bind(list)
        // Клик для открытия списка слов
        holder.itemView.setOnClickListener { onClick(list.id, list.name) }
        // Долгое нажатие для удаления
        holder.itemView.setOnLongClickListener {
            onDelete(list.id)
            true
        }
    }

    override fun getItemCount() = items.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(list: WordList) {
            text1.text = list.name
            val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(list.createdAt)
            text2.text = date
        }
    }
}