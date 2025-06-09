package com.example.miniproject.service

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotService @Inject constructor() {
    
    private val commonQuestions = mapOf(
        "how to add medicine" to "To add a medicine, go to the home screen and tap the '+' button. Fill in the medicine details including name, dosage, and time to take.",
        "how to set reminder" to "When adding a medicine, you'll be asked to set the time. The app will automatically send you notifications at the specified time.",
        "how to mark medicine as taken" to "When you receive a notification, tap on it to open the app. You can then mark the medicine as taken by tapping the checkmark button.",
        "how to edit medicine" to "Go to the medicine list, tap on the medicine you want to edit, and use the edit button to modify the details.",
        "how to delete medicine" to "Go to the medicine list, tap on the medicine you want to delete, and use the delete button to remove it.",
        "what if i miss a dose" to "If you miss a dose, mark it as missed in the app. It's recommended to consult your doctor about missed doses.",
        "how to view history" to "Go to the history tab to view your past medicine records, including taken and missed doses.",
        "how to change notification sound" to "Go to Settings > Notifications to customize your notification preferences.",
        "how to backup data" to "Your data is automatically backed up to your account. Make sure you're signed in to keep your data safe.",
        "how to contact support" to "You can contact support through the 'Help' section in the app settings."
    )

    fun getResponse(query: String): String {
        val normalizedQuery = query.lowercase().trim()
        
        return when {
            normalizedQuery.contains("hello") || normalizedQuery.contains("hi") ->
                "Hello! How can I help you today?"
            
            normalizedQuery.contains("thank") ->
                "You're welcome! Let me know if you need anything else."
            
            normalizedQuery.contains("bye") || normalizedQuery.contains("goodbye") ->
                "Goodbye! Take care and don't forget to take your medicines on time!"
            
            else -> {
                commonQuestions.entries.firstOrNull { (key, _) ->
                    normalizedQuery.contains(key)
                }?.value ?: "I'm not sure about that. You can ask me about adding medicines, setting reminders, marking medicines as taken, or viewing your history."
            }
        }
    }
} 