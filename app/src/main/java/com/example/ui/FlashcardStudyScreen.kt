package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CardEditDialog
import com.example.ui.components.DeckListView
import com.example.ui.components.DeleteConfirmDialog
import com.example.ui.components.FlashcardView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardStudyScreen(
    viewModel: FlashcardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Quiz,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Text(
                            text = "Flashcard Quiz",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.openAddDialog() },
                        modifier = Modifier.testTag("top_add_card_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Flashcard",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("fab_add_card")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "New Card",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Mode Tabs: Study Quiz vs All Cards
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = uiState.selectedTab == AppTab.STUDY,
                    onClick = { viewModel.setSelectedTab(AppTab.STUDY) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text("Study Quiz")
                        }
                    },
                    modifier = Modifier.testTag("tab_study")
                )
                Tab(
                    selected = uiState.selectedTab == AppTab.DECK,
                    onClick = { viewModel.setSelectedTab(AppTab.DECK) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Style,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text("All Cards (${uiState.cards.size})")
                        }
                    },
                    modifier = Modifier.testTag("tab_deck")
                )
            }

            // Main Content Body based on selected tab
            when (uiState.selectedTab) {
                AppTab.STUDY -> {
                    StudyQuizContent(
                        uiState = uiState,
                        onToggleShowAnswer = { viewModel.toggleShowAnswer() },
                        onNext = { viewModel.nextCard() },
                        onPrevious = { viewModel.previousCard() },
                        onEdit = { card -> viewModel.openEditDialog(card) },
                        onDelete = { card -> viewModel.requestDeleteCard(card) },
                        onReset = { viewModel.resetProgress() },
                        onAddNew = { viewModel.openAddDialog() },
                        modifier = Modifier.weight(1f)
                    )
                }
                AppTab.DECK -> {
                    DeckListView(
                        cards = uiState.filteredCards,
                        categories = uiState.categories,
                        searchQuery = uiState.searchQuery,
                        selectedCategory = uiState.selectedCategory,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onCategorySelect = { viewModel.setSelectedCategory(it) },
                        onCardClick = { index -> viewModel.selectCardIndex(index) },
                        onEditCard = { card -> viewModel.openEditDialog(card) },
                        onDeleteCard = { card -> viewModel.requestDeleteCard(card) },
                        onAddNewCard = { viewModel.openAddDialog() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    // Dialogs
    if (uiState.isEditing) {
        CardEditDialog(
            card = uiState.editingCard,
            existingCategories = uiState.categories,
            onDismiss = { viewModel.dismissEditDialog() },
            onSave = { id, q, a, cat -> viewModel.saveCard(id, q, a, cat) }
        )
    }

    if (uiState.isDeleteConfirmOpen) {
        DeleteConfirmDialog(
            card = uiState.cardToDelete,
            onDismiss = { viewModel.dismissDeleteDialog() },
            onConfirm = { viewModel.confirmDeleteCard() }
        )
    }
}

@Composable
private fun StudyQuizContent(
    uiState: FlashcardUiState,
    onToggleShowAnswer: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onEdit: (com.example.data.Flashcard) -> Unit,
    onDelete: (com.example.data.Flashcard) -> Unit,
    onReset: () -> Unit,
    onAddNew: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.cards.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "No Flashcards Available",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your own flashcards to start studying and testing your knowledge.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onAddNew,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("empty_state_add_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add First Flashcard")
                }
            }
        }
        return
    }

    val currentCard = uiState.currentCard ?: return
    val total = uiState.totalCards
    val currentNum = uiState.currentIndex + 1
    val progress = (currentNum.toFloat() / total.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Study Progress Indicator Bar & Counter Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.testTag("card_counter_badge")
            ) {
                Text(
                    text = "Card $currentNum of $total",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            IconButton(
                onClick = onReset,
                modifier = Modifier.testTag("reset_progress_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart Deck",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Linear Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .testTag("quiz_progress_bar"),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Center Flashcard
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = 600.dp),
            contentAlignment = Alignment.Center
        ) {
            FlashcardView(
                card = currentCard,
                isAnswerShowing = uiState.isAnswerShowing,
                onToggleShowAnswer = onToggleShowAnswer,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Primary Navigation Buttons ("Previous" and "Next")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            OutlinedButton(
                onClick = onPrevious,
                enabled = uiState.hasPrevious,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("previous_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Previous",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Next Button
            Button(
                onClick = onNext,
                enabled = uiState.hasNext,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("next_button")
            ) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card Management Actions (Edit & Delete for the active flashcard)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onEdit(currentCard) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("edit_current_card_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit Card", style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = { onDelete(currentCard) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.testTag("delete_current_card_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
