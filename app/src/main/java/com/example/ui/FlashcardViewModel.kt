package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Flashcard
import com.example.data.FlashcardDatabase
import com.example.data.FlashcardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FlashcardUiState(
    val cards: List<Flashcard> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerShowing: Boolean = false,
    val isEditing: Boolean = false,
    val editingCard: Flashcard? = null,
    val isDeleteConfirmOpen: Boolean = false,
    val cardToDelete: Flashcard? = null,
    val selectedTab: AppTab = AppTab.STUDY,
    val searchQuery: String = "",
    val selectedCategory: String? = null
) {
    val currentCard: Flashcard?
        get() = if (cards.isNotEmpty() && currentIndex in cards.indices) cards[currentIndex] else null

    val totalCards: Int
        get() = cards.size

    val hasPrevious: Boolean
        get() = currentIndex > 0

    val hasNext: Boolean
        get() = currentIndex < cards.size - 1

    val categories: List<String>
        get() = cards.map { it.category.trim() }.filter { it.isNotBlank() }.distinct()

    val filteredCards: List<Flashcard>
        get() = cards.filter { card ->
            val matchesQuery = searchQuery.isBlank() ||
                    card.question.contains(searchQuery, ignoreCase = true) ||
                    card.answer.contains(searchQuery, ignoreCase = true) ||
                    card.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || card.category.equals(selectedCategory, ignoreCase = true)
            matchesQuery && matchesCategory
        }
}

enum class AppTab {
    STUDY,
    DECK
}

class FlashcardViewModel(
    application: Application,
    private val repository: FlashcardRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allFlashcards.collect { allCards ->
                _uiState.update { current ->
                    val safeIndex = if (allCards.isEmpty()) 0 else current.currentIndex.coerceIn(0, allCards.size - 1)
                    current.copy(
                        cards = allCards,
                        currentIndex = safeIndex
                    )
                }
            }
        }
    }

    fun nextCard() {
        _uiState.update { current ->
            if (current.cards.isNotEmpty() && current.currentIndex < current.cards.size - 1) {
                current.copy(
                    currentIndex = current.currentIndex + 1,
                    isAnswerShowing = false
                )
            } else current
        }
    }

    fun previousCard() {
        _uiState.update { current ->
            if (current.currentIndex > 0) {
                current.copy(
                    currentIndex = current.currentIndex - 1,
                    isAnswerShowing = false
                )
            } else current
        }
    }

    fun toggleShowAnswer() {
        _uiState.update { current ->
            current.copy(isAnswerShowing = !current.isAnswerShowing)
        }
    }

    fun setShowAnswer(show: Boolean) {
        _uiState.update { current ->
            current.copy(isAnswerShowing = show)
        }
    }

    fun selectCardIndex(index: Int) {
        _uiState.update { current ->
            val safe = index.coerceIn(0, (current.cards.size - 1).coerceAtLeast(0))
            current.copy(
                currentIndex = safe,
                isAnswerShowing = false,
                selectedTab = AppTab.STUDY
            )
        }
    }

    fun setSelectedTab(tab: AppTab) {
        _uiState.update { current ->
            current.copy(selectedTab = tab)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { current ->
            current.copy(searchQuery = query)
        }
    }

    fun setSelectedCategory(category: String?) {
        _uiState.update { current ->
            current.copy(selectedCategory = category)
        }
    }

    fun openAddDialog() {
        _uiState.update { current ->
            current.copy(
                editingCard = null,
                isEditing = true
            )
        }
    }

    fun openEditDialog(card: Flashcard) {
        _uiState.update { current ->
            current.copy(
                editingCard = card,
                isEditing = true
            )
        }
    }

    fun dismissEditDialog() {
        _uiState.update { current ->
            current.copy(
                editingCard = null,
                isEditing = false
            )
        }
    }

    fun saveCard(id: Long?, question: String, answer: String, category: String) {
        viewModelScope.launch {
            val trimmedQuestion = question.trim()
            val trimmedAnswer = answer.trim()
            val trimmedCategory = category.trim().ifBlank { "General" }

            if (trimmedQuestion.isNotBlank() && trimmedAnswer.isNotBlank()) {
                if (id == null || id == 0L) {
                    val newCard = Flashcard(
                        question = trimmedQuestion,
                        answer = trimmedAnswer,
                        category = trimmedCategory
                    )
                    repository.insert(newCard)
                } else {
                    val existing = _uiState.value.cards.find { it.id == id }
                    val updated = Flashcard(
                        id = id,
                        question = trimmedQuestion,
                        answer = trimmedAnswer,
                        category = trimmedCategory,
                        isMastered = existing?.isMastered ?: false,
                        createdAt = existing?.createdAt ?: System.currentTimeMillis()
                    )
                    repository.update(updated)
                }
            }
            dismissEditDialog()
        }
    }

    fun requestDeleteCard(card: Flashcard) {
        _uiState.update { current ->
            current.copy(
                cardToDelete = card,
                isDeleteConfirmOpen = true
            )
        }
    }

    fun dismissDeleteDialog() {
        _uiState.update { current ->
            current.copy(
                cardToDelete = null,
                isDeleteConfirmOpen = false
            )
        }
    }

    fun confirmDeleteCard() {
        val toDelete = _uiState.value.cardToDelete
        if (toDelete != null) {
            viewModelScope.launch {
                repository.delete(toDelete)
                _uiState.update { current ->
                    val newSize = current.cards.size - 1
                    val newIndex = if (current.currentIndex >= newSize && current.currentIndex > 0) {
                        current.currentIndex - 1
                    } else {
                        current.currentIndex
                    }
                    current.copy(
                        currentIndex = newIndex,
                        isAnswerShowing = false,
                        cardToDelete = null,
                        isDeleteConfirmOpen = false
                    )
                }
            }
        }
    }

    fun toggleMastered(card: Flashcard) {
        viewModelScope.launch {
            repository.update(card.copy(isMastered = !card.isMastered))
        }
    }

    fun resetProgress() {
        _uiState.update { current ->
            current.copy(
                currentIndex = 0,
                isAnswerShowing = false
            )
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = FlashcardDatabase.getDatabase(
                        application,
                        CoroutineScope(Dispatchers.Default)
                    )
                    val repository = FlashcardRepository(database.flashcardDao())
                    return FlashcardViewModel(application, repository) as T
                }
            }
    }
}
