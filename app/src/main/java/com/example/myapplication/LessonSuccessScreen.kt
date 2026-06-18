package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PL_GoldSoft
import com.example.myapplication.ui.theme.PL_GreenDark
import com.example.myapplication.ui.theme.PL_GreenSoft
import com.example.myapplication.ui.theme.PL_RedSoft

@Composable
fun LessonSuccessScreen(
    xpEarned: Int,
    onContinue: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFBF7F0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Celebration Area (Simplified Treasure Chest)
                Box(
                    modifier = Modifier
                        .size(150.dp, 130.dp)
                        .padding(bottom = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎁", fontSize = 80.sp)
                }

                Text(
                    text = "أحسنت! 🎯",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = PL_GreenDark
                )

                Text(
                    text = "أكملت الدرس بنجاح",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reward Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RewardChip(label = "+$xpEarned XP", bgColor = PL_GoldSoft, textColor = Color(0xFFA35A00))
                    Spacer(modifier = Modifier.width(10.dp))
                    RewardChip(label = "❤️ +1", bgColor = PL_RedSoft, textColor = Color(0xFFA32D2D))
                    Spacer(modifier = Modifier.width(10.dp))
                    RewardChip(label = "95%", bgColor = PL_GreenSoft, textColor = PL_GreenDark)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // New Badge Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EEFF))
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFF8B5CF6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⭐", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("وسام جديد: نجم القراءة", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                            Text("أكملت 5 دروس متتالية بدون خطأ", fontSize = 11.5.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1CB854)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("متابعة", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RewardChip(label: String, bgColor: Color, textColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = textColor
        )
    }
}
