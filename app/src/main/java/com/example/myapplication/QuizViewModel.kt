package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class QuizState(
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val hearts: Int = 4,
    val xpEarned: Int = 0,
    val isChecked: Boolean = false,
    val selectedOption: Int? = null,
    val isCorrect: Boolean = false,
    val isFinished: Boolean = false
)

class QuizViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuizState())
    val uiState: StateFlow<QuizState> = _uiState

    fun selectOption(index: Int) {
        if (!_uiState.value.isChecked) {
            _uiState.value = _uiState.value.copy(selectedOption = index)
        }
    }

    fun checkAnswer() {
        val currentState = _uiState.value
        if (currentState.selectedOption == null) return

        // In a real app, logic would check against correct answer data
        val isCorrect = currentState.selectedOption == 0 
        
        _uiState.value = currentState.copy(
            isChecked = true,
            isCorrect = isCorrect,
            hearts = if (isCorrect) currentState.hearts else currentState.hearts - 1,
            xpEarned = if (isCorrect) currentState.xpEarned + 10 else currentState.xpEarned
        )
    }

    fun nextStep() {
        val currentState = _uiState.value
        if (currentState.currentStep < currentState.totalSteps) {
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
