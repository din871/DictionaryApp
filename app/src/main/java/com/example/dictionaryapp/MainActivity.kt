package com.example.dictionaryapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация базы данных и репозитория
        val database = AppDatabase.getInstance(this)
        val repository = Repository(database.wordListDao(), database.wordDao())

        // Создание ViewModel
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        // Настройка RecyclerView
        val rvLists = findViewById<RecyclerView>(R.id.rvLists)
        rvLists.layoutManager = LinearLayoutManager(this)

        // Адаптер с обработчиками клика (открыть) и долгого нажатия (удалить)
        adapter = ListAdapter(
            onClick = { listId, listName ->
                val intent = WordListActivity.newIntent(this, listId, listName)
                startActivity(intent)
            },
            onDelete = { listId ->
                // Диалог подтверждения удаления
                AlertDialog.Builder(this)
                    .setTitle("Удалить список?")
                    .setMessage("Все слова в этом списке будут удалены безвозвратно.")
                    .setPositiveButton("Удалить") { _, _ ->
                        viewModel.deleteList(listId)
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
        )
        rvLists.adapter = adapter

        // Наблюдение за списками слов
        viewModel.allLists.observe(this) { lists ->
            adapter.submitList(lists)
        }

        // Кнопка добавления нового списка
        val fabAddList = findViewById<FloatingActionButton>(R.id.fabAddList)
        fabAddList.setOnClickListener {
            showAddListDialog()
        }
    }

    private fun showAddListDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Новый список слов")
            .setView(editText)
            .setPositiveButton("Создать") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.addList(name)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}