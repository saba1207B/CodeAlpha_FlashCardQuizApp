package com.example.data

import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val flashcardDao: FlashcardDao) {
    val allFlashcards: Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()

    suspend fun insert(flashcard: Flashcard): Long {
        return flashcardDao.insertFlashcard(flashcard)
    }

    suspend fun update(flashcard: Flashcard) {
        flashcardDao.updateFlashcard(flashcard)
    }

    suspend fun delete(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }

    suspend fun deleteById(id: Long) {
        flashcardDao.deleteFlashcardById(id)
    }

    suspend fun insertAll(flashcards: List<Flashcard>) {
        flashcardDao.insertAll(flashcards)
    }
}
