package com.tamakara.booth.data.remote

import com.tamakara.booth.data.model.CreateItemRequest
import com.tamakara.booth.data.model.Item
import com.tamakara.booth.data.model.ItemPage
import com.tamakara.booth.data.model.LoginRequest
import com.tamakara.booth.data.model.Order
import com.tamakara.booth.data.model.RegisterRequest
import com.tamakara.booth.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // 用户相关
    @POST("/user/register")
    suspend fun register(@Body request: RegisterRequest): String

    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): String

    @GET("/user/vo/user")
    suspend fun getUser(
        @Header("X-USER-ID") userId: Long?,
        @Query("sellerId") sellerId: Long?
    ): User

    // 商品相关
    @GET("/item/vo/items")
    suspend fun getItems(
        @Header("X-USER-ID") userId: Long?,
        @Query("sellerId") sellerId: Long?,
        @Query("itemState") itemState: String = "ON_SALE",
        @Query("pageNo") pageNo: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): ItemPage

    @GET("/item/vo/item/{itemId}")
    suspend fun getItem(
        @Header("X-USER-ID") userId: Long?,
        @Path("itemId") itemId: Long
    ): Item

    @POST("/item/create")
    suspend fun createItem(
        @Header("X-USER-ID") userId: Long,
        @Body request: CreateItemRequest
    ): Long

    // 订单相关
    @POST("/order/create/{itemId}")
    suspend fun createOrder(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    ): Long

    @GET("/order/vo/order/{orderId}")
    suspend fun getOrder(
        @Header("X-USER-ID") userId: Long,
        @Path("orderId") orderId: Long
    ): Order

    // 收藏相关
    @POST("/user/favorite/{itemId}")
    suspend fun favoriteItem(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    )

    @DELETE("/user/unfavorite/{itemId}")
    suspend fun unfavoriteItem(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    )
}

