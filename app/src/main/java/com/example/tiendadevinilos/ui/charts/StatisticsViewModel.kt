package com.example.tiendadevinilos.ui.charts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.StatisticsModel
import com.example.tiendadevinilos.network.RetrofitClient
import com.example.tiendadevinilos.network.userIdModelRetro
import kotlinx.coroutines.launch

class StatisticsViewModel: ViewModel()  {
    private val _statisticsList = MutableLiveData<List<StatisticsModel>>()
    val statisticsList: LiveData<List<StatisticsModel>> = _statisticsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseStatus = MutableLiveData<Boolean>()
    val responseStatus: LiveData<Boolean> = _responseStatus

    private val _errorMessage = MutableLiveData<String>()

    fun getStatistics(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitClient.instance.getStatistics(userIdModelRetro(user_id = userId))

                if (response.isSuccessful) {
                    response.body()?.let { statisticsResponse ->
                        val statistics = response.body()?.data
                        if (statistics != null) {
                            _statisticsList.value = statisticsResponse.data as List<StatisticsModel>
                            Log.d("StatisticsViewModel", "Statistics: $statistics")
                            _responseStatus.value = true
                        } else {
                            _errorMessage.value = "No se encontraron órdenes"
                            _responseStatus.value = false
                        }
                    } ?: run {
                        _errorMessage.value = "La respuesta del servidor está vacía"
                        _responseStatus.value = false
                    }
                } else {
                    _errorMessage.value = "Error en la respuesta: ${response.message()}"
                    _responseStatus.value = false
                }

            }catch (e: Exception) {

            }finally {
                _isLoading.value = false

            }
        }
    }
}