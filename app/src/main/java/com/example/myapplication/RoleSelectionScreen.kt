package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFBF5ED)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "اختر دورك", // Select Your Role
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF16A34A)
                )
                
                Text(
                    text = "كيف ستستخدم بليرن اليوم؟", // How will you use PlayLearn today?
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                RoleCard(
                    title = "طالب",
                    description = "تعلم، العب، واحصل على مكافآت",
                    icon = "🎓",
                    color = Color(0xFF16A34A),
                    onClick = { onRoleSelected("student") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoleCard(
                    title = "معلم",
                    description = "أدِر فصولك وتابع تقدم طلابك",
                    icon = "👨‍🏫",
                    color = Color(0xFF3B82F6),
                    onClick = { onRoleSelected("teacher") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoleCard(
                    title = "إدارة المدرسة",
                    description = "أدِر المنصة والمدارس والتقارير",
                    icon = "🏫",
                    color = Color(0xFFF97316),
                    onClick = { onRoleSelected("admin") }
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(onClick = onBack) {
                    Text("العودة للخلف", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun RoleCard(title: String, description: String, icon: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(title, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
                Text(description, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}
