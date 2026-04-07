package com.example.dictionaryapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert
    suspend fun insertWord(word: Word)

    @Query("SELECT * FROM words WHERE list_id = :listId ORDER BY addedDate DESC")
    fun getWordsForList(listId: Long): Flow<List<Word>>

    @Query("DELETE FROM words WHERE id = :wordId")
    suspend fun deleteWordById(wordId: Long)
}