package com.tamakara.booth.data.repository

import android.content.Context
import com.tamakara.booth.data.model.CreateItemRequest
import com.tamakara.booth.data.model.Item
import com.tamakara.booth.data.model.ItemPage
import com.tamakara.booth.data.model.LoginRequest
import com.tamakara.booth.data.model.RegisterRequest
import com.tamakara.booth.data.model.User
import com.tamakara.booth.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoothRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    init {
        RetrofitClient.init(context.applicationContext)
    }

    private val api = RetrofitClient.apiService()

    // 用户相关
    suspend fun register(phone: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val userId = api.register(RegisterRequest(phone, password))
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(phone: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val token = api.login(LoginRequest(phone, password))
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: Long): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = api.getUser(userId, null)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = api.getUser(null, null)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    // 商品相关
    suspend fun getItems(userId: Long?, pageNo: Int = 1): Result<ItemPage> = withContext(Dispatchers.IO) {
        try {
            val items = api.getItems(userId = userId, sellerId = null, pageNo = pageNo)
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItem(userId: Long?, itemId: Long): Result<Item> = withContext(Dispatchers.IO) {
        try {
            val item = api.getItem(userId, itemId)
            Result.success(item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createItem(userId: Long, request: CreateItemRequest): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val itemId = api.createItem(userId, request)
            Result.success(itemId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 订单相关
    suspend fun createOrder(userId: Long, itemId: Long): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val orderId = api.createOrder(userId, itemId)
            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 收藏相关
    suspend fun favoriteItem(userId: Long, itemId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.favoriteItem(userId, itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unfavoriteItem(userId: Long, itemId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.unfavoriteItem(userId, itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
