package com.tamakara.booth.data.model

data class Order(
    val id: Long,
    val itemId: Long,
    val sellerId: Long,
    val orderState: String,
    val payAmount: Double,
    val createdAt: String
)

