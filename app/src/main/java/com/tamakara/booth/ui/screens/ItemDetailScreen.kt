package com.tamakara.booth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tamakara.booth.data.local.PreferencesManager
import com.tamakara.booth.ui.viewmodel.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
    itemId: Long,
    viewModel: ItemDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    val orderState by viewModel.orderState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(itemId) {
        val userId = prefsManager.userId.first()
        viewModel.loadItem(if (userId > 0) userId else null, itemId)
    }

    LaunchedEffect(orderState) {
        when (val state = orderState) {
            is OrderState.Success -> {
                snackbarHostState.showSnackbar("订单创建成功，订单号: ${state.orderId}")
                viewModel.resetOrderState()
            }
            is OrderState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOrderState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ItemDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ItemDetailUiState.Success -> {
                    val item = state.item
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 商品图片
                        if (item.images.isNotEmpty()) {
                            AsyncImage(
                                model = item.images.first(),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            // 价格
                            Text(
                                text = "¥${item.price}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 商品名称
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 商品描述
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "商品描述",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 购买按钮
                            if (!item.isSeller) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            val userId = prefsManager.userId.first()
                                            if (userId > 0) {
                                                viewModel.createOrder(userId, itemId)
                                            } else {
                                                snackbarHostState.showSnackbar("请先登录")
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = orderState !is OrderState.Loading
                                ) {
                                    if (orderState is OrderState.Loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text("立即购买")
                                    }
                                }
                            }
                        }
                    }
                }
                is ItemDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            scope.launch {
                                val userId = prefsManager.userId.first()
                                viewModel.loadItem(if (userId > 0) userId else null, itemId)
                            }
                        }) {
                            Text("重试")
                        }
                    }
                }
            }
        }
    }
}

