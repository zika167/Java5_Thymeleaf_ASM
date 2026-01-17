# 🧹 BÁO CÁO KIỂM TRA VÀ TÁI CẤU TRÚC SOURCE CODE

**Ngày phân tích:** 2026-01-17  
**Dự án:** Java5 ASM - Grocery Store

---

## 📊 TỔNG QUAN HIỆN TẠI

### Cấu trúc thư mục:
```
src/
├── main/
│   ├── java/poly/edu/java5_asm/
│   │   ├── Java5AsmApplication.java
│   │   └── controller/
│   │       └── HomeController.java (1 controller duy nhất)
│   └── resources/
│       ├── application.properties
│       ├── schema.sql (KHÔNG SỬ DỤNG - đã có trong mariadb_init/)
│       ├── static/assets/ (assets chính thức)
│       ├── scss/ (7.9MB - có node_modules)
│       └── templates/
│           ├── *.html (14 files đang dùng)
│           ├── fragments/ (header, footer)
│           └── F8-project-08-main/ (30MB - DUPLICATE!)
└── test/
    └── java/poly/edu/java5_asm/
        └── Java5AsmApplicationTests.java
```

---

## ❌ CÁC FILE/THƯ MỤC KHÔNG SỬ DỤNG (CẦN XÓA)

### 🔴 PRIORITY 1: Xóa ngay (tiết kiệm ~38MB)

#### 1. **Thư mục F8-project-08-main/** (30MB)
**Đường dẫn:** `src/main/resources/templates/F8-project-08-main/`

**Lý do xóa:**
- ✅ Đây là bản sao của template gốc từ F8
- ✅ Tất cả file HTML đã được copy ra ngoài thư mục `templates/`
- ✅ Assets đã được copy vào `static/assets/`
- ✅ Chứa cả `.git/` folder (không cần thiết)
- ✅ Chứa duplicate HTML files (18 files)

**Nội dung:**
```
F8-project-08-main/
├── .git/ (Git repository - không cần)
├── .vscode/ (VSCode settings - không cần)
├── assets/ (đã copy sang static/assets/)
├── scss/ (đã copy sang resources/scss/)
├── templates/ (đã copy sang resources/templates/)
└── *.html (18 files - đã copy ra ngoài)
```

**Lệnh xóa:**
```bash
rm -rf src/main/resources/templates/F8-project-08-main/
```

---

#### 2. **node_modules trong scss/** (7.9MB)
**Đường dẫn:** `src/main/resources/scss/node_modules/`

**Lý do xóa:**
- ✅ Spring Boot không cần node_modules trong resources
- ✅ SCSS đã được compile thành CSS trong `static/assets/css/`
- ✅ Nếu cần compile SCSS, chạy từ thư mục scss/ riêng
- ✅ Không nên commit node_modules vào Git

**Lệnh xóa:**
```bash
rm -rf src/main/resources/scss/node_modules/
```

**Lưu ý:** Nếu cần compile SCSS:
```bash
cd src/main/resources/scss/
npm install  # Cài lại khi cần
npm run build
```

---

#### 3. **schema.sql** (Duplicate)
**Đường dẫn:** `src/main/resources/schema.sql`

**Lý do xóa:**
- ✅ Đã có `mariadb_init/01-schema.sql` (đang được sử dụng)
- ✅ Spring Boot không tự động chạy file này
- ✅ Docker Compose đã load schema từ `mariadb_init/`
- ✅ Duplicate và gây nhầm lẫn

**Lệnh xóa:**
```bash
rm src/main/resources/schema.sql
```

---

### 🟡 PRIORITY 2: Xem xét xóa

#### 4. **File .DS_Store** (macOS metadata)
**Đường dẫn:** Nhiều nơi trong project

**Lý do xóa:**
- ✅ File hệ thống của macOS
- ✅ Không cần thiết cho project
- ✅ Nên thêm vào .gitignore

**Lệnh xóa:**
```bash
find . -name ".DS_Store" -delete
```

**Thêm vào .gitignore:**
```
.DS_Store
```

---

#### 5. **Thư mục scss/** (nếu không compile SCSS)
**Đường dẫn:** `src/main/resources/scss/`

**Lý do xóa (nếu không cần):**
- ✅ CSS đã được compile sẵn trong `static/assets/css/`
- ✅ Nếu không chỉnh sửa style, không cần giữ SCSS source

**Quyết định:**
- ❌ **GIỮ LẠI** nếu bạn sẽ customize CSS
- ✅ **XÓA** nếu chỉ dùng CSS có sẵn

---

## ✅ CÁC FILE ĐANG SỬ DỤNG (GIỮ LẠI)

### Templates đang được sử dụng:
```
✅ index.html              → @GetMapping("/")
✅ sign-in.html            → @GetMapping("/sign-in")
✅ sign-up.html            → @GetMapping("/sign-up")
✅ category.html           → @GetMapping("/category")
✅ product-detail.html     → @GetMapping("/product/{id}")
✅ checkout.html           → @GetMapping("/checkout")
✅ shipping.html           → @GetMapping("/shipping")
✅ payment.html            → @GetMapping("/payment")
✅ profile.html            → @GetMapping("/profile")
✅ edit-personal-info.html → @GetMapping("/edit-personal-info")
✅ favourite.html          → @GetMapping("/favourite")
✅ add-new-card.html       → @GetMapping("/add-new-card")
✅ reset-password.html     → @GetMapping("/reset-password")
✅ reset-password-emailed.html → @GetMapping("/reset-password-emailed")
```

