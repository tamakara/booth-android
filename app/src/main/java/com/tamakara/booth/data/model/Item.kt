package com.tamakara.booth.data.model

data class Item(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val postage: Double,
    val images: List<String>,
    val sellerId: Long,
    val favorites: Long,
    val state: Int,
    val isSeller: Boolean = false
)

data class ItemPage(
    val records: List<Item>,
    val total: Long,
    val current: Long,
    val size: Long
)

data class CreateItemRequest(
    val name: String,
    val description: String,
    val price: Double,
    val postage: Double,
    val stateCode: Int = 1,
    val deliveryMethodCode: Int = 1,
    val images: List<Long> = emptyList()
)

