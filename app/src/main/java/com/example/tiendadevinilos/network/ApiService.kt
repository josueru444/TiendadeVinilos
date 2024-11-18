package com.example.tiendadevinilos.network

import androidx.lifecycle.LiveData
import com.example.tiendadevinilos.model.AddUserGenreModel
import com.example.tiendadevinilos.model.CartItemRequest
import com.example.tiendadevinilos.model.DeteleCartItemRequest
import com.example.tiendadevinilos.model.GenreModel
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
    val message: String,
    val data: List<Any>,
)
data class userGenreRequest(
    val user_id: String,

)

interface ApiService {
    //Products////////////////////////////////
    @GET("api/products")
    suspend fun getProducts(): List<ProductModel>

    @GET("api/product/{id}")
    suspend fun getProductById(@Path("id") id: Long): ProductModel

    // User ------------------------------

    @POST("api/new-google-user")
    suspend fun AddUserGoogle(
        @Body userModel: UserModel
    ): Response<Responses>

    // CART --------------------------
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

    //Genres ------------------------------------

    @GET("api/get-all-genre")
    suspend fun getAllGenre(): List<GenreModel>

    @POST("api/add-user-genre")
    suspend fun addUserGenre(
        @Body addGenreModel: AddUserGenreModel
    ): Response<Responses>

    // Obtener géneros por usuario

    @POST("api/get-genre-by-user")
    suspend fun getUserGenre(@Body request: Map<String, String>): Response<Responses>



}