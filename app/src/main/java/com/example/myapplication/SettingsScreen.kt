package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var isArabic by remember { mutableStateOf(true) }
    var isDarkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("الإعدادات", fontWeight = FontWeight.Black) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("عام", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Language Toggle
            SettingsToggleItem(
                title = "اللغة (Language)",
                description = if (isArabic) "العربية" else "English",
                isChecked = isArabic,
                onCheckedChange = { isArabic = it }
            )

            // Dark Mode Toggle
            SettingsToggleItem(
                title = "الوضع الليلي (Dark Mode)",
                description = "تفعيل المظهر الداكن",
                isChecked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("الحساب والخصوصية", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    SettingsActionItem(title = "تغيير كلمة المرور", icon = "🔑")
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    SettingsActionItem(title = "سياسة الخصوصية", icon = "🛡️")
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    SettingsActionItem(title = "حول بليرن", icon = "ℹ️")
                }
            }
        }
    }
}

@Composable
fun SettingsToggleItem(title: String, description: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF16A34A))
            )
        }
    }
}

@Composable
fun SettingsActionItem(title: String, icon: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("←", color = Color.Gray)
    }
}
