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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.LeaderboardEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    leaderboardViewModel: LeaderboardViewModel = viewModel()
) {
    val entries by leaderboardViewModel.leaderboardEntries.collectAsState()
    val isLoading by leaderboardViewModel.isLoading.collectAsState()

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
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Podium Section for Top 3
                    val top3 = entries.take(3)
                    val remaining = if (entries.size > 3) entries.drop(3) else emptyList()

                    if (top3.isNotEmpty()) {
                        PodiumSection(top3)
                    }

                    // List Section
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        itemsIndexed(remaining) { index, data ->
                            RankingRow(rank = index + 4, data = data)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumSection(top3: List<LeaderboardEntry>) {
    val first = top3.getOrNull(0)
    val second = top3.getOrNull(1)
    val third = top3.getOrNull(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        if (second != null) {
            PodiumItem(name = second.userName, xp = second.xp.toString(), rank = 2, height = 100.dp, color = Color(0xFF94A3B8))
        }
        if (first != null) {
            PodiumItem(name = first.userName, xp = first.xp.toString(), rank = 1, height = 140.dp, color = Color(0xFFFBB24F))
        }
        if (third != null) {
            PodiumItem(name = third.userName, xp = third.xp.toString(), rank = 3, height = 80.dp, color = Color(0xFFB45309))
        }
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
        Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
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
fun RankingRow(rank: Int, data: LeaderboardEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(rank.toString(), fontWeight = FontWeight.Black, color = Color(0xFF9CA3AF), modifier = Modifier.width(32.dp))
            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Text("👤", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(data.userName, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("${data.xp} XP", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        }
    }
}

data class RankingData(val name: String, val xp: String, val isTop: Boolean)
