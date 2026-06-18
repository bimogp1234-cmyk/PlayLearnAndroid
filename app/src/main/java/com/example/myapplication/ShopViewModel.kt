package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ShopItem(
    val id: String,
    val nameAr: String,
    val descriptionAr: String,
    val price: Int,
    val icon: String
)

class ShopViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isPurchasing = MutableStateFlow(false)
    val isPurchasing: StateFlow<Boolean> = _isPurchasing

    private val _purchaseError = MutableStateFlow<String?>(null)
    val purchaseError: StateFlow<String?> = _purchaseError

    val shopItems = listOf(
        ShopItem("streak_freeze", "تجميد السلسلة", "يحمي سلسلتك إذا فاتك يوم من التعلم", 500, "❄️"),
        ShopItem("xp_boost", "مضاعف النقاط", "احصل على ضعف النقاط لمدة 30 دقيقة", 1000, "⚡"),
        ShopItem("gold_frame", "إطار ذهبي", "إطار مميز لصورة ملفك الشخصي", 2500, "👑"),
        ShopItem("new_outfit", "زي مدرسي جديد", "غير مظهر شخصيتك في التطبيق", 1500, "👕")
    )

    fun purchaseItem(item: ShopItem, currentXp: Int, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        if (currentXp < item.price) {
            _purchaseError.value = "ليس لديك نقاط كافية لشراء هذا العنصر"
            return
        }

        viewModelScope.launch {
            _isPurchasing.value = true
            _purchaseError.value = null
            try {
                db.runTransaction { transaction ->
                    val userRef = db.collection("users").document(uid)
                    
                    // Deduct XP
                    transaction.update(userRef, "xp", FieldValue.increment(-item.price.toLong()))
                    
                    // Specific logic for streak freeze
                    if (item.id == "streak_freeze") {
                        transaction.update(userRef, "streakFreezes", FieldValue.increment(1))
                    }
                    
                    // For other items, we could have an inventory list
                }.await()
                
                onSuccess()
            } catch (e: Exception) {
                _purchaseError.value = "فشلت عملية الشراء: ${e.message}"
            } finally {
                _isPurchasing.value = false
            }
        }
    }
}
