@echo off
REM ============================================
REM REFACTOR TO MODULE-BASED ARCHITECTURE (Windows)
REM ============================================
REM Script tự động di chuyển files sang cấu trúc module mới
REM 
REM CẢNH BÁO: Script này sẽ thay đổi cấu trúc toàn bộ dự án!
REM Hãy commit code trước khi chạy!
REM ============================================

echo ============================================
echo   REFACTORING TO MODULE-BASED ARCHITECTURE
echo ============================================
echo.
echo WARNING: Script này sẽ thay đổi cấu trúc toàn bộ dự án!
echo.
set /p confirm="Bạn đã commit code chưa? (yes/no): "

if not "%confirm%"=="yes" (
    echo Vui lòng commit code trước khi chạy script!
    exit /b 1
)

set BASE_PATH=src\main\java\poly\edu\java5_asm

echo.
echo Bước 1: Tạo cấu trúc thư mục mới...
echo.

REM Create common structure
mkdir "%BASE_PATH%\common\config" 2>nul
mkdir "%BASE_PATH%\common\exception" 2>nul
mkdir "%BASE_PATH%\common\security" 2>nul
mkdir "%BASE_PATH%\common\util" 2>nul
mkdir "%BASE_PATH%\common\controller" 2>nul

REM Create module structures
for %%m in (auth user product category brand cart order payment review wishlist address email caffeine admin) do (
    mkdir "%BASE_PATH%\module\%%m\controller" 2>nul
    mkdir "%BASE_PATH%\module\%%m\dto\request" 2>nul
    mkdir "%BASE_PATH%\module\%%m\dto\response" 2>nul
    mkdir "%BASE_PATH%\module\%%m\entity" 2>nul
    mkdir "%BASE_PATH%\module\%%m\repository" 2>nul
    mkdir "%BASE_PATH%\module\%%m\service" 2>nul
    mkdir "%BASE_PATH%\module\%%m\exception" 2>nul
    echo   Created module: %%m
)

echo.
echo Bước 2: Di chuyển Common files...
echo.

REM Move config files
if exist "%BASE_PATH%\config" (
    for %%f in ("%BASE_PATH%\config\*.java") do (
        git mv "%%f" "%BASE_PATH%\common\config\" 2>nul || move "%%f" "%BASE_PATH%\common\config\"
        echo   Moved: %%~nxf
    )
)

REM Move exception files
if exist "%BASE_PATH%\exception" (
    for %%f in ("%BASE_PATH%\exception\*.java") do (
        git mv "%%f" "%BASE_PATH%\common\exception\" 2>nul || move "%%f" "%BASE_PATH%\common\exception\"
        echo   Moved: %%~nxf
    )
)

REM Move security files
if exist "%BASE_PATH%\security" (
    for %%f in ("%BASE_PATH%\security\*.java") do (
        git mv "%%f" "%BASE_PATH%\common\security\" 2>nul || move "%%f" "%BASE_PATH%\common\security\"
        echo   Moved: %%~nxf
    )
)

REM Move util files
if exist "%BASE_PATH%\util" (
    for %%f in ("%BASE_PATH%\util\*.java") do (
        git mv "%%f" "%BASE_PATH%\common\util\" 2>nul || move "%%f" "%BASE_PATH%\common\util\"
        echo   Moved: %%~nxf
    )
)

echo.
echo Bước 3: Di chuyển Module files...
echo.

REM Note: Windows batch doesn't support functions like bash
REM So we'll do it manually for each module

REM AUTH MODULE
echo   Auth module...
if exist "%BASE_PATH%\controller\AuthController.java" (
    git mv "%BASE_PATH%\controller\AuthController.java" "%BASE_PATH%\module\auth\controller\" 2>nul || move "%BASE_PATH%\controller\AuthController.java" "%BASE_PATH%\module\auth\controller\"
)
if exist "%BASE_PATH%\service\AuthService.java" (
    git mv "%BASE_PATH%\service\AuthService.java" "%BASE_PATH%\module\auth\service\" 2>nul || move "%BASE_PATH%\service\AuthService.java" "%BASE_PATH%\module\auth\service\"
)

REM USER MODULE
echo   User module...
if exist "%BASE_PATH%\entity\User.java" (
    git mv "%BASE_PATH%\entity\User.java" "%BASE_PATH%\module\user\entity\" 2>nul || move "%BASE_PATH%\entity\User.java" "%BASE_PATH%\module\user\entity\"
)
if exist "%BASE_PATH%\repository\UserRepository.java" (
    git mv "%BASE_PATH%\repository\UserRepository.java" "%BASE_PATH%\module\user\repository\" 2>nul || move "%BASE_PATH%\repository\UserRepository.java" "%BASE_PATH%\module\user\repository\"
)

REM Continue for other modules...
REM (Due to batch limitations, recommend using IntelliJ IDEA refactoring instead)

echo.
echo ============================================
echo   MIGRATION STARTED!
echo ============================================
echo.
echo KHUYẾN NGHỊ: Sử dụng IntelliJ IDEA để refactor
echo.
echo 1. Mở project trong IntelliJ IDEA
echo 2. Sử dụng Refactor -^> Move để di chuyển files
echo 3. IntelliJ sẽ tự động update imports
echo.
echo Xem file REFACTORING_GUIDE.md để biết chi tiết!
echo.
echo ============================================

pause
