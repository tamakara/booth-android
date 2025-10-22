# 🔧 Jetpack Compose 项目配置和同步指南

## ⚠️ 当前状态
项目已完成所有代码编写，但需要先同步 Gradle 依赖才能消除编译错误。

## ✅ 立即执行的步骤

### 1️⃣ **在 Android Studio 中同步项目**

**方式一：使用顶部提示栏**
- 打开项目后，Android Studio 顶部会显示黄色横幅
- 点击 **"Sync Now"** 按钮
- 等待 Gradle 同步完成（首次可能需要 5-10 分钟）

**方式二：手动触发同步**
- 点击菜单：`File` → `Sync Project with Gradle Files`
- 或使用快捷键：`Ctrl + Shift + O` (Windows/Linux) 或 `Cmd + Shift + O` (Mac)

**方式三：使用 Gradle 工具栏**
- 点击右上角的大象图标（Gradle）
- 点击刷新图标 🔄

### 2️⃣ **等待依赖下载**
同步过程中会下载以下依赖：
- Jetpack Compose BOM 2024.12.01
- Material 3 组件
- Coil 图片加载库
- Accompanist 扩展库
- Retrofit + OkHttp
- Kotlin Coroutines

**进度查看：**
- 底部状态栏会显示下载进度
- Build 窗口显示详细日志

### 3️⃣ **验证同步成功**
同步完成后：
- ✅ 所有红色错误消失
- ✅ Import 语句正常
- ✅ 可以看到代码自动补全

---

## 🚀 如果同步失败

### 方案 A：清理并重建
```bash
# 在 Android Studio 终端执行
gradlew clean
gradlew build --refresh-dependencies
```

### 方案 B：删除缓存
1. 关闭 Android Studio
2. 删除以下目录：
   - `.gradle/` 文件夹
   - `.idea/` 文件夹
   - `app/build/` 文件夹
3. 重新打开项目

### 方案 C：检查网络
- 确保可以访问 Google Maven 和 Maven Central
- 如需代理，配置 `gradle.properties`：
```properties
systemProp.http.proxyHost=your-proxy
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=your-proxy
systemProp.https.proxyPort=8080
```

---

## 📝 项目配置说明

### ✅ 已配置完成

#### 1. **项目级 build.gradle.kts**
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
```

#### 2. **应用级 build.gradle.kts**
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    // ... 其他 Compose 依赖
}
```

#### 3. **libs.versions.toml**
包含所有版本号定义

#### 4. **AndroidManifest.xml**
- 启动 Activity 设置为 `ComposeMainActivity`
- 已添加网络权限
- 配置 `usesCleartextTraffic="true"`

---

## 🎯 同步后的下一步

### 1. **运行应用**
- 点击绿色播放按钮 ▶️
- 选择模拟器或真机
- 应用将启动 Compose UI

### 2. **确保后端运行**
模拟器通过 `10.0.2.2` 访问主机的 localhost：
- 用户服务：http://10.0.2.2:8081
- 商品服务：http://10.0.2.2:8082
- 文件服务：http://10.0.2.2:8083
- 订单服务：http://10.0.2.2:8084

### 3. **测试功能**
- ✅ 首页商品列表
- ✅ 登录注册
- ✅ 发布商品
- ✅ 商品详情
- ✅ 收藏功能
- ✅ 创建订单

---

## 🐛 常见问题

### Q1: 同步卡住不动
**A:** 
- 检查网络连接
- 关闭 VPN 后重试
- 使用阿里云镜像（中国用户）

### Q2: 提示 "Compose Compiler Plugin Required"
**A:** 
- 已在项目中配置 `org.jetbrains.kotlin.plugin.compose`
- 确保 Kotlin 版本为 2.0.21

### Q3: Import 仍然是红色
**A:**
- 等待 Gradle 索引完成（右下角进度条）
- File → Invalidate Caches → Restart

### Q4: 运行时找不到依赖
**A:**
```bash
gradlew clean
gradlew assembleDebug
```

---

## 📱 已实现的页面

### 1. **ComposeMainActivity**
- Material 3 主题
- 底部导航栏
- Navigation Compose

### 2. **HomeScreen**
- 网格布局商品列表
- 下拉刷新
- Coil 图片加载

### 3. **LoginScreen**
- 手机号密码登录
- 一键注册

### 4. **PublishScreen**
- 发布商品表单
- 验证和提交

### 5. **ItemDetailScreen**
- 图片轮播
- 收藏功能
- 立即购买

### 6. **ProfileScreen**
- 用户信息
- 登录/退出

---

## 💡 为什么使用 Jetpack Compose？

| 优势 | 说明 |
|-----|------|
| 🚀 **代码量少 40%** | 无需 XML，无需 Adapter |
| 🎨 **声明式 UI** | 描述"是什么"而非"怎么做" |
| ⚡ **实时预览** | `@Preview` 即时查看效果 |
| 🔒 **类型安全** | 编译时检查，减少错误 |
| 🌟 **Material 3** | 最新设计语言 |
| 🔄 **自动重组** | 状态改变，UI 自动更新 |

---

## 🎉 完成同步后

**恭喜！** 你将拥有一个完整的、基于 Jetpack Compose 的 C2C 二手交易平台！

### 技术栈
- ✅ Kotlin 2.0.21
- ✅ Jetpack Compose + Material 3
- ✅ MVVM 架构
- ✅ Retrofit + OkHttp
- ✅ Kotlin Coroutines
- ✅ Coil 图片加载
- ✅ Navigation Compose

---

**现在请在 Android Studio 中点击 "Sync Project with Gradle Files" 开始同步！** 🚀

