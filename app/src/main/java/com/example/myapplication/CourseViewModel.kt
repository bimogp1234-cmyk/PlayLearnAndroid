package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Course
import com.example.myapplication.data.Lesson
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourseViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadCoursesByGrade(grade: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("courses")
                    .whereEqualTo("level", grade)
                    .get()
                    .await()
                _courses.value = snapshot.toObjects(Course::class.java)
            } catch (e: Exception) {
                _courses.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLessons(courseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("lessons")
                    .whereEqualTo("courseId", courseId)
                    .get()
                    .await()
                _lessons.value = snapshot.toObjects(Lesson::class.java)
            } catch (e: Exception) {
                _lessons.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
