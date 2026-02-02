@echo off
REM ============================================
REM UPDATE ADMIN PASSWORD SCRIPT
REM ============================================
REM Script này CHỈ update password admin
REM KHÔNG xóa dữ liệu khác trong database
REM ============================================

echo.
echo ============================================
echo   UPDATE ADMIN PASSWORD
echo ============================================
echo.

REM Kiểm tra Docker đang chạy
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not running!
    echo Please start Docker Desktop first.
    echo.
    pause
    exit /b 1
)

REM Kiểm tra database container đang chạy
docker ps | findstr coffee_shop_db >nul
if errorlevel 1 (
    echo [ERROR] Database container is not running!
    echo Please start it with: docker-compose up -d mariadb
    echo.
    pause
    exit /b 1
)

echo Updating admin password to: password123
echo.

docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "UPDATE users SET password='$2a$10$547jHD5iIaAp/7g1OKx3rO4cRul78RDFuaeeZT60rHCvyfnLSB2WO' WHERE username='admin';"

if errorlevel 1 (
    echo [ERROR] Failed to update password!
    pause
    exit /b 1
)

echo.
echo Verifying admin account...
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SELECT username, email, role FROM users WHERE username='admin';"

echo.
echo ============================================
echo   PASSWORD UPDATE COMPLETED!
echo ============================================
echo.
echo Admin Login Credentials:
echo   Username: admin
echo   Email: admin@grocerystore.com
echo   Password: password123
echo.
echo ============================================
pause
