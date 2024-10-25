package com.example.tiendadevinilos.network

import com.example.tiendadevinilos.model.ProductModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/products")
    suspend fun getProducts(): List<ProductModel>

    @GET("/product/{id}")
    suspend fun getProductById(@Path("id") id: Long): ProductModel

}