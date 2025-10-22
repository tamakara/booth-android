package com.tamakara.booth.data.model

data class User(
    val id: Long,
    val username: String?,
    val phone: String?,
    val balance: Double,
    val avatarUrl: String?
)

data class LoginRequest(
    val phone: String,
    val password: String
)

data class RegisterRequest(
    val phone: String,
    val password: String
)

