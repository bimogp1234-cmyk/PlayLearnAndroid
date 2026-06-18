package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إدارة المنصة", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("✕") }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Text("نظرة عامة", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    AdminStatRow("المدارس", "12", "المعلمين", "48")
                }
                item {
                    AdminStatRow("الطلاب", "1,240", "النشطين الآن", "85")
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("إجراءات سريعة", fontWeight = FontWeight.Bold)
                }
                item {
                    AdminActionCard("إضافة مدرسة جديدة", "🏫")
                }
                item {
                    AdminActionCard("إدارة طلبات المعلمين", "📝")
                }
                item {
                    AdminActionCard("تقارير النظام", "📊")
                }
            }
        }
    }
}

@Composable
fun AdminStatRow(l1: String, v1: String, l2: String, v2: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        AnalyticsCard(l1, v1, Color(0xFF8B5CF6), Modifier.weight(1f))
        AnalyticsCard(l2, v2, Color(0xFFFBBF24), Modifier.weight(1f))
    }
}

@Composable
fun AdminActionCard(title: String, icon: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text("←")
        }
    }
}
