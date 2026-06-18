package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onSwitchToTeacher: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    isAdmin: Boolean = false,
    onNavigateToAdmin: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val user by authViewModel.currentUser.collectAsState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("الملف الشخصي", fontWeight = FontWeight.Black) },
                    actions = {
                        IconButton(onClick = onNavigateToSettings) {
                            Text("⚙️", fontSize = 20.sp)
                        }
                        IconButton(onClick = onLogout) {
                            Text("🚪", fontSize = 20.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Header
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👨‍🎓", fontSize = 48.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(user?.name ?: "مستخدم جديد", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(user?.schoolId ?: "لم يتم اختيار صف دراسي", color = Color.Gray, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(16.dp))

                if (authViewModel.isTeacher() || authViewModel.isAdmin()) {
                    Button(
                        onClick = onSwitchToTeacher,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("التبديل إلى لوحة المعلم", fontWeight = FontWeight.Bold)
                    }
                }

                if (isAdmin) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onNavigateToAdmin,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF4E0), contentColor = Color(0xFFA35A00))
                    ) {
                        Text("لوحة الإدارة", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(label = "XP الإجمالي", value = (user?.xp ?: 0).toString(), color = Color(0xFFFBB24F), icon = "⭐", modifier = Modifier.weight(1f))
                    StatCard(label = "أطول سلسلة", value = "${user?.streak ?: 0} أيام", color = Color(0xFFF97316), icon = "🔥", modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Badges Section
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("الإنجازات", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("عرض الكل", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val currentXp = user?.xp ?: 0
                    val currentStreak = user?.streak ?: 0
                    
                    val badges = listOf(
                        BadgeData("المبتدئ", "🌱", true),
                        BadgeData("المنطلق", "🚀", currentXp >= 100),
                        BadgeData("المثابر", "📚", currentStreak >= 3),
                        BadgeData("نجم ساطع", "✨", currentXp >= 500),
                        BadgeData("الخبير", "🎓", currentXp >= 1000),
                        BadgeData("الأسطورة", "🔥", currentStreak >= 7)
                    )
                    
                    items(badges.size) { index ->
                        BadgeItem(badges[index])
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, icon: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Text(value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun BadgeItem(badge: BadgeData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    if (badge.isUnlocked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) 
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), 
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badge.icon, 
                fontSize = 32.sp, 
                modifier = Modifier.alpha(if (badge.isUnlocked) 1f else 0.2f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.name, 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Bold, 
            color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

data class BadgeData(val name: String, val icon: String, val isUnlocked: Boolean)
