package com.tamakara.booth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tamakara.booth.data.local.PreferencesManager
import com.tamakara.booth.ui.navigation.Screen
import com.tamakara.booth.ui.viewmodel.PublishState
import com.tamakara.booth.ui.viewmodel.PublishViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    navController: NavController,
    viewModel: PublishViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var postage by remember { mutableStateOf("0") }

    val publishState by viewModel.publishState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(publishState) {
        when (val state = publishState) {
            is PublishState.Success -> {
                snackbarHostState.showSnackbar("发布成功！商品ID: ${state.itemId}")
                viewModel.resetState()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
            is PublishState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("发布商品") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("商品名称") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("价格") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("¥") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = postage,
                onValueChange = { postage = it },
                label = { Text("邮费") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("¥") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("商品描述") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank() || description.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("请填写完整信息")
                        }
                        return@Button
                    }

                    val priceValue = price.toDoubleOrNull()
                    val postageValue = postage.toDoubleOrNull() ?: 0.0

                    if (priceValue == null || priceValue <= 0) {
                        scope.launch {
                            snackbarHostState.showSnackbar("请输入有效价格")
                        }
                        return@Button
                    }

                    scope.launch {
                        val userId = prefsManager.userId.first()
                        if (userId > 0) {
                            viewModel.publishItem(userId, name, description, priceValue, postageValue)
                        } else {
                            snackbarHostState.showSnackbar("请先登录")
                            navController.navigate(Screen.Login.route)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = publishState !is PublishState.Loading
            ) {
                if (publishState is PublishState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("发布")
                }
            }
        }
    }
}