### Fragments:
```
✅ fragments/header.html   → Được include trong tất cả pages
✅ fragments/footer.html   → Được include trong tất cả pages
```

---

## 🏗️ ĐỀ XUẤT TÁI CẤU TRÚC

### 1. **Cấu trúc Java cần bổ sung**

Hiện tại chỉ có 1 controller. Cần tạo thêm:

```
src/main/java/poly/edu/java5_asm/
├── controller/
│   ├── HomeController.java ✅
│   ├── ProductController.java ❌ (cần tạo)
│   ├── CartController.java ❌ (cần tạo)
│   ├── OrderController.java ❌ (cần tạo)
│   ├── UserController.java ❌ (cần tạo)
│   └── AdminController.java ❌ (cần tạo)
├── model/ ❌ (cần tạo)
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   ├── Cart.java
│   └── ...
├── repository/ ❌ (cần tạo)
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── ...
├── service/ ❌ (cần tạo)
│   ├── UserService.java
│   ├── ProductService.java
│   └── ...
├── dto/ ❌ (cần tạo)
│   └── ...
├── config/ ❌ (cần tạo)
│   ├── SecurityConfig.java
│   └── WebConfig.java
└── util/ ❌ (cần tạo)
    └── ...
```

### 2. **Cấu trúc resources đề xuất**

```
src/main/resources/
├── application.properties ✅
├── static/
│   └── assets/ ✅
│       ├── css/
│       ├── js/
│       ├── img/
│       ├── icon/
│       └── fonts/
└── templates/
    ├── index.html ✅
    ├── sign-in.html ✅
    ├── ... (các pages khác)
    ├── fragments/ ✅
    │   ├── header.html
    │   └── footer.html
    └── admin/ ❌ (cần tạo cho admin pages)
        ├── dashboard.html
        ├── products.html
        └── orders.html
```

---

## 📝 HÀNH ĐỘNG CẦN THỰC HIỆN

### Bước 1: Xóa các file không cần thiết
```bash
# 1. Xóa thư mục F8-project-08-main (30MB)
rm -rf src/main/resources/templates/F8-project-08-main/

# 2. Xóa node_modules trong scss (7.9MB)
rm -rf src/main/resources/scss/node_modules/

# 3. Xóa schema.sql duplicate
rm src/main/resources/schema.sql

# 4. Xóa .DS_Store files
find . -name ".DS_Store" -delete

# 5. Xóa .git trong F8 folder (nếu còn)
find src/main/resources -name ".git" -type d -exec rm -rf {} + 2>/dev/null
```

### Bước 2: Cập nhật .gitignore
```bash
# Thêm vào .gitignore
echo "" >> .gitignore
echo "# macOS" >> .gitignore
echo ".DS_Store" >> .gitignore
echo "" >> .gitignore
echo "# Node modules" >> .gitignore
echo "**/node_modules/" >> .gitignore
echo "" >> .gitignore
echo "# SCSS compiled" >> .gitignore
echo "src/main/resources/scss/.sass-cache/" >> .gitignore
```

### Bước 3: Tạo cấu trúc Java chuẩn
```bash
# Tạo các package cần thiết
mkdir -p src/main/java/poly/edu/java5_asm/{model,repository,service,dto,config,util}
mkdir -p src/main/resources/templates/admin
```

---

## 📈 LỢI ÍCH SAU KHI CLEANUP

### Tiết kiệm dung lượng:
- ❌ Trước: ~45MB (chỉ tính resources)
- ✅ Sau: ~7MB
- 💾 **Tiết kiệm: ~38MB (84%)**

### Cải thiện:
- ✅ Project gọn gàng, dễ maintain
- ✅ Build nhanh hơn (ít file hơn)
- ✅ Git repository nhẹ hơn
- ✅ Không còn file duplicate
- ✅ Cấu trúc rõ ràng hơn

---

## ⚠️ LƯU Ý QUAN TRỌNG

### Trước khi xóa:
1. ✅ **Backup project** (commit Git hoặc copy folder)
2. ✅ **Kiểm tra application có chạy được không**
3. ✅ **Test tất cả pages**

### Sau khi xóa:
1. ✅ **Test lại application**
2. ✅ **Kiểm tra CSS/JS vẫn load được**
3. ✅ **Commit changes vào Git**

---

## 🎯 KẾ HOẠCH TIẾP THEO

### Phase 1: Cleanup (Ngay lập tức)
- [ ] Xóa F8-project-08-main/
- [ ] Xóa node_modules/
- [ ] Xóa schema.sql
- [ ] Xóa .DS_Store
- [ ] Cập nhật .gitignore

### Phase 2: Tạo cấu trúc Java (Tuần 1)
- [ ] Tạo Entity classes (model/)
- [ ] Tạo Repository interfaces
- [ ] Tạo Service classes
- [ ] Tạo DTO classes

### Phase 3: Implement features (Tuần 2-4)
- [ ] User authentication
- [ ] Product listing & detail
- [ ] Shopping cart
- [ ] Checkout & payment
- [ ] Order management
- [ ] Admin panel

---

## 📞 HỖ TRỢ

Nếu có vấn đề sau khi cleanup:
1. Restore từ Git: `git checkout .`
2. Hoặc restore từ backup
3. Kiểm tra lại các bước đã thực hiện

---

**Tạo bởi:** Kiro AI  
**Ngày:** 2026-01-17
