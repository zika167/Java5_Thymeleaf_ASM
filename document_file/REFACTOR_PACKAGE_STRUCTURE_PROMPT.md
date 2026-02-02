# PROMPT: Chuyển đổi Package Structure sang Module-Based

## Mô tả
Prompt này dùng để chuyển đổi cấu trúc package của dự án Java Spring Boot từ flat structure sang module-based structure.

---

## Prompt để sử dụng

```
Tôi muốn chuyển đổi cấu trúc package của dự án Java Spring Boot từ flat structure sang module-based structure.

**Yêu cầu:**

1. Mỗi file Java trong folder `module/<tên_module>/<loại>/<TênFile>.java` phải có package declaration tương ứng:
   `package poly.edu.java5_asm.module.<tên_module>.<loại>;`

2. Cập nhật tất cả imports trong toàn bộ dự án để phù hợp với cấu trúc mới:
   - Entity imports: `poly.edu.java5_asm.module.<module>.entity.*`
   - Repository imports: `poly.edu.java5_asm.module.<module>.repository.*`
   - Service imports: `poly.edu.java5_asm.module.<module>.service.*`
   - Controller imports: `poly.edu.java5_asm.module.<module>.controller.*`
   - DTO imports: `poly.edu.java5_asm.module.<module>.dto.request.*` hoặc `poly.edu.java5_asm.module.<module>.dto.response.*`
   - Exception imports: `poly.edu.java5_asm.common.exception.*`
   - Security imports: `poly.edu.java5_asm.common.security.*`
   - Config imports: `poly.edu.java5_asm.common.config.*`

3. Thay thế các wildcard imports cũ như:
   - `poly.edu.java5_asm.entity.*` → imports cụ thể từ các module
   - `poly.edu.java5_asm.repository.*` → imports cụ thể từ các module
   - `poly.edu.java5_asm.dto.*` → imports cụ thể từ các module
   - `poly.edu.java5_asm.service.*` → imports cụ thể từ các module
   - `poly.edu.java5_asm.exception.*` → `poly.edu.java5_asm.common.exception.*`

4. Cập nhật các thư viện nếu cần (ví dụ: JWT 0.12.x có API mới - dùng parser() thay vì parserBuilder())

5. Sau khi hoàn thành, chạy `.\mvnw.cmd compile` để verify không còn lỗi

**Lưu ý quan trọng:**
- KHÔNG sửa file .env (chứa credentials thật)
- Build command: `.\mvnw.cmd compile` (Windows)
- Test command: `.\mvnw.cmd test`
```

---

## Cấu trúc module mong muốn

```
src/main/java/poly/edu/java5_asm/
├── common/
│   ├── config/          # Cấu hình chung (Security, Cache, Web...)
│   ├── controller/      # Controller chung (HomeController)
│   ├── exception/       # Custom exceptions
│   ├── security/        # Security classes (JWT, UserDetails...)
│   └── util/            # Utility classes
└── module/
    ├── user/
    │   ├── entity/      # User, UserActivityLog
    │   ├── repository/  # UserRepository, UserActivityLogRepository
    │   ├── service/     # UserService
    │   ├── controller/  # ProfileController
    │   └── dto/
    │       └── request/ # ProfileUpdateRequest
    ├── product/
    │   ├── entity/
    │   ├── repository/
    │   ├── service/
    │   ├── controller/
    │   └── dto/
    ├── order/
    ├── cart/
    ├── admin/
    ├── address/
    ├── auth/
    ├── brand/
    ├── category/
    ├── caffeine/
    ├── email/
    ├── payment/
    ├── review/
    └── wishlist/
```

---

## Mapping imports cũ → mới (Tham khảo)

| Import cũ | Import mới |
|-----------|------------|
| `poly.edu.java5_asm.entity.User` | `poly.edu.java5_asm.module.user.entity.User` |
| `poly.edu.java5_asm.entity.Product` | `poly.edu.java5_asm.module.product.entity.Product` |
| `poly.edu.java5_asm.entity.Order` | `poly.edu.java5_asm.module.order.entity.Order` |
| `poly.edu.java5_asm.entity.Cart` | `poly.edu.java5_asm.module.cart.entity.Cart` |
| `poly.edu.java5_asm.repository.UserRepository` | `poly.edu.java5_asm.module.user.repository.UserRepository` |
| `poly.edu.java5_asm.repository.ProductRepository` | `poly.edu.java5_asm.module.product.repository.ProductRepository` |
| `poly.edu.java5_asm.dto.ProfileUpdateRequest` | `poly.edu.java5_asm.module.user.dto.request.ProfileUpdateRequest` |
| `poly.edu.java5_asm.dto.request.AdminProductRequest` | `poly.edu.java5_asm.module.admin.dto.request.AdminProductRequest` |
| `poly.edu.java5_asm.dto.response.DashboardStatsResponse` | `poly.edu.java5_asm.module.admin.dto.response.DashboardStatsResponse` |
| `poly.edu.java5_asm.exception.*` | `poly.edu.java5_asm.common.exception.*` |
| `poly.edu.java5_asm.service.impl.AddressServiceImpl` | `poly.edu.java5_asm.module.address.service.AddressServiceImpl` |

---

## JWT 0.12.x API Changes (Nếu cần)

```java
// CŨ (0.11.x)
Jwts.builder()
    .setSubject(username)
    .setIssuedAt(now)
    .setExpiration(expiryDate)
    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
    .compact();

Jwts.parserBuilder()
    .setSigningKey(getSigningKey())
    .build()
    .parseClaimsJws(token)
    .getBody();

// MỚI (0.12.x)
Jwts.builder()
    .subject(username)
    .issuedAt(now)
    .expiration(expiryDate)
    .signWith(getSigningKey())
    .compact();

Jwts.parser()
    .verifyWith(getSigningKey())
    .build()
    .parseSignedClaims(token)
    .getPayload();

// Key type cũng đổi từ Key → SecretKey
private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
}
```

---

## Ngày tạo
- **Ngày:** 29/01/2026
- **Mục đích:** Lưu lại prompt để tái sử dụng khi cần refactor package structure
