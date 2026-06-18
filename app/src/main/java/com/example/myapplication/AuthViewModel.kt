package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val firebaseManager = FirebaseManager()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isAuthChecked = MutableStateFlow(false)
    val isAuthChecked: StateFlow<Boolean> = _isAuthChecked

    init {
        checkAuth()
    }

    private fun checkAuth() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            fetchUserData(firebaseUser.uid)
        } else {
            _isLoading.value = false
            _isAuthChecked.value = true
        }
    }

    fun fetchUserData(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = firebaseManager.getUserProfile(uid)
            _currentUser.value = user
            if (user != null) {
                firebaseManager.setUserOnline(uid)
            }
            _isLoading.value = false
            _isAuthChecked.value = true
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _isAuthChecked.value = true
    }

    fun loginWithEmail(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    fetchUserData(it.uid)
                    onSuccess()
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Authentication failed")
            }
    }

    fun signUpWithEmail(user: User, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val finalUser = user.copy(uid = uid)
                db.collection("users").document(uid).set(finalUser)
                    .addOnSuccessListener {
                        _currentUser.value = finalUser
                        _isLoading.value = false
                        onSuccess()
                    }
                    .addOnFailureListener {
                        _isLoading.value = false
                        onError(it.message ?: "Failed to save user profile")
                    }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Registration failed")
            }
    }

    // Role-based logic
    fun isStudent() = _currentUser.value?.role == "student"
    fun isTeacher() = _currentUser.value?.role == "teacher"
    fun isAdmin() = _currentUser.value?.role == "admin"
}
