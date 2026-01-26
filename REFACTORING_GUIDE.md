# 🔄 HƯỚNG DẪN REFACTORING TOÀN BỘ DỰ ÁN

## ⚠️ CẢNH BÁO QUAN TRỌNG

**Refactoring này sẽ thay đổi 105 files Java!**

Trước khi bắt đầu:
- ✅ Commit tất cả code hiện tại
- ✅ Tạo branch mới: `git checkout -b refactor-to-modules`
- ✅ Backup dự án
- ✅ Thông báo team (nếu có)

---

## 📋 PHƯƠNG ÁN THỰC HIỆN

### OPTION 1: Sử dụng Script Tự Động (KHUYÊN DÙNG)

#### Bước 1: Chuẩn bị
```bash
# Commit code hiện tại
git add -A
git commit -m "Before refactoring to modules"

# Tạo branch mới
git checkout -b refactor-to-modules

# Cho phép chạy script
chmod +x refactor-to-modules.sh
```

#### Bước 2: Chạy script
```bash
./refactor-to-modules.sh
```

Script sẽ tự động:
- ✅ Tạo cấu trúc thư mục mới
- ✅ Di chuyển tất cả files
- ✅ Sử dụng `git mv` để giữ history

#### Bước 3: Fix imports trong IntelliJ IDEA

**Cách 1: Tự động (NHANH NHẤT)**
1. Mở project trong IntelliJ IDEA
2. Click chuột phải vào `src/main/java`
3. Chọn `Refactor` → `Optimize Imports`
4. Chọn `Optimize imports in directory`
5. Click `Run`

**Cách 2: Từng file**
1. Mở file có lỗi import
2. Nhấn `Ctrl+Alt+O` (Windows/Linux) hoặc `Cmd+Option+O` (Mac)
3. IntelliJ sẽ tự động fix imports

#### Bước 4: Build và test
```bash
# Clean và compile
./mvnw clean compile

# Nếu có lỗi, xem log và fix
# Thường là lỗi import

# Run tests
./mvnw test

# Run application
./mvnw spring-boot:run
```

#### Bước 5: Commit
```bash
git add -A
git commit -m "refactor: Restructure to module-based architecture

- Move all files to module structure
- Update package declarations
- Fix all imports
- All tests passing"

git push origin refactor-to-modules
```

---

### OPTION 2: Sử dụng IntelliJ IDEA Refactoring (AN TOÀN NHẤT)

IntelliJ IDEA có công cụ refactoring mạnh mẽ, tự động update imports!

#### Bước 1: Tạo cấu trúc thư mục trong IntelliJ

1. Right-click `src/main/java/poly/edu/java5_asm`
2. New → Package → `common.config`
3. Lặp lại cho tất cả packages:
   - `common.exception`
   - `common.security`
   - `common.util`
   - `module.auth.controller`
   - `module.auth.dto`
   - `module.auth.service`
   - ... (tất cả modules)

#### Bước 2: Di chuyển files bằng IntelliJ

1. **Di chuyển Config files:**
   - Chọn tất cả files trong `config/`
   - Drag & drop vào `common/config/`
   - Hoặc: Right-click → Refactor → Move → chọn `common.config`
   - IntelliJ sẽ tự động update imports!

2. **Di chuyển Exception files:**
   - Chọn tất cả files trong `exception/`
   - Move vào `common/exception/`

3. **Di chuyển Security files:**
   - Chọn tất cả files trong `security/`
   - Move vào `common/security/`

4. **Di chuyển từng module:**
   - Review module: Move tất cả files vào `module/review/`
   - Wishlist module: Move vào `module/wishlist/`
   - ... (lặp lại cho tất cả modules)

**Ưu điểm:**
- ✅ IntelliJ tự động update imports
- ✅ Tự động update package declarations
- ✅ Ít lỗi hơn
- ✅ Có thể undo nếu sai

**Nhược điểm:**
- ⏱️ Mất thời gian hơn (phải move từng module)
- 🖱️ Phải thao tác thủ công nhiều

---

## 📊 CẤU TRÚC MỚI

