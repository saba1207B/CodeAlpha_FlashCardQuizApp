package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.data.Flashcard

@Composable
fun CardEditDialog(
    card: Flashcard?,
    existingCategories: List<String>,
    onDismiss: () -> Unit,
    onSave: (id: Long?, question: String, answer: String, category: String) -> Unit
) {
    val isEditMode = card != null
    var question by remember { mutableStateOf(card?.question ?: "") }
    var answer by remember { mutableStateOf(card?.answer ?: "") }
    var category by remember { mutableStateOf(card?.category ?: "General") }
    var questionError by remember { mutableStateOf(false) }
    var answerError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "Edit Flashcard" else "New Flashcard",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = question,
                    onValueChange = {
                        question = it
                        if (it.isNotBlank()) questionError = false
                    },
                    label = { Text("Question (Front)") },
                    placeholder = { Text("e.g. What is the powerhouse of the cell?") },
                    leadingIcon = {
                        Icon(Icons.Default.Quiz, contentDescription = null)
                    },
                    isError = questionError,
                    supportingText = {
                        if (questionError) {
                            Text("Question cannot be empty")
                        } else {
                            Text("${question.length} characters")
                        }
                    },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("question_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = answer,
                    onValueChange = {
                        answer = it
                        if (it.isNotBlank()) answerError = false
                    },
                    label = { Text("Answer (Back)") },
                    placeholder = { Text("e.g. Mitochondria") },
                    leadingIcon = {
                        Icon(Icons.Default.QuestionAnswer, contentDescription = null)
                    },
                    isError = answerError,
                    supportingText = {
                        if (answerError) {
                            Text("Answer cannot be empty")
                        } else {
                            Text("${answer.length} characters")
                        }
                    },
                    minLines = 2,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("answer_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category / Topic") },
                    placeholder = { Text("e.g. Science, Biology, History") },
                    leadingIcon = {
                        Icon(Icons.Default.Category, contentDescription = null)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validQ = question.isNotBlank()
                    val validA = answer.isNotBlank()
                    questionError = !validQ
                    answerError = !validA

                    if (validQ && validA) {
                        onSave(card?.id, question, answer, category)
                    }
                },
                modifier = Modifier.testTag("save_card_button")
            ) {
                Text(if (isEditMode) "Save Changes" else "Add Flashcard")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_card_button")
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
