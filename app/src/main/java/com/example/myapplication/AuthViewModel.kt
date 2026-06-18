package com.example.myapplication

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

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

    private var verificationId: String? = null

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

    fun loginWithGoogle(credential: AuthCredential, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                result.user?.let { firebaseUser ->
                    viewModelScope.launch {
                        val existingUser = firebaseManager.getUserProfile(firebaseUser.uid)
                        if (existingUser == null) {
                            val newUser = User(
                                uid = firebaseUser.uid,
                                name = firebaseUser.displayName ?: "",
                                email = firebaseUser.email ?: "",
                                photoURL = firebaseUser.photoUrl?.toString() ?: ""
                            )
                            db.collection("users").document(firebaseUser.uid).set(newUser).await()
                            _currentUser.value = newUser
                        } else {
                            _currentUser.value = existingUser
                        }
                        _isLoading.value = false
                        onSuccess()
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Google Sign-In failed")
            }
    }

    fun sendPhoneOtp(phoneNumber: String, activity: Activity, onCodeSent: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneCredential(credential, {}, onError)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _isLoading.value = false
                    onError(e.message ?: "Verification failed")
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    _isLoading.value = false
                    verificationId = id
                    onCodeSent()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val id = verificationId ?: return onError("Verification ID not found")
        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithPhoneCredential(credential, onSuccess, onError)
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                result.user?.let { firebaseUser ->
                    viewModelScope.launch {
                        val existingUser = firebaseManager.getUserProfile(firebaseUser.uid)
                        if (existingUser == null) {
                            val newUser = User(
                                uid = firebaseUser.uid,
                                phone = firebaseUser.phoneNumber ?: "",
                                name = "New User"
                            )
                            db.collection("users").document(firebaseUser.uid).set(newUser)
                            _currentUser.value = newUser
                        } else {
                            _currentUser.value = existingUser
                        }
                        _isLoading.value = false
                        onSuccess()
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Phone Sign-In failed")
            }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _isAuthChecked.value = true
    }

    fun isStudent() = _currentUser.value?.role == "student"
    fun isTeacher() = _currentUser.value?.role == "teacher"
    fun isAdmin() = _currentUser.value?.role == "admin"
}
