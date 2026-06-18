package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Course
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(onBack: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("لوحة تحكم المعلم", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("✕") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Create Course */ }) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Stats Section
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AnalyticsCard("الطلاب", "45", Color(0xFF3B82F6), Modifier.weight(1f))
                    AnalyticsCard("الدورات", "3", Color(0xFF16A34A), Modifier.weight(1f))
                }
            }
            
            item {
                UploadContentSection(onUploadSuccess = { 
                    scope.launch {
                        snackbarHostState.showSnackbar("تم نشر الدرس بنجاح! 🎉")
                    }
                })
            }
            
            item {
                Text("دوراتي التدريبية", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            
            items(listOf(
                com.example.myapplication.data.Course(titleAr = "أساسيات الرياضيات", level = "الصف الرابع"),
                com.example.myapplication.data.Course(titleAr = "اللغة العربية - قواعد", level = "الصف الخامس")
            )) { course ->
                CourseTeacherItem(course)
            }
        }
    }
}

@Composable
fun UploadContentSection(
    onUploadSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "📤 رفع محتوى تعليمي جديد",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الدرس") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("المحتوى التعليمي") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5,
                enabled = !isUploading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        isUploading = true
                        val lesson = com.example.myapplication.data.Lesson(
                            id = db.collection("lessons").document().id,
                            courseId = "default_course_id",
                            titleAr = title,
                            content = description
                        )
                        db.collection("lessons").document(lesson.id).set(lesson)
                            .addOnSuccessListener {
                                isUploading = false
                                onUploadSuccess()
                                title = ""
                                description = ""
                            }
                            .addOnFailureListener {
                                isUploading = false
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isUploading && title.isNotEmpty() && description.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("نشر الدرس للطلاب", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AnalyticsCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 14.sp, color = color)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
fun CourseTeacherItem(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(course.titleAr, fontWeight = FontWeight.Bold)
                Text(course.level, fontSize = 12.sp, color = Color.Gray)
            }
            Button(onClick = { /* Manage */ }) {
                Text("إدارة")
            }
        }
    }
}
