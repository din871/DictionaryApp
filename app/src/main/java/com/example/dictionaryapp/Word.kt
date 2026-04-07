package com.example.dictionaryapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val translation: String,
    val addedDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "list_id")
    val listId: Long
)