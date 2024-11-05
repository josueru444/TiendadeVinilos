package com.example.tiendadevinilos.network

import com.example.tiendadevinilos.model.CartItemRequest
import com.example.tiendadevinilos.model.DeteleCartItemRequest
import com.example.tiendadevinilos.model.GetCartResponse
import com.example.tiendadevinilos.model.GetItemsCartList
import com.example.tiendadevinilos.model.ModifyCartRequest
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Responses(
    val success: Boolean,
    val message: String
)

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): List<ProductModel>

    @GET("api/product/{id}")
    suspend fun getProductById(@Path("id") id: Long): ProductModel

    @POST("api/new-google-user")
    suspend fun AddUserGoogle(
        @Body userModel: UserModel
    ): Response<Responses>

    @POST("api/add-cart")
    suspend fun AddItemCart(
        @Body cartItemRequest: CartItemRequest
    ): Response<Responses>

    @POST("/api/get-cart")
    suspend fun getCartItems(
        @Body getItemsCartList: GetItemsCartList
    ): Response<GetCartResponse>

    @POST("api/modify-quantity-cart-items")
    suspend fun modifyQuantityCartItems(
        @Body modifyCartRequest: ModifyCartRequest
    ): Response<Responses>

    @POST("api/delete-cart-item")
    suspend fun deleteCartItem(
        @Body DeteleCartItemRequest: DeteleCartItemRequest
    ): Response<Responses>


}