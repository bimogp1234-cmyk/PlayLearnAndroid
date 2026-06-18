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
import com.example.myapplication.ui.theme.*

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
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    
    val bgColor = when {
        isCorrect -> PL_GreenSoft
        isWrong -> PL_RedSoft
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surface
    }

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        color = bgColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
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
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun WordPair(text: String, isSelected: Boolean, isMatched: Boolean, onClick: () -> Unit) {
    val borderColor = when {
        isMatched -> PL_Green
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        color = if (isMatched) PL_GreenSoft else MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, fontWeight = FontWeight.Bold, color = if (isMatched) PL_GreenDark else MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun FeedbackSection(isCorrect: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isCorrect) PL_GreenSoft else PL_RedSoft, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(if (isCorrect) "✅" else "❌", fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                if (isCorrect) "إجابة صحيحة!" else "إجابة خاطئة",
                fontWeight = FontWeight.Black,
                color = if (isCorrect) PL_GreenDark else PL_Red
            )
            if (!isCorrect) {
                Text("حاول مرة أخرى في السؤال التالي", fontSize = 12.sp, color = PL_Red)
            }
        }
    }
}
