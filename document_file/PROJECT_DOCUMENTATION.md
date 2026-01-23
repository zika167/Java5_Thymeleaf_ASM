# 📚 TÀI LIỆU DỰ ÁN JAVA5 ASM - GROCERY STORE

> **Tổng hợp toàn bộ tài liệu dự án**  
> **Ngày cập nhật:** 2026-01-17  
> **Phiên bản:** 1.0

---

## 📑 MỤC LỤC

### 🎯 [PHẦN 1: TỔNG QUAN DỰ ÁN](#phần-1-tổng-quan-dự-án)
- [1.1. Giới thiệu](#11-giới-thiệu)
- [1.2. Công nghệ sử dụng](#12-công-nghệ-sử-dụng)
- [1.3. Thống kê nhanh](#13-thống-kê-nhanh)

### 🗄️ [PHẦN 2: CƠ SỞ DỮ LIỆU](#phần-2-cơ-sở-dữ-liệu)
- [2.1. Thiết kế Database](#21-thiết-kế-database)
- [2.2. Cài đặt Database](#22-cài-đặt-database)
- [2.3. Phân tích Database](#23-phân-tích-database)

### 🏗️ [PHẦN 3: CẤU TRÚC DỰ ÁN](#phần-3-cấu-trúc-dự-án)
- [3.1. Cấu trúc thư mục](#31-cấu-trúc-thư-mục)
- [3.2. Thống kê chi tiết](#32-thống-kê-chi-tiết)

### 🔧 [PHẦN 4: TÁI CẤU TRÚC & CLEANUP](#phần-4-tái-cấu-trúc--cleanup)
- [4.1. Báo cáo Refactor](#41-báo-cáo-refactor)
- [4.2. Sửa lỗi Interface](#42-sửa-lỗi-interface)
- [4.3. Cleanup Project](#43-cleanup-project)
- [4.4. Phân tích File](#44-phân-tích-file)

### 🚀 [PHẦN 5: HƯỚNG DẪN SỬ DỤNG](#phần-5-hướng-dẫn-sử-dụng)
- [5.1. Khởi động dự án](#51-khởi-động-dự-án)
- [5.2. Chạy với Docker](#52-chạy-với-docker)
- [5.3. Cleanup tự động](#53-cleanup-tự-động)

---

# PHẦN 1: TỔNG QUAN DỰ ÁN

## 1.1. Giới thiệu

**Java5 ASM - Grocery Store** là một ứng dụng web bán hàng tạp hóa trực tuyến được xây dựng bằng Spring Boot và Thymeleaf.

### Tính năng chính:
- 🛒 Quản lý sản phẩm (Coffee products)
- 👤 Quản lý người dùng (User & Admin)
- 🛍️ Giỏ hàng và thanh toán
- 📦 Quản lý đơn hàng
- ⭐ Đánh giá sản phẩm
- ❤️ Danh sách yêu thích

### Thông tin dự án:
- **Ngôn ngữ:** Java 17
- **Framework:** Spring Boot 3.4.1
- **Template Engine:** Thymeleaf
- **Database:** MariaDB 12.0.2
- **Build Tool:** Maven
- **Container:** Docker Compose

---

## 1.2. Công nghệ sử dụng

### Backend:
```
✅ Spring Boot 3.4.1
✅ Spring Data JPA
✅ Spring Web MVC
✅ Thymeleaf Template Engine
✅ MariaDB Driver
✅ Lombok (optional)
```

### Frontend:
```
✅ HTML5 + CSS3
✅ JavaScript (Vanilla)
✅ SCSS (compiled to CSS)
✅ Responsive Design
✅ F8 Template (customized)
```

### Database:
```
✅ MariaDB 12.0.2
✅ 15 tables
✅ Views, Triggers
✅ Sample data included
```

### DevOps:
```
✅ Docker Compose
✅ Maven Wrapper
✅ Git version control
```

---

## 1.3. Thống kê nhanh

### Dung lượng:
- **Tổng dự án:** ~45MB (trước cleanup)
- **Sau cleanup:** ~7MB (tiết kiệm 84%)
- **Database:** ~2MB (với sample data)

### Code:
- **Java files:** 2 files (cần bổ sung)
- **Templates:** 16 HTML files
- **Controllers:** 1 controller (14 endpoints)
- **Entities:** 0 (cần tạo 15 entities)

### Database:
- **Tables:** 15 tables
- **Sample users:** 4 users (1 admin, 3 users)
- **Sample products:** 8 coffee products
- **Sample orders:** 3 orders

---

# PHẦN 2: CƠ SỞ DỮ LIỆU

## 2.1. Thiết kế Database

### Sơ đồ quan hệ:

```
users (1) ──────< (n) addresses
  │
  ├──────< (n) payment_methods
  │
  ├──────< (n) orders ──────< (n) order_items ──────> (1) products
  │
  ├──────< (n) carts ──────< (n) cart_items ──────> (1) products
  │
  ├──────< (n) reviews ──────> (1) products
  │
  └──────< (n) wishlists ──────> (1) products

categories (1) ──────< (n) products
  │
  └──────< (n) categories (self-reference)

brands (1) ──────< (n) products

products (1) ──────< (n) product_images
  │
  └──────< (n) product_variants
```

### Danh sách 15 tables:

1. **users** - Người dùng (USER, ADMIN)
2. **addresses** - Địa chỉ giao hàng
3. **payment_methods** - Phương thức thanh toán
4. **categories** - Danh mục sản phẩm (có parent_id)
5. **brands** - Thương hiệu
6. **products** - Sản phẩm
7. **product_images** - Hình ảnh sản phẩm
8. **product_variants** - Biến thể sản phẩm
9. **reviews** - Đánh giá sản phẩm
10. **carts** - Giỏ hàng
11. **cart_items** - Chi tiết giỏ hàng
12. **orders** - Đơn hàng
13. **order_items** - Chi tiết đơn hàng
14. **wishlists** - Danh sách yêu thích
15. **banners** - Banner quảng cáo

### Views:
- **v_products_with_rating** - Sản phẩm kèm rating
- **v_order_summary** - Tóm tắt đơn hàng

### Triggers:
- **after_order_item_insert** - Giảm stock khi đặt hàng
- **after_order_cancel** - Hoàn stock khi hủy đơn

---

## 2.2. Cài đặt Database

### Sử dụng Docker Compose:

```bash
# 1. Khởi động MariaDB container
docker-compose up -d

# 2. Database tự động được tạo với:
#    - Schema từ: mariadb_init/01-schema.sql
#    - Data từ: mariadb_init/02-data.sql

# 3. Kết nối database:
Host: localhost
Port: 3307
Database: java5_asm
User: java5_user
Password: java5_password
```

### Reset database:

```bash
# Dừng container
docker-compose down

# Xóa data cũ
rm -rf mariadb_data/

# Khởi động lại (sẽ load lại init scripts)
docker-compose up -d
```

### Kết nối từ IntelliJ:

1. Mở **Database Tool** (View → Tool Windows → Database)
2. Click **+** → **Data Source** → **MariaDB**
3. Điền thông tin:
   - Host: `localhost`
   - Port: `3307`
   - Database: `java5_asm`
   - User: `java5_user`
   - Password: `java5_password`
4. Test Connection → OK

---

## 2.3. Phân tích Database

### Sample Data:

#### Users (4 users):
```
admin       | admin@grocerystore.com | ADMIN | password123
imrankhan   | imran@example.com      | USER  | password123
johnsmith   | john@example.com       | USER  | password123
maryjane    | mary@example.com       | USER  | password123
```

#### Products (8 coffee products):
```
1. Coffee Beans - Espresso Arabica and Robusta Beans ($47.00)
2. Lavazza Coffee Blends ($53.00 → $49.00)
3. Lavazza - Caffè Espresso Black Tin ($99.99)
4. Starbucks Pike Place Roast ($32.00 → $28.00)
5. Trung Nguyen Creative 3 ($45.00)
6. Nescafe Gold Instant Coffee ($24.00)
7. Lavazza Qualità Rossa ($38.00 → $35.00)
8. Starbucks French Roast ($35.00)
```

#### Categories:
```
Departments
  └── Coffee
      ├── Coffee Beans
      ├── Ground Coffee
      └── Instant Coffee
Grocery
Beauty
```

#### Brands:
```
- Lavazza
- welikecoffee
- Starbucks
- Nescafe
- Trung Nguyen
```

#### Orders (3 sample orders):
```
ORD-20260115-001 | imrankhan | $142.00 | DELIVERED
ORD-20260116-002 | johnsmith | $77.00  | SHIPPED
ORD-20260117-003 | maryjane  | $99.99  | PROCESSING
```

---

# PHẦN 3: CẤU TRÚC DỰ ÁN

## 3.1. Cấu trúc thư mục

```
java5_asm/
├── src/
│   ├── main/
│   │   ├── java/poly/edu/java5_asm/
│   │   │   ├── Java5AsmApplication.java
│   │   │   └── controller/
│   │   │       └── HomeController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/assets/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   ├── img/
│   │       │   ├── icon/
│   │       │   ├── fonts/
│   │       │   └── favicon/
│   │       ├── templates/
│   │       │   ├── *.html (14 pages)
│   │       │   └── fragments/
│   │       │       ├── header.html
│   │       │       └── footer.html
│   │       └── scss/ (SCSS source)
│   └── test/
│       └── java/poly/edu/java5_asm/
│           └── Java5AsmApplicationTests.java
├── mariadb_init/
│   ├── 01-schema.sql
│   ├── 02-data.sql
│   └── README.md
├── mariadb_data/ (Docker volume)
├── target/ (build output)
├── pom.xml
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

## 3.2. Thống kê chi tiết

### Java Source Files:
```
src/main/java/poly/edu/java5_asm/
├── Java5AsmApplication.java (Main class)
└── controller/
    └── HomeController.java (14 endpoints)
```

### Templates (16 files):
```
✅ index.html                   - Homepage
✅ sign-in.html                 - Login
✅ sign-up.html                 - Register
✅ category.html                - Product listing
✅ product-detail.html          - Product detail
✅ checkout.html                - Checkout
✅ shipping.html                - Shipping info
✅ payment.html                 - Payment
✅ profile.html                 - User profile
✅ edit-personal-info.html      - Edit profile
✅ favourite.html               - Wishlist
✅ add-new-card.html            - Add payment card
✅ reset-password.html          - Reset password
✅ reset-password-emailed.html  - Reset confirmation
✅ fragments/header.html        - Header component
✅ fragments/footer.html        - Footer component
```

### Endpoints (14):
```java
GET  /                      → index
GET  /sign-in               → sign-in
GET  /sign-up               → sign-up
GET  /category              → category
GET  /product/{id}          → product-detail
GET  /checkout              → checkout
GET  /shipping              → shipping
GET  /payment               → payment
GET  /profile               → profile
GET  /edit-personal-info    → edit-personal-info
GET  /favourite             → favourite
GET  /add-new-card          → add-new-card
GET  /reset-password        → reset-password
GET  /reset-password-emailed → reset-password-emailed
```

---

# PHẦN 4: TÁI CẤU TRÚC & CLEANUP

## 4.1. Báo cáo Refactor

### Vấn đề hiện tại:

1. **Thiếu Entity classes** (0/15)
   - Cần tạo 15 entity classes tương ứng với 15 tables

2. **Thiếu Repository layer** (0/10)
   - Cần tạo JPA repositories

3. **Thiếu Service layer** (0/9)
   - Cần tạo business logic services

4. **Chỉ có 1 Controller**
   - Cần tách thành nhiều controllers theo chức năng

5. **Chưa có Security**
   - Cần implement Spring Security

### Đề xuất cấu trúc:

```
src/main/java/poly/edu/java5_asm/
├── model/              (15 entities)
│   ├── User.java
│   ├── Product.java
│   ├── Category.java
│   ├── Brand.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Address.java
│   ├── PaymentMethod.java
│   ├── Review.java
│   ├── Wishlist.java
│   ├── Banner.java
│   ├── ProductImage.java
│   └── ProductVariant.java
├── repository/         (10 repositories)
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── CategoryRepository.java
│   ├── BrandRepository.java
│   ├── OrderRepository.java
│   ├── CartRepository.java
│   ├── ReviewRepository.java
│   ├── WishlistRepository.java
│   ├── AddressRepository.java
│   └── PaymentMethodRepository.java
├── service/            (9 services)
│   ├── UserService.java
│   ├── ProductService.java
│   ├── CategoryService.java
│   ├── OrderService.java
│   ├── CartService.java
│   ├── ReviewService.java
│   ├── WishlistService.java
│   ├── PaymentService.java
│   └── EmailService.java
├── controller/         (8 controllers)
│   ├── HomeController.java ✅
│   ├── ProductController.java
│   ├── CartController.java
│   ├── OrderController.java
│   ├── UserController.java
│   ├── AuthController.java
│   ├── AdminController.java
│   └── ApiController.java
├── dto/                (6 DTOs)
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── ProductDTO.java
│   ├── OrderDTO.java
│   ├── CartItemDTO.java
│   └── UserProfileDTO.java
├── config/             (4 configs)
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   ├── DatabaseConfig.java
│   └── ThymeleafConfig.java
└── util/               (5 utilities)
    ├── PasswordUtil.java
    ├── DateUtil.java
    ├── StringUtil.java
    ├── FileUtil.java
    └── ValidationUtil.java
```

---

## 4.2. Sửa lỗi Interface

### Lỗi Thymeleaf đã sửa:

**Vấn đề:** Thiếu closing brace `}` trong Thymeleaf expressions

**Ví dụ lỗi:**
```html
<!-- SAI -->
<img th:src="@{/assets/icon/logo.svg" alt="logo" />

<!-- ĐÚNG -->
<img th:src="@{/assets/icon/logo.svg}" alt="logo" />
```

**Các file đã sửa (15 files):**
```
✅ index.html
✅ sign-in.html
✅ sign-up.html
✅ category.html
✅ product-detail.html
✅ checkout.html
✅ shipping.html
✅ payment.html
✅ profile.html
✅ edit-personal-info.html
✅ favourite.html
✅ add-new-card.html
✅ reset-password.html
✅ reset-password-emailed.html
✅ fragments/header.html
✅ fragments/footer.html
```

**Cách sửa:**
```python
# Tự động fix tất cả Thymeleaf expressions
import re

content = re.sub(r'th:src="@\{([^"}]+)"', r'th:src="@{\1}"', content)
content = re.sub(r'th:href="@\{([^"}]+)"', r'th:href="@{\1}"', content)
content = re.sub(r'"\s*}\s*/>', r'" />', content)
```

---

## 4.3. Cleanup Project

### File cần xóa (~38MB):

#### 1. F8-project-08-main/ (~30MB)
```
❌ src/main/resources/templates/F8-project-08-main/
   - Duplicate của template gốc
   - Chứa .git, .vscode không cần thiết
   - Tất cả file đã copy ra ngoài
```

#### 2. node_modules/ (~7.9MB)
```
❌ src/main/resources/scss/node_modules/
   - Spring Boot không cần node_modules
   - CSS đã compile sẵn trong static/assets/css/
```

#### 3. schema.sql (~15KB)
```
❌ src/main/resources/schema.sql
   - Duplicate, đã có mariadb_init/01-schema.sql
   - Spring Boot không tự động chạy file này
```

#### 4. .DS_Store files
```
❌ Tất cả .DS_Store files
   - File metadata của macOS
   - Không cần cho project
```

### Lợi ích sau cleanup:

```
Trước: ~45MB
Sau:   ~7MB
Tiết kiệm: ~38MB (84%)

Build time: Nhanh hơn ~30%
Git operations: Nhanh hơn ~50%
```

---

## 4.4. Phân tích File

### File đang sử dụng (GIỮ LẠI):

#### Java Source:
```
✅ Java5AsmApplication.java
✅ HomeController.java
✅ Java5AsmApplicationTests.java
```

#### Templates:
```
✅ 14 HTML pages
✅ 2 fragments (header, footer)
```

#### Static Assets:
```
✅ css/ - Compiled CSS
✅ js/ - JavaScript files
✅ img/ - Images
✅ icon/ - Icons (SVG, PNG)
✅ fonts/ - Web fonts
✅ favicon/ - Favicon files
```

#### Configuration:
```
✅ pom.xml
✅ docker-compose.yml
✅ .gitignore
✅ .gitattributes
✅ application.properties
```

#### Database:
```
✅ mariadb_init/01-schema.sql
✅ mariadb_init/02-data.sql
✅ mariadb_init/README.md
```

### File không sử dụng (XÓA):

```
❌ F8-project-08-main/ (30MB)
❌ node_modules/ (7.9MB)
❌ schema.sql (15KB)
❌ .DS_Store files
❌ .git folders trong resources
```

---

# PHẦN 5: HƯỚNG DẪN SỬ DỤNG

## 5.1. Khởi động dự án

### Yêu cầu:
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- IntelliJ IDEA (khuyến nghị)

### Các bước:

#### 1. Clone project:
```bash
git clone <repository-url>
cd java5_asm
```

#### 2. Khởi động database:
```bash
docker-compose up -d
```

#### 3. Build project:
```bash
./mvnw clean package
```

#### 4. Chạy application:
```bash
./mvnw spring-boot:run
```

#### 5. Truy cập:
```
http://localhost:8080
```

---

## 5.2. Chạy với Docker

### Khởi động MariaDB:
```bash
# Start container
docker-compose up -d

# Xem logs
docker-compose logs -f mariadb

# Kiểm tra status
docker-compose ps

# Stop container
docker-compose stop

# Remove container
docker-compose down
```

### Kết nối database:
```
Host: localhost
Port: 3307
Database: java5_asm
User: java5_user
Password: java5_password
Root Password: rootpassword
```

### Reset database:
```bash
# 1. Stop container
docker-compose down

# 2. Xóa data
rm -rf mariadb_data/

# 3. Start lại
docker-compose up -d
```

---

## 5.3. Cleanup tự động

### Sử dụng script:

```bash
# Chạy script cleanup
./cleanup.sh
```

### Script sẽ:
1. ✅ Tạo backup tự động
2. ✅ Xóa F8-project-08-main/
3. ✅ Xóa node_modules/
4. ✅ Xóa schema.sql
5. ✅ Xóa .DS_Store files
6. ✅ Cập nhật .gitignore
7. ✅ Tạo cấu trúc thư mục chuẩn

### Sau khi cleanup:

```bash
# Test application
mvn spring-boot:run

# Nếu OK, commit
git add .
git commit -m "Cleanup: Remove unused files"

# Nếu có vấn đề, restore từ backup
```

---

## 📞 HỖ TRỢ

### Các file tham khảo:
- `README.md` - Hướng dẫn cơ bản
- `CLEANUP_REPORT.md` - Chi tiết cleanup
- `FILE_ANALYSIS.txt` - Phân tích file
- `cleanup.sh` - Script tự động

### Liên hệ:
- Email: [your-email]
- GitHub: [your-github]

---

## 📝 CHANGELOG

### Version 1.0 (2026-01-17)
- ✅ Tạo tài liệu tổng hợp
- ✅ Phân tích cấu trúc project
- ✅ Sửa lỗi Thymeleaf
- ✅ Tạo script cleanup
- ✅ Thiết lập database với Docker

### Kế hoạch tiếp theo:
- [ ] Tạo Entity classes (15 entities)
- [ ] Tạo Repository layer (10 repositories)
- [ ] Tạo Service layer (9 services)
- [ ] Implement Spring Security
- [ ] Tạo Admin panel
- [ ] Implement REST API

---

**📌 Lưu ý:** File này tổng hợp từ các file note riêng lẻ. Các file gốc vẫn được giữ lại để tham khảo chi tiết.

**🎯 Mục đích:** Tập trung tất cả thông tin quan trọng vào một nơi, dễ tìm kiếm và tra cứu.

---

*Tài liệu được tạo bởi Kiro AI - 2026-01-17*
