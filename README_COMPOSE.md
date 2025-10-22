# Booth - C2C二手交易平台 (Jetpack Compose版本)

## 🎯 为什么使用 Jetpack Compose？

### ✨ Compose 的优势

1. **声明式 UI**
   - 用简洁的代码描述UI应该是什么样子，而不是如何构建
   - 更直观，更易于理解和维护
   
2. **更少的代码**
   - 不需要XML布局文件
   - 不需要findViewById
   - 不需要适配器样板代码
   
3. **实时预览**
   - 使用 @Preview 注解即时查看UI效果
   - 无需编译运行即可看到变化
   
4. **类型安全**
   - 编译时检查，减少运行时错误
   - 自动补全和重构支持更好
   
5. **现代化开发**
   - Google官方推荐的Android UI开发方式
   - 持续更新和社区支持
   - 更好的性能和动画支持

## 📱 应用架构

### MVVM + Compose
```
UI Layer (Compose)
    ↓
ViewModel (LiveData/StateFlow)
    ↓
Repository
    ↓
API Service (Retrofit)
```

## 🎨 已实现的功能

### 1. 首页 (HomeScreen)
- ✅ 网格布局展示商品
- ✅ 下拉刷新 (Accompanist SwipeRefresh)
- ✅ 异步图片加载 (Coil)
- ✅ 点击跳转详情页
- ✅ 加载状态和空状态处理

### 2. 登录注册 (LoginScreen)
- ✅ Material 3 设计语言
- ✅ 表单验证
- ✅ 加载状态显示
- ✅ SnackBar 提示
- ✅ 自动保存登录状态

### 3. 发布商品 (PublishScreen)
- ✅ 多字段表单
- ✅ 数字键盘优化
- ✅ 滚动布局
- ✅ 验证和提交
- ✅ 成功后自动跳转

### 4. 商品详情 (ItemDetailScreen)
- ✅ 图片轮播 (Accompanist HorizontalPager)
- ✅ 轮播指示器
- ✅ 收藏功能（带动画图标）
- ✅ 立即购买（创建订单）
- ✅ 底部操作栏
- ✅ 卖家信息展示

### 5. 个人中心 (ProfileScreen)
- ✅ 用户信息卡片
- ✅ 登录状态管理
- ✅ 退出登录
- ✅ 我的商品入口

## 🔧 技术栈

### Compose 相关
- `Jetpack Compose BOM 2024.12.01` - Compose核心库
- `Material 3` - Material Design 3组件
- `Compose Navigation` - 导航组件
- `Coil Compose` - 图片加载
- `Accompanist` - Compose扩展库
  - Pager（图片轮播）
  - SwipeRefresh（下拉刷新）
  - SystemUIController（状态栏控制）

### 网络和数据
- `Retrofit 2.9.0` - REST API客户端
- `OkHttp 4.12.0` - HTTP客户端
- `Gson` - JSON解析
- `Kotlin Coroutines` - 异步处理

### 架构组件
- `ViewModel` - UI状态管理
- `LiveData` - 响应式数据
- `Lifecycle` - 生命周期感知

## 📦 项目结构

```
app/src/main/java/com/tamakara/booth/
├── data/
│   ├── api/              # API服务接口
│   ├── model/            # 数据模型
│   └── repository/       # 数据仓库
├── ui/
│   ├── compose/          # Compose UI (新)
│   │   ├── screens/      # 各个页面
│   │   ├── theme/        # 主题配置
│   │   ├── BoothApp.kt   # 主应用组件
│   │   └── ComposeMainActivity.kt
│   ├── auth/             # 认证相关ViewModel
│   ├── home/             # 首页ViewModel
│   └── item/             # 商品相关ViewModel
└── utils/                # 工具类
```

## 🎯 Compose vs 传统View对比

| 特性 | 传统View | Jetpack Compose |
|-----|---------|----------------|
| UI定义 | XML + Kotlin | 纯Kotlin |
| 代码量 | 多 | 少30-40% |
| 布局嵌套 | 容易过深 | 扁平化 |
| 状态管理 | 手动同步 | 自动重组 |
| 预览 | 需要运行 | 实时预览 |
| 动画 | 复杂 | 简单直观 |
| 学习曲线 | 平缓 | 稍陡但值得 |
| 性能 | 好 | 更好 |

## 🚀 使用说明

### 1. 同步项目
```bash
# Windows
gradlew.bat build

# 或在Android Studio中
File -> Sync Project with Gradle Files
```

### 2. 运行应用
- 点击 Run 按钮
- 选择模拟器或真机
- 应用将启动 ComposeMainActivity

### 3. 后端配置
确保后端服务运行在：
- 用户服务：localhost:8081
- 商品服务：localhost:8082
- 文件服务：localhost:8083
- 订单服务：localhost:8084

模拟器会通过 `10.0.2.2` 访问主机的 localhost

## 💡 核心代码示例

### 声明式UI示例
```kotlin
@Composable
fun ItemCard(item: ItemVO, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = item.images.firstOrNull(),
                contentDescription = item.name,
                modifier = Modifier.height(180.dp)
            )
            
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall
            )
            
            Text(
                text = "¥${item.price}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
```

### 状态管理示例
```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    var items by remember { mutableStateOf<List<ItemVO>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }
    
    viewModel.itemsPage.observeAsState().value?.onSuccess { page ->
        items = page.records
    }
    
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(items) { item ->
            ItemCard(item) { /* 导航到详情 */ }
        }
    }
}
```

## 🎨 Material 3 主题

应用使用了最新的 Material Design 3：
- 动态配色方案
- 圆角卡片设计
- 波纹效果
- 深色模式支持

## 📝 下一步优化

- [ ] 添加图片上传功能
- [ ] 实现订单列表页面
- [ ] 添加搜索功能
- [ ] 优化加载动画
- [ ] 添加骨架屏
- [ ] 实现无限滚动
- [ ] 添加过渡动画
- [ ] 支持深色模式切换

## 🌟 Compose特性展示

### 1. 下拉刷新
使用 `Accompanist SwipeRefresh`，无需自定义实现

### 2. 图片轮播
使用 `Accompanist HorizontalPager`，流畅且简单

### 3. 响应式状态
UI自动响应数据变化，无需手动notifyDataSetChanged

### 4. 类型安全导航
编译时检查路由参数，减少错误

## 📚 学习资源

- [Jetpack Compose官方文档](https://developer.android.com/jetpack/compose)
- [Compose Samples](https://github.com/android/compose-samples)
- [Material 3](https://m3.material.io/)

---

**Jetpack Compose** 代表了Android UI开发的未来！🚀

