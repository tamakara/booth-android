package com.tamakara.booth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamakara.booth.data.model.*
import com.tamakara.booth.data.repository.BoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = BoothRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            repository.login(phone, password).fold(
                onSuccess = { userId ->
                    _loginState.value = LoginState.Success(userId)
                },
                onFailure = { error ->
                    _loginState.value = LoginState.Error(error.message ?: "登录失败")
                }
            )
        }
    }

    fun register(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            repository.register(phone, password).fold(
                onSuccess = { userId ->
                    _loginState.value = LoginState.Success(userId)
                },
                onFailure = { error ->
                    _loginState.value = LoginState.Error(error.message ?: "注册失败")
                }
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

