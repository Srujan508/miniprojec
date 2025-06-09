package com.example.miniproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.miniproject.service.ChatbotService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotService: ChatbotService
) : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val chatHistory: StateFlow<List<Pair<String, String>>> = _chatHistory

    fun sendMessage(message: String) {
        val response = chatbotService.getResponse(message)
        _chatHistory.value = _chatHistory.value + (message to response)
    }

    fun clearChat() {
        _chatHistory.value = emptyList()
    }
} 