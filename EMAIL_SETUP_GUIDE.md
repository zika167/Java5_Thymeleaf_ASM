# 📧 Hướng Dẫn Cấu Hình Email

> **Để gửi email xác nhận đơn hàng cho khách hàng**

---

## 📋 Yêu Cầu

- Tài khoản Gmail
- Đã bật 2-Step Verification

---

## 🔧 Các Bước Cấu Hình

### Bước 1: Bật 2-Step Verification

1. Truy cập [Google Account Security](https://myaccount.google.com/security)
2. Tìm mục **2-Step Verification**
3. Click **Get Started** và làm theo hướng dẫn
4. Hoàn tất việc bật 2-Step Verification

### Bước 2: Tạo App Password

1. Truy cập [App Passwords](https://myaccount.google.com/apppasswords)
2. Đăng nhập lại nếu được yêu cầu
3. Tại mục **Select app**: Chọn **Mail**
4. Tại mục **Select device**: Chọn **Other (Custom name)**
5. Nhập tên: `Java5 ASM` hoặc `Coffee Shop`
6. Click **Generate**
7. Google sẽ hiển thị mật khẩu 16 ký tự (dạng: `xxxx xxxx xxxx xxxx`)
8. **Copy mật khẩu này** (chỉ hiển thị 1 lần)

### Bước 3: Cập Nhật File `.env`

Mở file `.env` trong thư mục project và tìm 3 dòng sau:

```env
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=Fat C Grocery Store <noreply@fatcgrocery.com>
```

Sửa thành:

```env
MAIL_USERNAME=youremail@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx
MAIL_FROM=Fat C Grocery Store <noreply@fatcgrocery.com>
```

**Ví dụ**:
```env
MAIL_USERNAME=nguyenvana@gmail.com
MAIL_PASSWORD=abcd efgh ijkl mnop
MAIL_FROM=Fat C Grocery Store <noreply@fatcgrocery.com>
```

### Bước 4: Restart Application

- Stop app trong IntelliJ (nếu đang chạy)
- Run lại `Java5AsmApplication`

---

## ✅ Kiểm Tra Email Hoạt Động

### 1. Xem logs khi start app

Console phải hiển thị:
```
✅ Loaded .env file successfully
```

**KHÔNG** thấy lỗi:
```
❌ AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
```

### 2. Test gửi email

1. Truy cập: http://localhost:8080
2. Đăng nhập vào hệ thống
3. Thêm sản phẩm vào giỏ hàng
4. Tiến hành đặt hàng
5. Điền thông tin giao hàng và thanh toán
6. Hoàn tất đơn hàng

### 3. Kiểm tra email

- Mở hộp thư email của tài khoản đã đăng nhập
- Phải nhận được email xác nhận đơn hàng
- Email có tiêu đề: **"Order Confirmation - Order #XXXXX"**

---

## 🐛 Xử Lý Lỗi

### Lỗi 1: AuthenticationFailedException

**Triệu chứng**:
```
AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
```

**Nguyên nhân**:
- Chưa bật 2-Step Verification
- App Password sai
- Dùng mật khẩu Gmail thông thường thay vì App Password

**Giải pháp**:
1. Kiểm tra đã bật 2-Step Verification chưa
2. Tạo lại App Password
3. Copy đúng 16 ký tự (có thể giữ nguyên khoảng trắng)
4. Paste vào `MAIL_PASSWORD` trong file `.env`
5. Restart app

### Lỗi 2: Mail server connection failed

**Triệu chứng**:
```
Could not connect to SMTP host: smtp.gmail.com, port: 587
```

**Nguyên nhân**:
- Không có kết nối internet
- Firewall chặn port 587

**Giải pháp**:
1. Kiểm tra kết nối internet
2. Tắt firewall tạm thời để test
3. Thêm exception cho port 587 trong firewall

### Lỗi 3: Email không được gửi nhưng không có lỗi

**Nguyên nhân**:
- `MAIL_USERNAME` sai
- Email bị vào spam

**Giải pháp**:
1. Kiểm tra lại `MAIL_USERNAME` trong `.env`
2. Kiểm tra thư mục Spam/Junk trong email
3. Xem logs trong IntelliJ console để biết chi tiết

---

## 📌 Lưu Ý Quan Trọng

- ✅ `MAIL_PASSWORD` là **App Password** (16 ký tự), KHÔNG phải mật khẩu Gmail
- ✅ Có thể giữ nguyên khoảng trắng trong App Password
- ✅ Mỗi App Password chỉ hiển thị 1 lần, nếu mất phải tạo lại
- ✅ Có thể tạo nhiều App Password cho các ứng dụng khác nhau
- ✅ Nếu không cấu hình email, app vẫn chạy bình thường (chỉ không gửi email)
- ⚠️ **KHÔNG commit file `.env` lên Git** (chứa thông tin nhạy cảm)

---

## 🔐 Bảo Mật

- App Password có quyền truy cập đầy đủ vào Gmail
- Nên tạo App Password riêng cho từng ứng dụng
- Có thể thu hồi App Password bất kỳ lúc nào tại [App Passwords](https://myaccount.google.com/apppasswords)
- Không chia sẻ App Password với người khác

---

## 📚 Tham Khảo

- [Google App Passwords](https://support.google.com/accounts/answer/185833)
- [2-Step Verification](https://support.google.com/accounts/answer/185839)
- [Gmail SMTP Settings](https://support.google.com/mail/answer/7126229)

---

**🎉 Xong! Giờ hệ thống có thể gửi email xác nhận đơn hàng!**
