package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursePathScreen(
    onLessonSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("مسار التعلم", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Text("→", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 32.dp)
            ) {
                item {
                    UnitHeader(number = 1, title = "الأساسيات", description = "تعلم التحيات والأرقام البسيطة")
                    Spacer(modifier = Modifier.height(24.dp))
                }

                items(5) { index ->
                    val isLocked = index > 1
                    val isCompleted = index < 1
                    
                    PathNode(
                        label = "الدرس ${index + 1}",
                        isLocked = isLocked,
                        isCompleted = isCompleted,
                        onClick = { if (!isLocked) onLessonSelect("lesson_$index") }
                    )
                    
                    if (index < 4) {
                        VerticalPathLine(isLocked = index >= 1)
                    }
                }
            }
        }
    }
}

@Composable
fun UnitHeader(number: Int, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("الوحدة $number", color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White)
            Text(description, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }
    }
}

@Composable
fun PathNode(label: String, isLocked: Boolean, isCompleted: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = when {
                    isCompleted -> Color(0xFFBBF7D0)
                    isLocked -> Color(0xFFE5E7EB)
                    else -> MaterialTheme.colorScheme.primary
                }
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = when {
                    isCompleted -> "✅"
                    isLocked -> "🔒"
                    else -> "📖"
                },
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = if (isLocked) Color.Gray else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun VerticalPathLine(isLocked: Boolean) {
    Box(
        modifier = Modifier
            .width(8.dp)
            .height(40.dp)
            .background(
                if (isLocked) Color(0xFFE5E7EB) else Color(0xFFBBF7D0),
                RoundedCornerShape(4.dp)
            )
    )
}
