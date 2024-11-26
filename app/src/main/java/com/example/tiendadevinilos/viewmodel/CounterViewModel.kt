package com.example.tiendadevinilos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    val counter = mutableStateOf(1)

    fun increment(max: Int) {
        if (counter.value < max) {
            counter.value++
        }

    }

    fun decrement() {
        if (counter.value > 1) {
            counter.value--
        }
    }


}