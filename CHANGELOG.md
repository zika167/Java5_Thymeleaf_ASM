# Changelog - 31/01/2026

## Tổng quan
Cập nhật dự án với các tính năng mới: API Documentation (Swagger), Audit Logging, cải thiện Performance và Security.

---

## 1. API Documentation (Swagger UI)

### Mô tả
Thêm Swagger UI để xem và test API trực tiếp trên browser.

### Truy cập
- **URL**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs

### Files thêm mới
- `src/main/java/poly/edu/java5_asm/common/config/OpenApiConfig.java`

### Dependencies thêm vào pom.xml
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.14</version>
</dependency>
```

### Cách sử dụng
1. Chạy app: `.\mvnw spring-boot:run`
2. Mở browser: http://localhost:8080/swagger-ui.html
3. Xem danh sách API và test trực tiếp

---

## 2. Audit Logging System

### Mô tả
Hệ thống ghi log các thao tác quan trọng (đặt hàng, thanh toán, admin actions) để truy vết khi có sự cố.

### Files thêm mới
```
src/main/java/poly/edu/java5_asm/common/audit/
├── AuditLog.java           # Entity lưu audit log
├── AuditAction.java        # Enum các loại action
├── AuditLogRepository.java # Repository với search queries
├── AuditService.java       # Service xử lý logging (async)
├── AuditAspect.java        # AOP Aspect tự động log
├── Auditable.java          # Annotation đánh dấu method cần log
└── AuditLogController.java # Admin API xem audit logs
```

### Dependencies thêm vào pom.xml
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>7.0.3</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.24</version>
</dependency>
```

### Cách sử dụng Annotation
```java
@Auditable(action = AuditAction.ORDER_CREATE, entityType = "Order")
public Order createOrder(...) { ... }
```

### Admin API Endpoints
- `GET /api/admin/audit-logs` - Lấy danh sách audit logs (phân trang)
- `GET /api/admin/audit-logs/search` - Tìm kiếm audit logs
- `GET /api/admin/audit-logs/user/{userId}` - Logs theo user
- `GET /api/admin/audit-logs/entity/{entityType}/{entityId}` - Logs theo entity

---

## 3. Database Indexes (Performance)

### Mô tả
Thêm database indexes để tối ưu query performance.

### Files đã sửa
- `Cart.java` - Index: user_id
- `CartItem.java` - Index: cart_id, product_id
- `Order.java` - Index: user_id, status, created_at
- `OrderItem.java` - Index: order_id, product_id
- `Product.java` - Index: category_id, brand_id, name
- `Review.java` - Index: product_id, user_id
- `Wishlist.java` - Index: user_id, product_id
- `Address.java` - Index: user_id
- `Category.java` - Index: name
- `Brand.java` - Index: name

---

## 4. DTO Validation

### Mô tả
Thêm validation annotations cho request DTOs.

### Files đã sửa
- `AddToCartRequest.java` - @NotNull, @Min
- `UpdateCartItemRequest.java` - @NotNull, @Min
- `CheckoutRequest.java` - @NotNull, @NotBlank

### Controllers đã thêm @Valid
- `CartController.java`
- `OrderController.java`

---

## 5. EntityGraph (N+1 Query Fix)

### Mô tả
Thêm EntityGraph để tránh N+1 query problem.

### Files đã sửa
- `CartItemRepository.java` - EntityGraph cho product
- `OrderItemRepository.java` - EntityGraph cho product

---

## 6. Security Improvements

### Cookie Security
- Thêm `SameSite=Lax` cho JWT cookie trong `FormLoginSuccessHandler.java`

### SecurityConfig
- Cho phép truy cập Swagger endpoints không cần auth:
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/swagger-ui.html`

---

## 7. Project Configuration Files

### Files thêm mới (Root)
- `SECURITY.md` - Security policy
- `LICENSE` - MIT License  
- `CONTRIBUTING.md` - Contribution guide
- `.editorconfig` - Editor settings
- `.env.example` - Environment variables template

### Deployment Files
- `app.yaml` - Google App Engine config
- `cloudbuild.yaml` - Google Cloud Build config
- `render.yaml` - Render.com deployment config
- `.github/workflows/ci.yml` - GitHub Actions CI/CD

### Documentation
- `document_file/DEPLOYMENT_GUIDE.md` - Hướng dẫn deploy

### Config
- `src/main/resources/application-prod.yml` - Production config
- `src/main/java/poly/edu/java5_asm/common/config/JpaAuditingConfig.java` - JPA Auditing config

---

## 8. Cấu hình application.yml

### Thêm mới
```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
    tags-sorter: alpha
```

---

## Hướng dẫn chạy

### Setup lần đầu
```bash
# 1. Copy file environment
copy .env.example .env

# 2. Mở .env và điền thông tin:
#    - DB_USERNAME, DB_PASSWORD (database)
#    - GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET (OAuth2)
#    - JWT_SECRET (random 32+ ký tự)
#    - MAIL_USERNAME, MAIL_PASSWORD (nếu cần email)
```

### Development
```bash
# Compile
.\mvnw compile

# Run tests
.\mvnw test

# Run app
.\mvnw spring-boot:run
```

### Kiểm tra
1. App chạy: http://localhost:8080
2. Swagger UI: http://localhost:8080/swagger-ui.html
3. Health check: http://localhost:8080/actuator/health

---

## Lưu ý
- IDE có thể hiển thị Lombok errors (false positives) - bỏ qua, build vẫn OK
- Audit logs được lưu async để không block request
- Production nên disable Swagger: `springdoc.swagger-ui.enabled=false`

---

## 9. Common Module Structure (Refactored)

### Entity Base
```
src/main/java/poly/edu/java5_asm/common/entity/
└── BaseEntity.java         # Base entity với id, createdAt, updatedAt
```

### Constants
```
src/main/java/poly/edu/java5_asm/common/constant/
└── (các constant classes)
```

### DTOs
```
src/main/java/poly/edu/java5_asm/common/dto/
└── (common DTOs)
```

### Custom Exceptions
```
src/main/java/poly/edu/java5_asm/common/exception/
├── AddressException.java
├── AddressNotFoundException.java
├── AuthException.java
├── BrandNotFoundException.java
├── CartException.java
├── CategoryNotFoundException.java
├── OrderException.java
├── OrderNotFoundException.java
├── PaymentException.java
└── ReviewException.java
```

---

## 10. Service Implementations

### Files thêm mới
- `src/main/java/poly/edu/java5_asm/module/address/service/AddressService.java`
- `src/main/java/poly/edu/java5_asm/module/admin/service/AdminStatisticsServiceImpl.java`
- `src/main/java/poly/edu/java5_asm/module/auth/service/AuthServiceImpl.java`
- `src/main/java/poly/edu/java5_asm/module/brand/service/` (Brand service)
- `src/main/java/poly/edu/java5_asm/module/cart/service/CartServiceImpl.java`
- `src/main/java/poly/edu/java5_asm/module/category/service/` (Category service)
- `src/main/java/poly/edu/java5_asm/module/order/service/OrderServiceImpl.java`
- `src/main/java/poly/edu/java5_asm/module/product/service/ProductServiceImpl.java`
- `src/main/java/poly/edu/java5_asm/module/user/service/UserServiceImpl.java`

---

## 11. Template Changes

### Files đã sửa
- `src/main/resources/templates/shared/fragments/header/user-actions.html`
