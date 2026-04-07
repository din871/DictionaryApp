package com.example.dictionaryapp

class Repository(
    private val wordListDao: WordListDao,
    private val wordDao: WordDao
) {
    suspend fun addList(name: String) = wordListDao.insertList(WordList(name = name))
    fun getAllLists() = wordListDao.getAllLists()

    suspend fun addWord(word: String, translation: String, listId: Long) =
        wordDao.insertWord(Word(word = word, translation = translation, listId = listId))

    fun getWordsForList(listId: Long) = wordDao.getWordsForList(listId)

    suspend fun deleteList(listId: Long) {
        wordListDao.deleteWordsByListId(listId)
        wordListDao.deleteListById(listId)
    }

    suspend fun deleteWord(wordId: Long) = wordDao.deleteWordById(wordId)
}