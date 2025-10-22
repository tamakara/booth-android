package com.tamakara.booth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamakara.booth.data.model.Item
import com.tamakara.booth.data.repository.BoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = BoothRepository()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadItems(userId: Long? = null) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getItems(userId).fold(
                onSuccess = { itemPage ->
                    _uiState.value = if (itemPage.records.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(itemPage.records)
                    }
                },
                onFailure = { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "加载失败")
                }
            )
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Empty : HomeUiState()
    data class Success(val items: List<Item>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
