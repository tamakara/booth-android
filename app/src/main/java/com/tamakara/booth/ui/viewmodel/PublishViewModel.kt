package com.tamakara.booth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tamakara.booth.data.model.CreateItemRequest
import com.tamakara.booth.data.repository.BoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PublishViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BoothRepository(application.applicationContext)

    private val _publishState = MutableStateFlow<PublishState>(PublishState.Idle)
    val publishState: StateFlow<PublishState> = _publishState.asStateFlow()

    fun publishItem(userId: Long, name: String, description: String, price: Double, postage: Double) {
        viewModelScope.launch {
            _publishState.value = PublishState.Loading
            val request = CreateItemRequest(
                name = name,
                description = description,
                price = price,
                postage = postage
            )
            repository.createItem(userId, request).fold(
                onSuccess = { itemId ->
                    _publishState.value = PublishState.Success(itemId)
                },
                onFailure = { error ->
                    _publishState.value = PublishState.Error(error.message ?: "发布失败")
                }
            )
        }
    }

    fun resetState() {
        _publishState.value = PublishState.Idle
    }
}

sealed class PublishState {
    object Idle : PublishState()
    object Loading : PublishState()
    data class Success(val itemId: Long) : PublishState()
    data class Error(val message: String) : PublishState()
}
