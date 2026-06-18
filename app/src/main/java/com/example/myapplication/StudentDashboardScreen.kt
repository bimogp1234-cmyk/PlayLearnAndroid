package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun StudentDashboardScreen(
    onStartLesson: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToProfile: () -> Unit,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val user by authViewModel.currentUser.collectAsState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                StudentHUD(
                    xp = user?.xp ?: 0,
                    streak = user?.streak ?: 0,
                    hearts = 5,
                    onProfileClick = onNavigateToProfile
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    WelcomeCard(userName = user?.name ?: "طالبنا المبدع")
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "استمر في التعلم",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CurrentCourseCard(onStartLesson)
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    QuickActionsRow(onNavigateToLeaderboard)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun StudentHUD(xp: Int, streak: Int, hearts: Int, onProfileClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatChip(icon = "🔥", value = streak.toString(), color = PL_Gold, bgColor = PL_GoldSoft)
                Spacer(modifier = Modifier.width(8.dp))
                StatChip(icon = "💎", value = xp.toString(), color = PL_Blue, bgColor = PL_Blue.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatChip(icon = "❤️", value = hearts.toString(), color = PL_Red, bgColor = PL_RedSoft)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun StatChip(icon: String, value: String, color: Color, bgColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 13.sp)
        }
    }
}

@Composable
fun WelcomeCard(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))),
                RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text("مرحباً، $userName! 👋", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text("جاهز لمغامرة تعليمية جديدة؟", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }
        Text("🚀", fontSize = 60.sp, modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun CurrentCourseCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(PL_GreenSoft, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📚", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("أساسيات القراءة", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("الوحدة الأولى: الحروف", color = Color.Gray, fontSize = 13.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LinearProgressIndicator(
                progress = 0.65f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = PL_Green,
                trackColor = PL_GreenSoft
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("اكتمل 65%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PL_GreenDark)
        }
    }
}

@Composable
fun QuickActionsRow(onLeaderboard: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ActionCard(title = "المتصدرون", icon = "🏆", color = PL_Gold, modifier = Modifier.weight(1f).clickable { onLeaderboard() })
        ActionCard(title = "المتجر", icon = "🏪", color = PL_Blue, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ActionCard(title: String, icon: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
        }
    }
}
