
---

## 💡 提示

- 首次同步可能需要 **10-20 分钟**（下载约 100MB 依赖）
- 确保有 **稳定的网络连接**
- 关闭 **防火墙/VPN**（如果干扰下载）
- 检查 **磁盘空间**（至少 2GB 可用）

---

**现在请执行"步骤 1"开始修复！** 🚀
# ❗ Compose 依赖未加载问题修复指南

## 🔍 问题诊断

你看到的所有 "Unresolved reference 'compose'" 错误表明：
- ✅ 代码是正确的
- ❌ Gradle 依赖没有成功下载或索引

## 🚀 立即执行修复步骤

### 步骤 1：清理项目（必须执行）

在 Android Studio 中：

1. **点击菜单栏：** `Build` → `Clean Project`
2. **等待完成后，点击：** `File` → `Invalidate Caches...`
3. **在弹出窗口中勾选：** 
   - ✅ Invalidate and Restart
   - ✅ Clear file system cache and Local History
   - ✅ Clear downloaded shared indexes
4. **点击 "Invalidate and Restart"**
5. **等待 Android Studio 重启**

### 步骤 2：重新同步（重启后执行）

Android Studio 重启后：

1. **点击顶部的 "Sync Project with Gradle Files" 图标** 🔄
2. **或使用快捷键：** `Ctrl + Shift + O` (Windows)
3. **等待同步完成**（可能需要 5-15 分钟）

### 步骤 3：检查 Gradle 输出

在底部的 "Build" 窗口查看：
- ✅ 看到 "BUILD SUCCESSFUL" - 继续下一步
- ❌ 看到错误 - 查看下方的"常见错误解决方案"

### 步骤 4：重建项目

1. **点击菜单：** `Build` → `Rebuild Project`
2. **等待编译完成**

---

## 🔧 如果仍然失败，执行命令行修复

### Windows 用户（在项目根目录打开 CMD）

```cmd
REM 1. 停止 Gradle 守护进程
gradlew.bat --stop

REM 2. 清理构建缓存
gradlew.bat clean

REM 3. 强制刷新依赖
gradlew.bat build --refresh-dependencies

REM 4. 如果网络问题，添加代理（可选）
REM gradlew.bat build -Dhttp.proxyHost=your-proxy -Dhttp.proxyPort=8080
```

---

## 🌐 网络问题解决方案

### 如果在中国大陆

创建或编辑 `gradle.properties` 文件（在项目根目录）：

```properties
# 使用阿里云镜像
systemProp.http.proxyHost=mirrors.aliyun.com
systemProp.https.proxyHost=mirrors.aliyun.com

# Maven 镜像
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m
```

然后在 `build.gradle.kts`（项目级）添加：

```kotlin
allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        google()
        mavenCentral()
    }
}
```

---

## 📋 验证修复成功

修复后应该看到：
1. ✅ 所有 import 语句变为蓝色/灰色（不再是红色）
2. ✅ 可以看到代码自动补全
3. ✅ 没有 "Unresolved reference" 错误
4. ✅ Build 窗口显示 "BUILD SUCCESSFUL"

---

## 🐛 常见错误及解决方案

### 错误 1: "Could not resolve androidx.compose:compose-bom:2024.12.01"

**原因：** 网络问题或仓库访问失败

**解决：**
1. 检查网络连接
2. 配置镜像（见上方"网络问题解决方案"）
3. 或降低 BOM 版本到 `2024.06.00`

### 错误 2: "Compose Compiler version incompatible"

**原因：** Kotlin 和 Compose 编译器版本不匹配

**解决：** 已在 `build.gradle.kts` 中正确配置，无需修改

### 错误 3: "Failed to download ... timeout"

**原因：** 下载超时

**解决：**
```cmd
gradlew.bat build --refresh-dependencies --no-daemon
```

### 错误 4: "Could not find org.jetbrains.kotlin.plugin.compose"

**原因：** Compose 编译器插件未找到

**解决：** 检查 `build.gradle.kts` 中是否有：
```kotlin
id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
```

---

## 🎯 终极解决方案（如果以上都失败）

### 方案 A：删除缓存重新开始

1. **关闭 Android Studio**
2. **删除以下文件夹：**
   ```
   C:\Users\suhxhee\Code\Android Studio\Booth\.gradle
   C:\Users\suhxhee\Code\Android Studio\Booth\.idea
   C:\Users\suhxhee\Code\Android Studio\Booth\app\build
   C:\Users\suhxhee\Code\Android Studio\Booth\build
   ```
3. **重新打开项目**
4. **等待 Gradle 自动同步**

### 方案 B：手动检查依赖

在 `app/build.gradle.kts` 中确认有：

```kotlin
dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    // ... 其他依赖
}
```

### 方案 C：降级版本

如果最新版本有问题，修改为稳定版本：

```kotlin
// 在 app/build.gradle.kts
val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
```

---

## 📞 如何确认问题已解决

打开任意 Compose 文件（如 `HomeScreen.kt`），检查：

```kotlin
import androidx.compose.material3.*  // 应该是灰色/蓝色，不是红色
import androidx.compose.runtime.*     // 应该是灰色/蓝色，不是红色

@Composable  // 应该被识别
fun HomeScreen() {
    Text("Hello")  // Text 应该可以点击跳转
}
```

---

## ✅ 预期结果

完成修复后：
- 🎯 **0 个编译错误**
- 🎯 **可以运行应用**
- 🎯 **所有 Compose API 都能自动补全**
- 🎯 **Build 成功**

