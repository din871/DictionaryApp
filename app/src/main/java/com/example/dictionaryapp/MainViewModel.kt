package com.example.dictionaryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val allLists: LiveData<List<WordList>> = repository.getAllLists().asLiveData()

    fun addList(name: String) {
        viewModelScope.launch {
            repository.addList(name)
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }
}