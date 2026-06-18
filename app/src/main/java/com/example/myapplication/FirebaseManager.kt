package com.example.myapplication

import com.example.myapplication.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val rtdb = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Auth
    fun getCurrentUser() = auth.currentUser
    
    suspend fun getUserProfile(uid: String): User? {
        return try {
            db.collection("users").document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Presence (Realtime DB)
    fun setUserOnline(uid: String) {
        val presenceRef = rtdb.getReference("onlineUsers/$uid")
        presenceRef.setValue(true)
        presenceRef.onDisconnect().removeValue()
    }

    suspend fun updateUserGrade(uid: String, grade: String): Boolean {
        return try {
            db.collection("users").document(uid).update("schoolId", grade).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Courses
    suspend fun getCourses(): List<Course> {
        return try {
            db.collection("courses").get().await().toObjects(Course::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Leaderboard
    suspend fun getLeaderboard(): List<LeaderboardEntry> {
        return try {
            db.collection("users")
                .orderBy("xp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get().await().toObjects(User::class.java).mapIndexed { index, user ->
                    LeaderboardEntry(user.uid, user.name, user.xp, index + 1)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
