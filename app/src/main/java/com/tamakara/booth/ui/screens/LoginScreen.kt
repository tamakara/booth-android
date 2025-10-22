package com.tamakara.booth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tamakara.booth.data.local.PreferencesManager
import com.tamakara.booth.ui.viewmodel.AuthViewModel
import com.tamakara.booth.ui.viewmodel.LoginState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                val userId = state.userId.toLongOrNull() ?: -1L
                prefsManager.saveUserInfo(userId, state.userId, phone)
                snackbarHostState.showSnackbar(if (isRegisterMode) "注册成功" else "登录成功")
                navController.popBackStack()
            }

            is LoginState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isRegisterMode) "注册" else "登录") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "欢迎来到 Booth",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("手机号") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (phone.isNotBlank() && password.isNotBlank()) {
                        if (isRegisterMode) {
                            viewModel.register(phone, password)
                        } else {
                            viewModel.login(phone, password)
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("请填写完整信息")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isRegisterMode) "注册" else "登录")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                Text(if (isRegisterMode) "已有账号？去登录" else "还没有账号？去注册")
            }
        }
    }
}