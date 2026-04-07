package com.example.dictionaryapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordListDao {
    @Insert
    suspend fun insertList(list: WordList)

    @Query("SELECT * FROM word_lists ORDER BY createdAt DESC")
    fun getAllLists(): Flow<List<WordList>>

    @Query("DELETE FROM word_lists WHERE id = :listId")
    suspend fun deleteListById(listId: Long)

    @Query("DELETE FROM words WHERE list_id = :listId")
    suspend fun deleteWordsByListId(listId: Long)
}