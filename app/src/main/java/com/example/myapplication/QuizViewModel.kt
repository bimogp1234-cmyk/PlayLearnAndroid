package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentStep: Int = 0,
    val totalSteps: Int = 0,
    val hearts: Int = 5,
    val xpEarned: Int = 0,
    val isChecked: Boolean = false,
    val selectedOption: Int? = null,
    val isCorrect: Boolean = false,
    val isFinished: Boolean = false,
    val isLoading: Boolean = false
)

class QuizViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(QuizState())
    val uiState: StateFlow<QuizState> = _uiState

    fun loadQuiz(lessonId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val snapshot = db.collection("questions")
                    .whereEqualTo("lessonId", lessonId)
                    .get()
                    .await()
                
                val questions = snapshot.toObjects(Question::class.java)
                _uiState.value = _uiState.value.copy(
                    questions = questions,
                    totalSteps = questions.size,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectOption(index: Int) {
        if (!_uiState.value.isChecked) {
            _uiState.value = _uiState.value.copy(selectedOption = index)
        }
    }

    fun checkAnswer() {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentStep) ?: return
        if (currentState.selectedOption == null) return

        val selectedText = currentQuestion.answers.getOrNull(currentState.selectedOption)
        val isCorrect = selectedText == currentQuestion.correctAnswer
        
        _uiState.value = currentState.copy(
            isChecked = true,
            isCorrect = isCorrect,
            hearts = if (isCorrect) currentState.hearts else currentState.hearts - 1,
            xpEarned = if (isCorrect) currentState.xpEarned + 15 else currentState.xpEarned
        )
    }

    fun nextStep() {
        val currentState = _uiState.value
        if (currentState.currentStep < currentState.totalSteps - 1 && currentState.hearts > 0) {
            _uiState.value = currentState.copy(
                currentStep = currentState.currentStep + 1,
                isChecked = false,
                selectedOption = null
            )
        } else {
            _uiState.value = currentState.copy(isFinished = true)
        }
    }
}
