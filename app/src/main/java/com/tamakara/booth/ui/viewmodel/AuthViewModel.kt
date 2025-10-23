package com.tamakara.booth.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tamakara.booth.data.local.PreferencesManager
import com.tamakara.booth.data.repository.BoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BoothRepository(application.applicationContext)
    private val prefsManager = PreferencesManager(application.applicationContext)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            repository.login(phone, password).fold(
                onSuccess = { token ->
                    // 1) 写入 SharedPreferences 给拦截器使用
                    repository.saveToken(token)
                    // 2) 立即通知 UI 登录成功
                    _loginState.value = LoginState.Success(token)
                    // 3) 后台直接调用后端获取当前用户信息（依赖拦截器携带的 Authorization）
                    viewModelScope.launch {
                        val userResult = repository.getCurrentUser()
                        if (userResult.isSuccess) {
                            val user = userResult.getOrNull()
                            if (user != null) {
                                prefsManager.saveUserInfo(user.id, token, user.phone ?: phone)
                            } else {
                                prefsManager.saveToken(token)
                            }
                        } else {
                            // 拉取失败则至少保存 token
                            prefsManager.saveToken(token)
                        }
                    }
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
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