```
src/main/java/poly/edu/java5_asm/
│
├── common/                          # Shared components
│   ├── config/                     # All @Configuration classes
│   │   ├── SecurityConfig.java
│   │   ├── PasswordEncoderConfig.java
│   │   ├── CacheConfig.java
│   │   └── ...
│   │
│   ├── exception/                  # Global exceptions
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ProductNotFoundException.java
│   │   ├── UserNotFoundException.java
│   │   └── ...
│   │
│   ├── security/                   # Security components
│   │   ├── CustomUserDetails.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtTokenProvider.java
│   │   ├── FormLoginSuccessHandler.java
│   │   ├── OAuth2LoginSuccessHandler.java
│   │   └── ...
│   │
│   ├── util/                       # Utility classes
│   │   └── ...
│   │
│   └── controller/                 # Shared controllers
│       └── HomeController.java
│
└── module/                          # Business modules
    │
    ├── auth/                       # Authentication module
    │   ├── controller/
    │   │   └── AuthController.java
    │   ├── dto/
    │   │   └── request/
    │   │       └── RegisterRequest.java
    │   └── service/
    │       └── AuthService.java
    │
    ├── user/                       # User management module
    │   ├── controller/
    │   │   └── ProfileController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── ProfileUpdateRequest.java
    │   │   └── response/
    │   ├── entity/
    │   │   ├── User.java
    │   │   └── UserActivityLog.java
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   └── UserActivityLogRepository.java
    │   └── service/
    │       └── UserService.java
    │
    ├── product/                    # Product module
    │   ├── controller/
    │   │   └── ProductRestController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── ProductSearchRequest.java
    │   │   └── response/
    │   │       ├── ProductResponse.java
    │   │       └── ProductListResponse.java
    │   ├── entity/
    │   │   └── Product.java
    │   ├── repository/
    │   │   └── ProductRepository.java
    │   └── service/
    │       └── ProductService.java
    │
    ├── category/                   # Category module
    │   ├── dto/
    │   │   └── response/
    │   │       └── CategoryResponse.java
    │   ├── entity/
    │   │   └── Category.java
    │   └── repository/
    │       └── CategoryRepository.java
    │
    ├── brand/                      # Brand module
    │   ├── dto/
    │   │   └── response/
    │   │       └── BrandResponse.java
    │   ├── entity/
    │   │   └── Brand.java
    │   └── repository/
    │       └── BrandRepository.java
    │
    ├── cart/                       # Shopping cart module
    │   ├── controller/
    │   │   └── CartController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   ├── AddToCartRequest.java
    │   │   │   └── UpdateCartItemRequest.java
    │   │   └── response/
    │   │       ├── CartResponse.java
    │   │       └── CartItemResponse.java
    │   ├── entity/
    │   │   ├── Cart.java
    │   │   └── CartItem.java
    │   ├── repository/
    │   │   ├── CartRepository.java
    │   │   └── CartItemRepository.java
    │   └── service/
    │       └── CartService.java
    │
    ├── order/                      # Order module
    │   ├── controller/
    │   │   └── OrderController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── CheckoutRequest.java
    │   │   └── response/
    │   │       ├── OrderResponse.java
    │   │       └── OrderItemResponse.java
    │   ├── entity/
    │   │   ├── Order.java
    │   │   └── OrderItem.java
    │   ├── repository/
    │   │   ├── OrderRepository.java
    │   │   └── OrderItemRepository.java
    │   └── service/
    │       └── OrderService.java
    │
    ├── payment/                    # Payment module
    │   ├── controller/
    │   │   └── PaymentController.java
    │   ├── dto/
    │   │   └── response/
    │   │       ├── VNPayResponse.java
    │   │       └── MomoResponse.java
    │   └── service/
    │       ├── VNPayService.java
    │       ├── VNPayServiceImpl.java
    │       ├── MomoService.java
    │       └── MomoServiceImpl.java
    │
    ├── review/                     # Review module
    │   ├── controller/
    │   │   └── ReviewController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── CreateReviewRequest.java
    │   │   └── response/
    │   │       ├── ReviewResponse.java
    │   │       ├── ReviewListResponse.java
    │   │       └── ProductRatingResponse.java
    │   ├── entity/
    │   │   └── Review.java
    │   ├── repository/
    │   │   └── ReviewRepository.java
    │   └── service/
    │       ├── ReviewService.java
    │       └── ReviewServiceImpl.java
    │
    ├── wishlist/                   # Wishlist module
    │   ├── controller/
    │   │   └── WishlistController.java
    │   ├── dto/
    │   │   └── response/
    │   │       └── WishlistResponse.java
    │   ├── entity/
    │   │   └── Wishlist.java
    │   ├── exception/
    │   │   ├── WishlistException.java
    │   │   ├── WishlistDuplicateException.java
    │   │   └── WishlistNotFoundException.java
    │   ├── repository/
    │   │   └── WishlistRepository.java
    │   └── service/
    │       ├── WishlistService.java
    │       └── WishlistServiceImpl.java
    │
    ├── address/                    # Address module
    │   ├── controller/
    │   │   └── AddressController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── CreateAddressRequest.java
    │   │   └── response/
    │   │       └── AddressResponse.java
    │   ├── entity/
    │   │   └── Address.java
    │   ├── repository/
    │   │   └── AddressRepository.java
    │   └── service/
    │       └── AddressServiceImpl.java
    │
    ├── email/                      # Email module
    │   └── service/
    │       ├── EmailService.java
    │       └── EmailServiceImpl.java
    │
    ├── caffeine/                   # Caffeine calculator module
    │   ├── controller/
    │   │   └── CaffeineController.java
    │   ├── dto/
    │   │   ├── request/
    │   │   │   └── CaffeineCalculationRequest.java
    │   │   └── response/
    │   │       └── CaffeineCalculationResult.java
    │   └── service/
    │       ├── CaffeineService.java
    │       └── CaffeineServiceImpl.java
    │
    └── admin/                      # Admin module
        ├── controller/
        │   ├── AdminController.java
        │   └── AdminStatisticsController.java
        ├── dto/
        │   └── response/
        │       ├── DashboardStatsResponse.java
        │       ├── UserRegistrationStatsResponse.java
        │       └── TrafficStatsResponse.java
        └── service/
            └── AdminStatisticsService.java
```

