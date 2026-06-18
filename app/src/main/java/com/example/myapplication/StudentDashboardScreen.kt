package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.myapplication.ui.theme.PL_GoldSoft
import com.example.myapplication.ui.theme.PL_GreenDark
import com.example.myapplication.ui.theme.PL_GreenSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onStartLesson: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(34.dp).background(PL_GreenSoft, CircleShape), contentAlignment = Alignment.Center) {
                                Text("ر", fontWeight = FontWeight.Bold, color = PL_GreenDark)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("أهلاً، رنا!", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("لنستمر في التعلم اليوم", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    },
                    actions = {
                        StatChip(icon = "🔥", value = "7", color = Color(0xFFA35A00), bgColor = PL_GoldSoft)
                        Spacer(modifier = Modifier.width(8.dp))
                        StatChip(icon = "●", value = "1,250", color = PL_GreenDark, bgColor = PL_GreenSoft)
                        Spacer(modifier = Modifier.width(16.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    NavigationBarItem(
                        icon = { Text("🏠", fontSize = 20.sp) },
                        label = { Text("الرئيسية") },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Text("📚", fontSize = 20.sp) },
                        label = { Text("دروسي") },
                        selected = false,
                        onClick = onStartLesson
                    )
                    NavigationBarItem(
                        icon = { Text("🏆", fontSize = 20.sp) },
                        label = { Text("المتصدرين") },
                        selected = false,
                        onClick = onNavigateToLeaderboard
                    )
                    NavigationBarItem(
                        icon = { Text("👤", fontSize = 20.sp) },
                        label = { Text("حسابي") },
                        selected = false,
                        onClick = onNavigateToProfile
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(modifier = Modifier.padding(vertical = 14.dp), horizontalArrangement = Arrangement.Start) {
                        repeat(4) { Text("❤️", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 2.dp)) }
                        Text("🤍", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 2.dp))
                    }
                }

                item {
                    Text("استمر من حيث توقفت", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onStartLesson() },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEAE6DC))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(46.dp).background(Color(0xFFEEF1FF), RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                                Text("📖", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(13.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("الوحدة 3: الحروف والكلمات", fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                                Text("7 من 11 درساً مكتمل", fontSize = 12.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(9.dp).background(Color(0xFFEAE6DC), RoundedCornerShape(6.dp))) {
                                    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.64f).background(Color(0xFF1CB854), RoundedCornerShape(6.dp)))
                                }
                            }
                        }
                    }
                }

                item {
                    Text("دوراتك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MiniCourseCard(title = "اللغة العربية", icon = "📖", color = PL_GreenSoft, tint = PL_GreenDark, modifier = Modifier.weight(1f))
                        MiniCourseCard(title = "الرياضيات", icon = "🔢", color = PL_GoldSoft, tint = Color(0xFFA35A00), modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun StatChip(icon: String, value: String, color: Color, bgColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 13.sp)
        }
    }
}

@Composable
fun MiniCourseCard(title: String, icon: String, color: Color, tint: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEAE6DC))
    ) {
        Column(modifier = Modifier.padding(13.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(36.dp).background(color, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Text(icon, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LessonCard(title: String, xp: String, isCompleted: Boolean, onClick: () -> Unit = {}) {
    // Legacy - replaced by more complex cards in this version
}
