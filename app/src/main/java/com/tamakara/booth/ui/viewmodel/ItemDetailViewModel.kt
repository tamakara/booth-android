package com.tamakara.booth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tamakara.booth.data.model.Item
import com.tamakara.booth.data.repository.BoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BoothRepository(application.applicationContext)

    private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    fun loadItem(userId: Long?, itemId: Long) {
        viewModelScope.launch {
            _uiState.value = ItemDetailUiState.Loading
            repository.getItem(userId, itemId).fold(
                onSuccess = { item ->
                    _uiState.value = ItemDetailUiState.Success(item)
                },
                onFailure = { error ->
                    _uiState.value = ItemDetailUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }

    fun createOrder(userId: Long, itemId: Long) {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            repository.createOrder(userId, itemId).fold(
                onSuccess = { orderId ->
                    _orderState.value = OrderState.Success(orderId)
                },
                onFailure = { error ->
                    _orderState.value = OrderState.Error(error.message ?: "创建订单失败")
                }
            )
        }
    }

    fun resetOrderState() {
        _orderState.value = OrderState.Idle
    }
}

sealed class ItemDetailUiState {
    object Loading : ItemDetailUiState()
    data class Success(val item: Item) : ItemDetailUiState()
    data class Error(val message: String) : ItemDetailUiState()
}

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    data class Success(val orderId: Long) : OrderState()
    data class Error(val message: String) : OrderState()
}
