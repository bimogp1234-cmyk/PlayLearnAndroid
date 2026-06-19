package com.example.myapplication.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "student", // student, teacher, admin
    val photoURL: String = "",
    val schoolId: String = "",
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 0,
    val streakFreezes: Int = 0,
    val language: String = "ar",
    @ServerTimestamp val createdAt: Date? = null
)

data class School(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val studentsCount: Int = 0
)

data class Course(
    val id: String = "",
    val titleAr: String = "",
    val titleEn: String = "",
    val descriptionAr: String = "",
    val descriptionEn: String = "",
    val teacherId: String = "",
    val level: String = ""
)

data class Lesson(
    val id: String = "",
    val courseId: String = "",
    val titleAr: String = "",
    val titleEn: String = "",
    val content: String = ""
)

data class Quiz(
    val id: String = "",
    val lessonId: String = "",
    val title: String = ""
)

data class Question(
    val id: String = "",
    val quizId: String = "",
    val questionAr: String = "",
    val questionEn: String = "",
    val answers: List<String> = emptyList(),
    val correctAnswer: String = ""
)

data class Badge(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val xpRequired: Int = 100
)

data class StudentProgress(
    val userId: String = "",
    val courseId: String = "",
    val completedLessons: List<String> = emptyList(),
    val progress: Int = 0
)

data class LeaderboardEntry(
    val userId: String = "",
    val userName: String = "",
    val xp: Int = 0,
    val rank: Int = 0
)

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
