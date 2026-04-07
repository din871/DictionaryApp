package com.example.dictionaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class WordAdapter(
    private val onDelete: (Long) -> Unit   // параметр – удаление по id слова
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private var words = listOf<Word>()

    fun submitList(newList: List<Word>) {
        words = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
        // Долгое нажатие для удаления
        holder.itemView.setOnLongClickListener {
            onDelete(word.id)
            true
        }
    }

    override fun getItemCount() = words.size

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(word: Word) {
            text1.text = "${word.word} → ${word.translation}"
            val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(word.addedDate)
            text2.text = date
        }
    }
}