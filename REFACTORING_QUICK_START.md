# 🚀 REFACTORING QUICK START

## ⚡ CÁCH NHANH NHẤT - Sử dụng IntelliJ IDEA

### Bước 1: Chuẩn bị (2 phút)
```bash
git add -A
git commit -m "Before refactoring"
git checkout -b refactor-to-modules
```

### Bước 2: Tạo cấu trúc trong IntelliJ (5 phút)

1. Right-click `src/main/java/poly/edu/java5_asm`
2. New → Package
3. Tạo các packages sau:

```
common.config
common.exception
common.security
common.util
common.controller

module.auth.controller
module.auth.dto.request
module.auth.service

module.user.controller
module.user.dto.request
module.user.entity
module.user.repository
module.user.service

module.product.controller
module.product.dto.request
module.product.dto.response
module.product.entity
module.product.repository
module.product.service

module.review.controller
module.review.dto.request
module.review.dto.response
module.review.entity
module.review.repository
module.review.service

module.wishlist.controller
module.wishlist.dto.response
module.wishlist.entity
module.wishlist.exception
module.wishlist.repository
module.wishlist.service

... (tương tự cho các modules khác)
```

### Bước 3: Di chuyển files (30 phút)

**Cách di chuyển:**
1. Chọn file (hoặc nhiều files)
2. Drag & drop vào package mới
3. Hoặc: Right-click → Refactor → Move
4. IntelliJ tự động update imports! ✨

**Thứ tự di chuyển:**

1. **Common files trước:**
   - `config/*` → `common/config/`
   - `exception/*` → `common/exception/`
   - `security/*` → `common/security/`
   - `util/*` → `common/util/`

2. **Modules đơn giản:**
   - Caffeine module
   - Email module
   - Brand module
   - Category module

3. **Modules phức tạp:**
   - Review module
   - Wishlist module
   - Product module
   - Cart module
   - Order module
   - Payment module

### Bước 4: Build & Test (1 giờ)

```bash
# Build
./mvnw clean compile

# Nếu có lỗi import, dùng IntelliJ:
# Ctrl+Alt+O (Windows/Linux)
# Cmd+Option+O (Mac)

# Run tests
./mvnw test

# Run app
./mvnw spring-boot:run
```

### Bước 5: Commit (5 phút)

```bash
git add -A
git commit -m "refactor: Restructure to module-based architecture"
git push origin refactor-to-modules
```

---

## 📋 MAPPING TABLE

| Old Location | New Location |
|-------------|--------------|
| `config/SecurityConfig.java` | `common/config/SecurityConfig.java` |
| `exception/GlobalExceptionHandler.java` | `common/exception/GlobalExceptionHandler.java` |
| `security/CustomUserDetails.java` | `common/security/CustomUserDetails.java` |
| `controller/ReviewController.java` | `module/review/controller/ReviewController.java` |
| `entity/Review.java` | `module/review/entity/Review.java` |
| `repository/ReviewRepository.java` | `module/review/repository/ReviewRepository.java` |
| `service/ReviewService.java` | `module/review/service/ReviewService.java` |
| `dto/request/CreateReviewRequest.java` | `module/review/dto/request/CreateReviewRequest.java` |
| `dto/response/ReviewResponse.java` | `module/review/dto/response/ReviewResponse.java` |

---

## ⚠️ QUAN TRỌNG

### DO:
✅ Commit trước khi bắt đầu  
✅ Sử dụng IntelliJ Refactor → Move  
✅ Test sau mỗi module  
✅ Commit từng bước nhỏ  

### DON'T:
❌ Copy-paste files thủ công  
❌ Edit imports thủ công  
❌ Move tất cả cùng lúc  
❌ Skip testing  

---

## 🎯 EXPECTED RESULT

**Before:**
```
src/main/java/poly/edu/java5_asm/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── security/
└── service/
```

**After:**
```
src/main/java/poly/edu/java5_asm/
├── common/
│   ├── config/
│   ├── exception/
│   ├── security/
│   └── util/
└── module/
    ├── auth/
    ├── user/
    ├── product/
    ├── review/
    ├── wishlist/
    └── ...
```

---

## 🆘 HELP

**Lỗi import?**
→ `Ctrl+Alt+O` trong IntelliJ

**Lỗi package?**
→ IntelliJ tự động fix khi move

**Lỗi build?**
→ Check imports, run `./mvnw clean compile`

**Muốn rollback?**
→ `git reset --hard HEAD`

---

## ⏱️ TIMELINE

- Preparation: 2 phút
- Create structure: 5 phút
- Move files: 30 phút
- Fix errors: 1 giờ
- Testing: 1 giờ
- Commit: 5 phút

**TOTAL: ~2.5 giờ**

---

**Xem `REFACTORING_GUIDE.md` để biết chi tiết đầy đủ!**
