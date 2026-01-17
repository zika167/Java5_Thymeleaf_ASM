# 🗄️ HƯỚNG DẪN SETUP DATABASE

## 📋 Tổng quan

Dự án sử dụng **MariaDB** chạy trong **Docker Container** với dữ liệu mẫu được tự động khởi tạo.

---

## 🚀 Cách 1: Sử dụng Docker (Khuyến nghị)

### Bước 1: Cài đặt Docker

Nếu chưa có Docker, tải và cài đặt:
- **macOS/Windows**: [Docker Desktop](https://www.docker.com/products/docker-desktop)
- **Linux**: 
  ```bash
  curl -fsSL https://get.docker.com -o get-docker.sh
  sudo sh get-docker.sh
  ```

### Bước 2: Khởi động MariaDB

```bash
# Khởi động container
docker-compose up -d

# Kiểm tra container đang chạy
docker ps

# Xem logs
docker-compose logs -f mariadb
```

### Bước 3: Kiểm tra Database

```bash
# Kết nối vào MariaDB
docker exec -it coffee_shop_db mariadb -u java5_user -pjava5_password java5_asm

# Trong MariaDB shell, kiểm tra tables
SHOW TABLES;

# Kiểm tra dữ liệu
SELECT * FROM users;
SELECT * FROM products;

# Thoát
EXIT;
```

### Bước 4: Chạy Spring Boot

```bash
# Build và chạy
./mvnw spring-boot:run

# Hoặc trong IDE: Run Java5AsmApplication.java
```

### Bước 5: Truy cập ứng dụng

Mở trình duyệt: `http://localhost:8080`

---

## 🔄 Quản lý Docker Container

### Dừng container
```bash
docker-compose down
```

### Khởi động lại
```bash
docker-compose up -d
```

### Xem logs
```bash
docker-compose logs -f mariadb
```

### Reset database (xóa tất cả dữ liệu)
```bash
# 1. Dừng container
docker-compose down

# 2. Xóa data
rm -rf mariadb_data/

# 3. Khởi động lại (sẽ chạy init scripts)
docker-compose up -d
```

---

## 🔧 Cách 2: Cài đặt MariaDB thủ công

### Bước 1: Cài đặt MariaDB

**macOS (Homebrew):**
```bash
brew install mariadb
brew services start mariadb
```

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mariadb-server
sudo systemctl start mariadb
```

**Windows:**
- Tải từ: https://mariadb.org/download/
- Chạy installer và làm theo hướng dẫn

### Bước 2: Tạo Database và User

```bash
# Đăng nhập MariaDB
sudo mariadb

# Hoặc
mysql -u root -p
```

Trong MariaDB shell:
```sql
-- Tạo database
CREATE DATABASE java5_asm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo user
CREATE USER 'java5_user'@'localhost' IDENTIFIED BY 'java5_password';

-- Cấp quyền
GRANT ALL PRIVILEGES ON java5_asm.* TO 'java5_user'@'localhost';
FLUSH PRIVILEGES;

-- Thoát
EXIT;
```

### Bước 3: Import Schema và Data

```bash
# Import schema
mariadb -u java5_user -pjava5_password java5_asm < mariadb_init/01-schema.sql

# Import data
mariadb -u java5_user -pjava5_password java5_asm < mariadb_init/02-data.sql
```

### Bước 4: Cập nhật application.properties

Sửa file `src/main/resources/application.properties`:

```properties
# Thay đổi port từ 3307 thành 3306
spring.datasource.url=jdbc:mariadb://localhost:3306/java5_asm
```

### Bước 5: Chạy Spring Boot

```bash
./mvnw spring-boot:run
```

---

## 📊 Thông tin Database

### Connection Details

| Thông tin | Giá trị |
|-----------|---------|
| **Host** | localhost |
| **Port** | 3307 (Docker) / 3306 (Local) |
| **Database** | java5_asm |
| **Username** | java5_user |
| **Password** | java5_password |
| **Root Password** | rootpassword (chỉ Docker) |

### Database Structure

```
java5_asm
├── users (4 records)
├── addresses (4 records)
├── payment_methods (4 records)
├── categories (9 records)
├── brands (5 records)
├── products (8 records)
├── product_images (13 records)
├── product_variants (10 records)
├── reviews (8 records)
├── wishlists (7 records)
├── banners (5 records)
├── carts (3 records)
├── cart_items (5 records)
├── orders (3 records)
└── order_items (5 records)
```

---

## 🔐 Tài khoản mẫu

### Admin Account
- **Username:** admin
- **Email:** admin@grocerystore.com
- **Password:** password123
- **Role:** ADMIN

### User Accounts
| Username | Email | Password | Role |
|----------|-------|----------|------|
| imrankhan | imran@example.com | password123 | USER |
| johnsmith | john@example.com | password123 | USER |
| maryjane | mary@example.com | password123 | USER |

---

## 🛠️ Troubleshooting

### Lỗi: Port 3307 đã được sử dụng

```bash
# Kiểm tra process đang dùng port
lsof -i :3307

# Hoặc thay đổi port trong docker-compose.yml
ports:
  - "3308:3306"  # Đổi 3307 thành 3308
```

### Lỗi: Cannot connect to database

```bash
# Kiểm tra container đang chạy
docker ps

# Xem logs
docker-compose logs mariadb

# Restart container
docker-compose restart mariadb
```

### Lỗi: Access denied for user

```bash
# Kiểm tra username/password trong application.properties
# Phải khớp với docker-compose.yml
```

### Reset database không hoạt động

```bash
# Xóa hoàn toàn container và volume
docker-compose down -v
rm -rf mariadb_data/
docker-compose up -d
```

---

## 📚 Tài liệu tham khảo

- [MariaDB Documentation](https://mariadb.org/documentation/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Data JPA](https://spring.io/projects/spring-data-jpa)

---

## 💡 Tips

1. **Development:** Dùng Docker để dễ dàng reset database
2. **Production:** Cài đặt MariaDB riêng và backup thường xuyên
3. **Security:** Đổi password mặc định trong production
4. **Performance:** Tạo index cho các cột thường query
5. **Backup:** Sử dụng `mysqldump` để backup database

```bash
# Backup database
docker exec coffee_shop_db mysqldump -u java5_user -pjava5_password java5_asm > backup.sql

# Restore database
docker exec -i coffee_shop_db mariadb -u java5_user -pjava5_password java5_asm < backup.sql
```

---

✅ **Database đã sẵn sàng sử dụng!**
