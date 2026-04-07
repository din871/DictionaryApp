package com.example.dictionaryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordListViewModel(
    private val repository: Repository,
    private val listId: Long
) : ViewModel() {
    val words: LiveData<List<Word>> = repository.getWordsForList(listId).asLiveData()

    fun addWord(word: String, translation: String) {
        viewModelScope.launch {
            repository.addWord(word, translation, listId)
        }
    }

    fun deleteWord(wordId: Long) {
        viewModelScope.launch {
            repository.deleteWord(wordId)
        }
    }
}