package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.PL_Green
import com.example.myapplication.ui.theme.PL_Red

enum class QuizType {
    MULTIPLE_CHOICE, IMAGE_SELECTION, WORD_MATCHING
}

@Composable
fun QuizScreen(
    onFinish: (Int) -> Unit,
    onBack: () -> Unit,
    quizViewModel: QuizViewModel = viewModel()
) {
    val uiState by quizViewModel.uiState.collectAsState()
    
    val quizType = when(uiState.currentStep) {
        1 -> QuizType.IMAGE_SELECTION
        2 -> QuizType.MULTIPLE_CHOICE
        else -> QuizType.WORD_MATCHING
    }

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            onFinish(uiState.xpEarned)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                QuizTopBar(
                    progress = uiState.currentStep / uiState.totalSteps.toFloat(), 
                    hearts = uiState.hearts,
                    onBack = onBack
                )
            },
            bottomBar = {
                QuizBottomBar(
                    buttonText = if (uiState.isChecked) (if (uiState.currentStep < uiState.totalSteps) "متابعة" else "إنهاء") else "تحقق",
                    isEnabled = uiState.selectedOption != null,
                    isChecked = uiState.isChecked,
                    onClick = {
                        if (uiState.isChecked) {
                            quizViewModel.nextStep()
                        } else {
                            quizViewModel.checkAnswer()
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                when (quizType) {
                    QuizType.IMAGE_SELECTION -> ImageQuizContent(
                        selectedOption = uiState.selectedOption,
                        isChecked = uiState.isChecked,
                        onSelect = { quizViewModel.selectOption(it) }
                    )
                    QuizType.MULTIPLE_CHOICE -> MultipleChoiceContent(
                        selectedOption = uiState.selectedOption,
                        isChecked = uiState.isChecked,
                        onSelect = { quizViewModel.selectOption(it) }
                    )
                    QuizType.WORD_MATCHING -> WordMatchContent()
                }

                if (uiState.isChecked && quizType != QuizType.WORD_MATCHING) {
                    Spacer(modifier = Modifier.height(32.dp))
                    FeedbackSection(isCorrect = uiState.isCorrect)
                }
            }
        }
    }
}

@Composable
fun QuizTopBar(progress: Float, hearts: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) { Text("✕", fontSize = 20.sp, color = Color.Gray, fontWeight = FontWeight.Bold) }
        Box(modifier = Modifier.weight(1f).height(11.dp).background(Color(0xFFEAE6DC), RoundedCornerShape(7.dp))) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(PL_Green, RoundedCornerShape(7.dp))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 4.dp)) {
            Text("❤️", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(hearts.toString(), fontWeight = FontWeight.Bold, color = PL_Red, fontSize = 13.sp)
        }
    }
}

@Composable
fun QuizBottomBar(buttonText: String, isEnabled: Boolean, isChecked: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(20.dp).height(56.dp),
            shape = RoundedCornerShape(22.dp),
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isChecked) PL_Green else PL_Green,
                disabledContainerColor = Color(0xFFD9D4C8)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(text = buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ImageQuizContent(selectedOption: Int?, isChecked: Boolean, onSelect: (Int) -> Unit) {
    Text("اختر الصورة الصحيحة", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .background(Color(0xFFFBF7F0), RoundedCornerShape(18.dp))
            .padding(26.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("تفاحة", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFF1F2933))
    }
    
    val options = listOf("🍎", "📚", "🍊", "🍇")
    val labels = listOf("تفاحة", "كتاب", "برتقال", "عنب")
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(4) { index ->
            ImageOption(
                emoji = options[index],
                label = labels[index],
                isSelected = selectedOption == index,
                isCorrect = isChecked && index == 0,
                isWrong = isChecked && selectedOption == index && index != 0,
                onClick = { onSelect(index) }
            )
        }
    }
}

@Composable
fun MultipleChoiceContent(selectedOption: Int?, isChecked: Boolean, onSelect: (Int) -> Unit) {
    Text("اختر الترجمة الصحيحة", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .background(Color(0xFFFBF7F0), RoundedCornerShape(18.dp))
            .padding(26.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Hello", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFF1F2933))
    }
    
    val options = listOf("مرحباً", "وداعاً", "شكراً", "نعم")
    options.forEachIndexed { index, option ->
        QuizOption(
            text = option,
            isSelected = selectedOption == index,
            isCorrect = isChecked && index == 0,
            isWrong = isChecked && selectedOption == index && index != 0,
            onClick = { onSelect(index) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun WordMatchContent() {
    Text("طابق الكلمات بالترجمة الصحيحة", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    Spacer(modifier = Modifier.height(32.dp))
    
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            WordPair("Book", isSelected = false, isMatched = true, onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            WordPair("School", isSelected = true, isMatched = false, onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            WordPair("Pen", isSelected = false, isMatched = false, onClick = {})
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            WordPair("كتاب", isSelected = false, isMatched = true, onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            WordPair("مدرسة", isSelected = false, isMatched = false, onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            WordPair("قلم", isSelected = false, isMatched = false, onClick = {})
        }
    }
}
