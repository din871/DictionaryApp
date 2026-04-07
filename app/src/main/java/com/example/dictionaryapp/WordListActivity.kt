package com.example.dictionaryapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WordListActivity : AppCompatActivity() {

    private lateinit var viewModel: WordListViewModel
    private lateinit var adapter: WordAdapter
    private var listId: Long = 0L

    companion object {
        fun newIntent(context: Context, listId: Long, listName: String): Intent {
            return Intent(context, WordListActivity::class.java).apply {
                putExtra("LIST_ID", listId)
                putExtra("LIST_NAME", listName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        listId = intent.getLongExtra("LIST_ID", -1)
        val listName = intent.getStringExtra("LIST_NAME") ?: "Слова"

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = listName

        val database = AppDatabase.getInstance(this)
        val repository = Repository(database.wordListDao(), database.wordDao())
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return WordListViewModel(repository, listId) as T
            }
        })[WordListViewModel::class.java]

        val rv = findViewById<RecyclerView>(R.id.rvWords)
        rv.layoutManager = LinearLayoutManager(this)

        // Адаптер с обработчиком долгого нажатия (удаление слова)
        adapter = WordAdapter(
            onDelete = { wordId ->
                AlertDialog.Builder(this)
                    .setTitle("Удалить слово?")
                    .setMessage("Вы уверены, что хотите удалить это слово?")
                    .setPositiveButton("Удалить") { _, _ ->
                        viewModel.deleteWord(wordId)
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
        )
        rv.adapter = adapter

        viewModel.words.observe(this) { words ->
            adapter.submitList(words)
        }

        findViewById<FloatingActionButton>(R.id.fabAddWord).setOnClickListener {
            showAddWordDialog()
        }
    }

    private fun showAddWordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_word, null)
        val etWord = dialogView.findViewById<EditText>(R.id.etWord)
        val etTranslation = dialogView.findViewById<EditText>(R.id.etTranslation)

        AlertDialog.Builder(this)
            .setTitle("Добавить слово")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val word = etWord.text.toString().trim()
                val translation = etTranslation.text.toString().trim()
                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    viewModel.addWord(word, translation)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}