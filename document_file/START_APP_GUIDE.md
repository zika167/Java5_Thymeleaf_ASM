# Hướng Dẫn Start Application

## ✅ Đã Fix Xong

Database container đã được start và đang chạy healthy.

## 🚀 Cách Start Application

### Option 1: Từ IntelliJ (Recommended)
1. Mở file `Java5AsmApplication.java`
2. Click vào nút ▶️ Run bên cạnh `public static void main`
3. Hoặc nhấn `Ctrl + Shift + F10` (Windows) / `Cmd + Shift + R` (Mac)

### Option 2: Từ Terminal
```bash
./mvnw spring-boot:run
```

### Option 3: Run JAR file
```bash
./mvnw clean package -DskipTests
java -jar target/java5_asm-0.0.1-SNAPSHOT.jar
```

## 📊 Kiểm Tra Database

Database đang chạy với thông tin:
- **Host**: localhost
- **Port**: 3307
- **Database**: java5_asm
- **Username**: java5_user
- **Password**: java5_password

Kiểm tra data:
```bash
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password -e "USE java5_asm; SELECT COUNT(*) FROM users; SELECT COUNT(*) FROM products;"
```

## 🌐 Access Application

Sau khi start thành công:
- **User Site**: http://localhost:8080
- **Admin Dashboard**: http://localhost:8080/admin/dashboard
- **CC-Doctor**: http://localhost:8080/cc-doctor
- **Login**: admin / password123

## ⚠️ Nếu Vẫn Lỗi

### Lỗi: Connection refused
```bash
# Check container đang chạy
docker ps

# Nếu không có, start lại
docker compose up -d

# Đợi 10 giây để database khởi động
sleep 10
```

### Lỗi: Port 3307 already in use
```bash
# Tìm process đang dùng port
lsof -i :3307

# Kill process đó hoặc đổi port trong application.properties
```

### Lỗi: Import errors trong IntelliJ
1. `File` → `Invalidate Caches...`
2. Check all options
3. Click `Invalidate and Restart`

## 📝 Logs

Xem logs của database:
```bash
docker logs coffee_shop_db
```

Xem logs của application:
- Trong IntelliJ: Tab "Run" ở dưới
- Trong terminal: Logs sẽ hiện trực tiếp

## ✅ Success Indicators

Application start thành công khi thấy:
```
Started Java5AsmApplication in X.XXX seconds
Tomcat started on port 8080
```

Database connection thành công khi thấy:
```
HikariPool-1 - Start completed
```

## 🎉 Ready!

Bây giờ bạn có thể:
1. Browse products: http://localhost:8080
2. Login as admin: http://localhost:8080/sign-in
3. Test CC-Doctor: http://localhost:8080/cc-doctor
4. Access admin panel: http://localhost:8080/admin/dashboard
