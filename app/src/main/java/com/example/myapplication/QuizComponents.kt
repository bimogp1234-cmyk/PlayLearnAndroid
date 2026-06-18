package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PL_Green
import com.example.myapplication.ui.theme.PL_Red

@Composable
fun QuizOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isCorrect -> PL_Green
        isWrong -> PL_Red
        isSelected -> Color(0xFF4C6FFF) // PL_Blue
        else -> Color(0xFFEAE6DC) // PL_Line
    }
    
    val bgColor = when {
        isCorrect -> Color(0xFFE8F8EE) // PL_GreenSoft
        isWrong -> Color(0xFFFDEAEA) // PL_RedSoft
        isSelected -> Color(0xFFEEF1FF) // PL_BlueSoft
        else -> Color.White
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.5.dp, borderColor),
        color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageOption(
    emoji: String,
    label: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isCorrect -> PL_Green
        isWrong -> PL_Red
        isSelected -> Color(0xFF4C6FFF)
        else -> Color(0xFFEAE6DC)
    }

    val bgColor = when {
        isCorrect -> Color(0xFFE8F8EE)
        isWrong -> Color(0xFFFDEAEA)
        isSelected -> Color(0xFFEEF1FF)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.5.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 44.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1F2933))
        }
    }
}

@Composable
fun WordPair(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isMatched) Color(0xFFE8F8EE) else if (isSelected) Color(0xFF1CB854) else Color(0xFFEAE6DC)
        ),
        color = if (isMatched) Color(0xFFE8F8EE) else if (isSelected) Color(0xFFE8F8EE) else Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isMatched) Color(0xFF129142) else Color.Black
        )
    }
}

@Composable
fun FeedbackSection(isCorrect: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isCorrect) Color(0xFFE8F8EE) else Color(0xFFFDEAEA),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = if (isCorrect) " أحسنت! إجابة صحيحة ✨" else "للاسف، حاول مرة أخرى 💔",
            color = if (isCorrect) Color(0xFF129142) else Color(0xFFF44B4B),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}
