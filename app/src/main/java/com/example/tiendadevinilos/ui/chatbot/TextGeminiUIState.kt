package com.example.tiendadevinilos.ui.chatbot

sealed class TextGeminiUIState {
    object welcomeScreen : TextGeminiUIState()
    object errorScreen : TextGeminiUIState()
    data class successScreen(val response: String) : TextGeminiUIState()

}