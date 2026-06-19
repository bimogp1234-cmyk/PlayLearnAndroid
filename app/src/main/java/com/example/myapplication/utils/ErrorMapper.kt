package com.example.myapplication.utils

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object ErrorMapper {
    fun mapFirebaseError(exception: Exception?): String {
        if (exception == null) return "حدث خطأ غير معروف"
        
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "لا يوجد حساب بهذا البريد الإلكتروني"
            is FirebaseAuthInvalidCredentialsException -> "كلمة المرور غير صحيحة أو البيانات غير صالحة"
            is FirebaseAuthUserCollisionException -> "هذا البريد الإلكتروني مسجل بالفعل"
            is FirebaseAuthException -> {
                when (exception.errorCode) {
                    "ERROR_NETWORK_REQUEST_FAILED" -> "خطأ في الاتصال بالشبكة، تأكد من اتصالك بالإنترنت"
                    "ERROR_TOO_MANY_REQUESTS" -> "محاولات كثيرة جداً، يرجى المحاولة لاحقاً"
                    else -> "فشلت العملية: ${exception.localizedMessage}"
                }
            }
            else -> "عذراً، حدث خطأ: ${exception.localizedMessage}"
        }
    }
}
