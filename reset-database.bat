@echo off
REM ============================================
REM RESET DATABASE SCRIPT FOR WINDOWS
REM ============================================
REM Script này sẽ:
REM 1. Dừng và xóa container database cũ
REM 2. Xóa dữ liệu database cũ (thư mục mariadb_data)
REM 3. Khởi động lại database với dữ liệu mới từ mariadb_init/
REM ============================================

setlocal enabledelayedexpansion

echo.
echo ============================================
echo   RESET DATABASE - COFFEE SHOP
echo ============================================
echo.
echo WARNING: This will DELETE ALL database data!
echo.
set /p confirm="Are you sure you want to continue? (yes/no): "

if /i not "%confirm%"=="yes" (
    echo.
    echo Operation cancelled.
    pause
    exit /b 0
)

echo.
echo Starting database reset...
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

echo [1/6] Stopping database container...
docker-compose down
if errorlevel 1 (
    echo [WARNING] Failed to stop containers, continuing...
)
echo Done!

echo.
echo [2/6] Removing old database data...
if exist mariadb_data (
    echo Deleting mariadb_data folder...
    rmdir /s /q mariadb_data
    if exist mariadb_data (
        echo [ERROR] Failed to delete mariadb_data folder!
        echo Please close any programs using this folder and try again.
        pause
        exit /b 1
    )
    echo Database data removed successfully!
) else (
    echo No old database data found, skipping...
)

echo.
echo [3/6] Starting fresh database container...
docker-compose up -d mariadb
if errorlevel 1 (
    echo [ERROR] Failed to start database!
    echo.
    pause
    exit /b 1
)
echo Database container started!

echo.
echo [4/6] Waiting for database to initialize...
echo This may take 30-60 seconds...
echo.

REM Đợi database healthy
set /a counter=0
:wait_loop
docker inspect --format="{{.State.Health.Status}}" coffee_shop_db 2>nul | findstr "healthy" >nul
if errorlevel 1 (
    set /a counter+=1
    if !counter! gtr 60 (
        echo [ERROR] Database failed to start after 60 seconds!
        echo Please check: docker logs coffee_shop_db
        pause
        exit /b 1
    )
    echo Waiting... (!counter!/60 seconds^)
    timeout /t 1 /nobreak >nul
    goto wait_loop
)

echo Database is healthy!

echo.
echo [5/6] Verifying database initialization...
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SELECT COUNT(*) as total_users FROM users;" 2>nul
if errorlevel 1 (
    echo [WARNING] Database tables may not be initialized yet.
    echo Waiting 10 more seconds...
    timeout /t 10 /nobreak >nul
    docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SELECT COUNT(*) as total_users FROM users;"
)

echo.
echo [6/6] Verifying admin account...
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SELECT username, email, role FROM users WHERE username='admin';"

echo.
echo ============================================
echo   DATABASE RESET COMPLETED SUCCESSFULLY!
echo ============================================
echo.
echo Database Information:
echo   Host: localhost
echo   Port: 3307
echo   Database: java5_asm
echo   Username: java5_user
echo   Password: java5_password
echo.
echo Admin Login Credentials:
echo   Username: admin
echo   Email: admin@grocerystore.com
echo   Password: password123
echo.
echo You can now start the Spring Boot application!
echo ============================================
echo.
pause
