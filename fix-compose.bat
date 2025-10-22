@echo off
echo ========================================
echo Booth 项目 Compose 依赖修复脚本
echo ========================================
echo.

echo [1/5] 停止 Gradle 守护进程...
call gradlew.bat --stop
echo ✓ 完成
echo.

echo [2/5] 清理项目构建缓存...
call gradlew.bat clean
echo ✓ 完成
echo.

echo [3/5] 删除本地缓存...
if exist .gradle (
    echo 删除 .gradle 文件夹...
    rmdir /s /q .gradle
)
if exist app\build (
    echo 删除 app\build 文件夹...
    rmdir /s /q app\build
)
if exist build (
    echo 删除 build 文件夹...
    rmdir /s /q build
)
echo ✓ 完成
echo.

echo [4/5] 强制刷新依赖并构建...
call gradlew.bat build --refresh-dependencies --no-daemon
echo ✓ 完成
echo.

echo [5/5] 验证构建...
call gradlew.bat assemble
echo ✓ 完成
echo.

echo ========================================
echo 修复完成！
echo ========================================
echo.
echo 下一步：
echo 1. 在 Android Studio 中点击 File -^> Sync Project with Gradle Files
echo 2. 等待同步完成
echo 3. 运行应用
echo.
pause