---

## ✅ CHECKLIST

### Phase 1: Preparation
- [ ] Commit all current code
- [ ] Create new branch: `refactor-to-modules`
- [ ] Backup project
- [ ] Notify team members

### Phase 2: Migration
- [ ] Run migration script OR use IntelliJ refactoring
- [ ] Verify all files moved correctly
- [ ] Check no files left in old directories

### Phase 3: Fix Imports
- [ ] Use IntelliJ "Optimize Imports" on entire project
- [ ] Fix any remaining import errors manually
- [ ] Update package declarations if needed

### Phase 4: Testing
- [ ] Build project: `./mvnw clean compile`
- [ ] Fix compilation errors
- [ ] Run all tests: `./mvnw test`
- [ ] Fix failing tests
- [ ] Run application: `./mvnw spring-boot:run`
- [ ] Test all features manually

### Phase 5: Verification
- [ ] Check all controllers work
- [ ] Check all API endpoints
- [ ] Check database operations
- [ ] Check security/authentication
- [ ] Check file uploads (if any)

### Phase 6: Finalization
- [ ] Update documentation
- [ ] Commit changes
- [ ] Create Pull Request
- [ ] Code review
- [ ] Merge to develop

---

## 🐛 TROUBLESHOOTING

### Lỗi: "Cannot find symbol"
**Nguyên nhân:** Import sai package

**Giải pháp:**
1. Mở file có lỗi
2. Nhấn `Alt+Enter` trên dòng lỗi
3. Chọn "Import class"
4. Hoặc: `Ctrl+Alt+O` để optimize imports

### Lỗi: "Package does not exist"
**Nguyên nhân:** Package declaration chưa được update

**Giải pháp:**
1. Mở file
2. Sửa dòng `package` ở đầu file
3. Ví dụ: `package poly.edu.java5_asm.controller;`
   → `package poly.edu.java5_asm.module.review.controller;`

### Lỗi: Circular dependency
**Nguyên nhân:** Module A import Module B, Module B import Module A

**Giải pháp:**
1. Tạo shared DTO trong `common`
2. Sử dụng Events/Messaging
3. Refactor để tránh circular dependency

### Lỗi: Tests failing
**Nguyên nhân:** Test imports chưa được update

**Giải pháp:**
1. Update imports trong test files
2. Update package paths trong test configs

---

## 📝 NOTES

### Import Examples

**Before:**
```java
import poly.edu.java5_asm.entity.Review;
import poly.edu.java5_asm.repository.ReviewRepository;
import poly.edu.java5_asm.service.ReviewService;
```

**After:**
```java
import poly.edu.java5_asm.module.review.entity.Review;
import poly.edu.java5_asm.module.review.repository.ReviewRepository;
import poly.edu.java5_asm.module.review.service.ReviewService;
```

### Cross-Module Dependencies

Khi module A cần sử dụng entity từ module B:

```java
// In Review module, need User entity
import poly.edu.java5_asm.module.user.entity.User;

// In Order module, need Product entity
import poly.edu.java5_asm.module.product.entity.Product;
```

Điều này là OK! Modules có thể import lẫn nhau.

---

## ⏱️ THỜI GIAN ƯỚC TÍNH

- **Script migration:** 5 phút
- **Fix imports (IntelliJ):** 30 phút
- **Build & fix errors:** 1-2 giờ
- **Testing:** 2-3 giờ
- **Documentation:** 30 phút

**TỔNG: 4-6 giờ** (nếu dùng IntelliJ refactoring)

---

## 🎯 KẾT QUẢ MONG ĐỢI

Sau khi refactoring:
- ✅ Code tổ chức theo modules rõ ràng
- ✅ Dễ tìm kiếm files
- ✅ Dễ maintain
- ✅ Dễ scale (thêm features mới)
- ✅ Tất cả tests pass
- ✅ Application chạy bình thường

---

## 📞 HỖ TRỢ

Nếu gặp vấn đề:
1. Check file `REFACTOR_TO_MODULE_ANALYSIS.md` để hiểu rõ hơn
2. Sử dụng IntelliJ's "Find Usages" để tìm dependencies
3. Commit từng bước nhỏ để dễ rollback
4. Hỏi team nếu cần

---

**Good luck! 🚀**
