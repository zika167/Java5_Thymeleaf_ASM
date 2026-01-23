# Hướng dẫn Setup Database

## 🔑 Thông tin đăng nhập Admin

**Username:** `admin`  
**Email:** `admin@grocerystore.com`  
**Password:** `password123`

---

## 🚀 Khi pull code mới về

### Cách 1: Chỉ update password admin (KHUYÊN DÙNG - Giữ nguyên dữ liệu)

Chạy script này để cập nhật password admin mà **KHÔNG MẤT** dữ liệu:

```bash
update-admin-password.bat
```

✅ **Ưu điểm:** Giữ nguyên tất cả dữ liệu (orders, products, users...)  
✅ **Chỉ thay đổi:** Password của tài khoản admin

---

### Cách 2: Reset toàn bộ database (Xóa hết dữ liệu)

Nếu muốn database sạch như lúc đầu:

```bash
reset-database.bat
```

⚠️ **Cảnh báo:** Script này sẽ **XÓA TẤT CẢ** dữ liệu và tạo lại từ đầu!

---

## 📊 Thông tin Database

- **Host:** localhost
- **Port:** 3307
- **Database:** java5_asm
- **Username:** java5_user
- **Password:** java5_password

---

## 🔧 Troubleshooting

### Lỗi: "Could not connect to database"
1. Kiểm tra Docker Desktop đang chạy
2. Chạy: `docker-compose up -d mariadb`
3. Đợi 30 giây để database khởi động

### Lỗi: "Invalid username or password"
1. Chạy: `update-admin-password.bat`
2. Thử đăng nhập lại với password: `password123`

### Lỗi: Database bị lỗi không sửa được
1. Chạy: `reset-database.bat`
2. Chọn `yes` để xác nhận xóa dữ liệu
3. Đợi script chạy xong
