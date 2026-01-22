# 🔄 Hướng Dẫn Cập Nhật Code Mới

> **Dành cho team đang làm việc cùng dự án**

---

## 📥 Các Bước Cập Nhật

### 1. Pull code mới nhất

```bash
git pull origin develop
```

### 2. Cập nhật file `.env`

- Nhận file `.env` mới từ team lead (qua chat/email)
- Copy và **thay thế** file `.env` cũ trong thư mục project

**Nếu muốn cấu hình email riêng** (để gửi email test):
- Xem hướng dẫn chi tiết tại: [EMAIL_SETUP_GUIDE.md](EMAIL_SETUP_GUIDE.md)

### 3. Reset database (BẮT BUỘC)

```bash
# Dừng và xóa database cũ
docker-compose down -v

# Start lại database mới
docker-compose up -d
```

**⚠️ Lưu ý**: Lệnh này sẽ **XÓA HẾT data cũ** vì database có thay đổi schema (thêm cột `provider`, `provider_id`)

### 4. Cập nhật dependencies

```bash
./mvnw clean install

# Hoặc trên Windows
mvnw.cmd clean install
```

### 5. Restart application trong IntelliJ

- Stop app đang chạy (nếu có)
- Run lại `Java5AsmApplication`

---

## ✅ Kiểm Tra

### Console phải hiển thị:
```
✅ Loaded .env file successfully
Started Java5AsmApplication in X.XXX seconds
```

### Truy cập ứng dụng:
```
http://localhost:8080
```

### Test Email (nếu đã cấu hình):

1. Đăng nhập vào hệ thống
2. Thêm sản phẩm vào giỏ hàng
3. Tiến hành đặt hàng
4. Kiểm tra email để nhận xác nhận đơn hàng

**Lưu ý**: Nếu không cấu hình email, đơn hàng vẫn được tạo thành công nhưng không gửi email

---

## 🐛 Nếu Gặp Lỗi

### Lỗi: Port 3307 đã được sử dụng

```bash
docker-compose down
docker-compose up -d
```

### Lỗi: Column 'provider' not found

```bash
# Chưa reset database, chạy lại:
docker-compose down -v
docker-compose up -d
```

### Lỗi: Port 8080 đã được sử dụng

- Stop app cũ trong IntelliJ
- Hoặc check process: `netstat -ano | findstr :8080`

### Lỗi: Email không gửi được

**Xem hướng dẫn chi tiết**: [EMAIL_SETUP_GUIDE.md](EMAIL_SETUP_GUIDE.md)

**Kiểm tra nhanh**:
- Đã bật 2-Step Verification chưa?
- Đã tạo App Password chưa?
- `MAIL_PASSWORD` phải là App Password (16 ký tự), không phải mật khẩu Gmail

---

## 📋 Tóm Tắt Lệnh

```bash
git pull origin main
docker-compose down -v
docker-compose up -d
./mvnw clean install
# Rồi Run app từ IntelliJ
```

---

**🎉 Xong! Giờ bạn đã có code và database mới nhất!**
