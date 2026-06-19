package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ChatMessage
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("chat_messages")
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        listenForMessages()
    }

    private fun listenForMessages() {
        database.orderByChild("timestamp").limitToLast(50)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = mutableListOf<ChatMessage>()
                    for (child in snapshot.children) {
                        child.getValue(ChatMessage::class.java)?.let { messageList.add(it) }
                    }
                    _messages.value = messageList.reversed()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun sendMessage(senderId: String, senderName: String, text: String) {
        if (text.isBlank()) return
        val messageId = database.push().key ?: return
        val message = ChatMessage(
            id = messageId,
            senderId = senderId,
            senderName = senderName,
            text = text
        )
        database.child(messageId).setValue(message)
    }
}
