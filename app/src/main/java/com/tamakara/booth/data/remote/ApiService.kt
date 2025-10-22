package com.tamakara.booth.data.remote

import com.tamakara.booth.data.model.*
import retrofit2.http.*

interface ApiService {
    // 用户相关
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): String

    @POST("login")
    suspend fun login(@Body request: LoginRequest): String

    @GET("vo/user")
    suspend fun getUser(
        @Header("X-USER-ID") userId: Long?,
        @Query("sellerId") sellerId: Long?
    ): User

    // 商品相关
    @GET("vo/items")
    suspend fun getItems(
        @Header("X-USER-ID") userId: Long?,
        @Query("sellerId") sellerId: Long?,
        @Query("itemState") itemState: String = "ON_SALE",
        @Query("pageNo") pageNo: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): ItemPage

    @GET("vo/item/{itemId}")
    suspend fun getItem(
        @Header("X-USER-ID") userId: Long?,
        @Path("itemId") itemId: Long
    ): Item

    @POST("create")
    suspend fun createItem(
        @Header("X-USER-ID") userId: Long,
        @Body request: CreateItemRequest
    ): Long

    // 订单相关
    @POST("create/{itemId}")
    suspend fun createOrder(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    ): Long

    @GET("vo/order/{orderId}")
    suspend fun getOrder(
        @Header("X-USER-ID") userId: Long,
        @Path("orderId") orderId: Long
    ): Order

    // 收藏相关
    @POST("favorite/{itemId}")
    suspend fun favoriteItem(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    )

    @DELETE("unfavorite/{itemId}")
    suspend fun unfavoriteItem(
        @Header("X-USER-ID") userId: Long,
        @Path("itemId") itemId: Long
    )
}

