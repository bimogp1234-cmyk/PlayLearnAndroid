package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
fun LeaderboardScreen() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("المتصدرين", fontWeight = FontWeight.Black) },
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
            ) {
                // Podium Section
                PodiumSection()

                // List Section
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    val rankings = listOf(
                        RankingData("سارة", "2450 XP", true),
                        RankingData("محمد", "2100 XP", false),
                        RankingData("إيمان", "1950 XP", false),
                        RankingData("أحمد (أنت)", "1250 XP", false),
                        RankingData("خالد", "1100 XP", false),
                        RankingData("ليلى", "950 XP", false)
                    )
                    
                    itemsIndexed(rankings) { index, data ->
                        RankingRow(rank = index + 4, data = data)
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        PodiumItem(name = "محمد", xp = "2100", rank = 2, height = 100.dp, color = Color(0xFF94A3B8))
        PodiumItem(name = "سارة", xp = "2450", rank = 1, height = 140.dp, color = Color(0xFFFBB24F))
        PodiumItem(name = "إيمان", xp = "1950", rank = 3, height = 80.dp, color = Color(0xFFB45309))
    }
}

@Composable
fun PodiumItem(name: String, xp: String, rank: Int, height: androidx.compose.ui.unit.Dp, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(if (rank == 1) 80.dp else 64.dp)
                .background(color.copy(alpha = 0.1f), CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(modifier = Modifier.fillMaxSize(), shape = CircleShape, color = color.copy(alpha = 0.2f)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(if (rank == 1) "👑" else "👤", fontSize = if (rank == 1) 32.sp else 24.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("$xp XP", color = Color(0xFF6B7280), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .width(60.dp)
                .height(height),
            color = color,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        ) {
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(top = 8.dp)) {
                Text(rank.toString(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun RankingRow(rank: Int, data: RankingData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (data.name.contains("أنت")) Color(0xFFF0FDF4) else Color.White
        ),
        border = if (data.name.contains("أنت")) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(rank.toString(), fontWeight = FontWeight.Black, color = Color(0xFF9CA3AF), modifier = Modifier.width(32.dp))
            Box(modifier = Modifier.size(40.dp).background(Color(0xFFF3F4F6), CircleShape), contentAlignment = Alignment.Center) {
                Text("👤", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(data.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(data.xp, fontWeight = FontWeight.Black, color = Color(0xFF16A34A))
        }
    }
}

data class RankingData(val name: String, val xp: String, val isTop: Boolean)
