package com.example.tiendadevinilos.network

import com.example.tiendadevinilos.model.AddUserGenreModel
import com.example.tiendadevinilos.model.AddressModel
import com.example.tiendadevinilos.model.CartItemRequest
import com.example.tiendadevinilos.model.DeteleCartItemRequest
import com.example.tiendadevinilos.model.GenreModel
import com.example.tiendadevinilos.model.GetCartResponse
import com.example.tiendadevinilos.model.GetItemsCartList
import com.example.tiendadevinilos.model.ModifyCartRequest
import com.example.tiendadevinilos.model.OrderApiResponse
import com.example.tiendadevinilos.model.OrderModel
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.model.StatisticsModel
import com.example.tiendadevinilos.model.StatisticsResponse
import com.example.tiendadevinilos.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Responses<T>(
    val success: Boolean,
    val message: String,
    val data: List<Any>,
)

data class userIdModelRetro(
    val user_id: String
)

interface ApiService {
    //Products////////////////////////////////
    @GET("api/products")
    suspend fun getProducts(): List<ProductModel>

    @GET("api/product/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductModel

    // User ------------------------------

    @POST("api/new-google-user")
    suspend fun AddUserGoogle(
        @Body userModel: UserModel
    ): Response<Responses<Any?>>

    // CART --------------------------
    @POST("api/add-cart")
    suspend fun AddItemCart(
        @Body cartItemRequest: CartItemRequest
    ): Response<Responses<Any?>>

    @POST("/api/get-cart")
    suspend fun getCartItems(
        @Body getItemsCartList: GetItemsCartList
    ): Response<GetCartResponse>

    @POST("api/modify-quantity-cart-items")
    suspend fun modifyQuantityCartItems(
        @Body modifyCartRequest: ModifyCartRequest
    ): Response<Responses<Any?>>

    @POST("api/delete-cart-item")
    suspend fun deleteCartItem(
        @Body DeteleCartItemRequest: DeteleCartItemRequest
    ): Response<Responses<Any?>>

    //Genres ------------------------------------

    @GET("api/get-all-genre")
    suspend fun getAllGenre(): List<GenreModel>

    @POST("api/add-user-genre")
    suspend fun addUserGenre(
        @Body addGenreModel: AddUserGenreModel
    ): Response<Responses<Any?>>

    // Obtener géneros por usuario

    @POST("api/get-genre-by-user")
    suspend fun getUserGenre(@Body request: Map<String, String>): Response<Responses<Any?>>

    //Addresses ---------------------------------
    @POST("api/add-user-address")
    suspend fun addNewAddress(
        @Body addressModel: AddressModel
    ): List<Responses<Any?>>

    //Orders
    @POST("api/add-new-order")
    suspend fun addNewOrder(
        @Body orderModel: OrderModel
    ): Response<Responses<Any?>>

    @POST("api/get-orders")
    suspend fun getAllOrders(
        @Body userIdModelRetro: userIdModelRetro
    ): Response<OrderApiResponse>

    @POST("api/get-statistics")
    suspend fun getStatistics(
        @Body userIdModelRetro: userIdModelRetro
    ): Response<StatisticsResponse>

}