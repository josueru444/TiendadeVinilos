package com.example.tiendadevinilos.repository

import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.network.RetrofitClient

class ProductRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getProducts(): List<ProductModel> {
        return apiService.getProducts()
    }

    suspend fun  getProductById(id: Long): ProductModel {
        return apiService.getProductById(id)
    }

}