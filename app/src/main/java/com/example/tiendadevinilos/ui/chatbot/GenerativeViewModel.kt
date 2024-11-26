package com.example.tiendadevinilos.ui.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class GenerativeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<String>("")
    val response: LiveData<String> = _response

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    val model =
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyB60UcLEja10tIIbAS9Gr5g-vkzK1-GC7k"
        )

    fun makePrompt(prompt: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = model.generateContent(prompt)
                _response.value = result.text.toString()
                Log.d("GenerativeViewModel", "Respuesta del modelo: ${result.text}")
            } catch (e: Exception) {
                _error.value = e.message.toString()
                Log.e("GenerativeViewModel", "Error al obtener la respuesta", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun resetResponse() {
        _response.value = ""
    }
    fun resetError() {
        _error.value = ""
    }


}