# 🎨 HƯỚNG DẪN REFACTORING TEMPLATES

## 📊 HIỆN TRẠNG

### Cấu trúc hiện tại:
```
src/main/resources/templates/
├── admin/                      # Admin pages
├── email/                      # Email templates
├── fragments/                  # Shared fragments
├── sign-in.html               # Auth pages
├── sign-up.html
├── profile.html               # User pages
├── cart.html                  # Cart page
├── checkout.html              # Order pages
├── product-detail.html        # Product pages
└── ... (33 files total)
```

---

## 🎯 CẤU TRÚC MỚI

```
src/main/resources/templates/
│
├── index.html                 # Home page (root level)
│
├── shared/                    # Shared across all modules
│   ├── fragments/            # Reusable fragments
│   │   ├── head.html
│   │   ├── header.html
│   │   ├── footer.html
│   │   ├── admin-sidebar.html
│   │   └── reviews.html
│   │
│   ├── email/                # Email templates
│   │   ├── order-confirmation-email.html
│   │   ├── order-status-update-email.html
│   │   └── payment-status-email.html
│   │
│   └── admin/                # Admin pages
│       ├── dashboard.html
│       ├── users.html
│       ├── products.html
│       └── orders.html
│
└── module/                    # Module-specific templates
    │
    ├── auth/                 # Authentication
    │   ├── sign-in.html
    │   ├── sign-up.html
    │   ├── reset-password.html
    │   └── reset-password-emailed.html
    │
    ├── user/                 # User management
    │   ├── profile.html
    │   └── edit-personal-info.html
    │
    ├── product/              # Products
    │   ├── product-detail.html
    │   └── category.html
    │
    ├── cart/                 # Shopping cart
    │   └── cart.html
    │
    ├── order/                # Orders
    │   ├── checkout.html
    │   ├── shipping.html
    │   ├── my-orders.html
    │   ├── order-detail.html
    │   └── order-confirmation.html
    │
    ├── payment/              # Payment
    │   ├── payment.html
    │   ├── add-new-card.html
    │   ├── payment-success.html
    │   └── payment-failure.html
    │
    ├── wishlist/             # Wishlist
    │   └── favourite.html
    │
    ├── address/              # Addresses
    │   └── addresses.html
    │
    └── caffeine/             # CC-Doctor
        └── cc-doctor.html
```

---

## 🚀 THỰC HIỆN

### Bước 1: Chạy script
```bash
chmod +x refactor-templates.sh
./refactor-templates.sh
```

### Bước 2: Update Controller return paths

Bạn cần update tất cả controllers để trả về đúng path mới:

#### AUTH MODULE
```java
// AuthController.java
// Old:
return "sign-in";
return "sign-up";

// New:
return "module/auth/sign-in";
return "module/auth/sign-up";
```

#### USER MODULE
```java
// ProfileController.java
// Old:
return "profile";
return "edit-personal-info";

// New:
return "module/user/profile";
return "module/user/edit-personal-info";
```

#### PRODUCT MODULE
```java
// ProductController.java
// Old:
return "product-detail";
return "category";

// New:
return "module/product/product-detail";
return "module/product/category";
```

#### CART MODULE
```java
// CartController.java
// Old:
return "cart";

// New:
return "module/cart/cart";
```

#### ORDER MODULE
```java
// OrderController.java
// Old:
return "checkout";
return "shipping";
return "my-orders";
return "order-detail";
return "order-confirmation";

// New:
return "module/order/checkout";
return "module/order/shipping";
return "module/order/my-orders";
return "module/order/order-detail";
return "module/order/order-confirmation";
```

#### PAYMENT MODULE
```java
// PaymentController.java
// Old:
return "payment";
return "add-new-card";
return "payment-success";
return "payment-failure";

// New:
return "module/payment/payment";
return "module/payment/add-new-card";
return "module/payment/payment-success";
return "module/payment/payment-failure";
```

#### WISHLIST MODULE
```java
// WishlistController.java (nếu có view)
// Old:
return "favourite";

// New:
return "module/wishlist/favourite";
```

#### ADDRESS MODULE
```java
// AddressController.java
// Old:
return "addresses";

// New:
return "module/address/addresses";
```

#### CAFFEINE MODULE
```java
// CaffeineController.java
// Old:
return "cc-doctor";

// New:
return "module/caffeine/cc-doctor";
```

#### ADMIN MODULE
```java
// AdminController.java
// Old:
return "admin/dashboard";
return "admin/users";

// New:
return "shared/admin/dashboard";
return "shared/admin/users";
```

---

### Bước 3: Update Thymeleaf fragment references

Trong TẤT CẢ các file HTML, update fragment references:

#### Old:
```html
<head th:replace="~{fragments/head :: head('Page Title')}"></head>
<header th:replace="~{fragments/header :: header}"></header>
<footer th:replace="~{fragments/footer :: footer}"></footer>
<div th:replace="~{fragments/reviews :: reviews}"></div>
```

#### New:
```html
<head th:replace="~{shared/fragments/head :: head('Page Title')}"></head>
<header th:replace="~{shared/fragments/header :: header}"></header>
<footer th:replace="~{shared/fragments/footer :: footer}"></footer>
<div th:replace="~{shared/fragments/reviews :: reviews}"></div>
```

---

## 📋 CHECKLIST UPDATE CONTROLLERS

### Controllers cần update:

- [ ] `AuthController.java` - 4 methods
  - [ ] showSignInPage() → "module/auth/sign-in"
  - [ ] showSignUpPage() → "module/auth/sign-up"
  - [ ] showResetPasswordPage() → "module/auth/reset-password"
  - [ ] showResetPasswordEmailedPage() → "module/auth/reset-password-emailed"

- [ ] `ProfileController.java` - 2 methods
  - [ ] showProfile() → "module/user/profile"
  - [ ] showEditProfile() → "module/user/edit-personal-info"

- [ ] `ProductRestController.java` - 2 methods
  - [ ] showProductDetail() → "module/product/product-detail"
  - [ ] showCategory() → "module/product/category"

- [ ] `CartController.java` - 1 method
  - [ ] showCart() → "module/cart/cart"

- [ ] `OrderController.java` - 5 methods
  - [ ] showCheckout() → "module/order/checkout"
  - [ ] showShipping() → "module/order/shipping"
  - [ ] showMyOrders() → "module/order/my-orders"
  - [ ] showOrderDetail() → "module/order/order-detail"
  - [ ] showOrderConfirmation() → "module/order/order-confirmation"

- [ ] `PaymentController.java` - 4 methods
  - [ ] showPayment() → "module/payment/payment"
  - [ ] showAddCard() → "module/payment/add-new-card"
  - [ ] showPaymentSuccess() → "module/payment/payment-success"
  - [ ] showPaymentFailure() → "module/payment/payment-failure"

- [ ] `AddressController.java` - 1 method
  - [ ] showAddresses() → "module/address/addresses"

- [ ] `CaffeineController.java` - 1 method
  - [ ] showCalculator() → "module/caffeine/cc-doctor"

- [ ] `AdminController.java` - 4 methods
  - [ ] showDashboard() → "shared/admin/dashboard"
  - [ ] showUsers() → "shared/admin/users"
  - [ ] showProducts() → "shared/admin/products"
  - [ ] showOrders() → "shared/admin/orders"

- [ ] `HomeController.java` - 1 method
  - [ ] showHome() → "index" (không đổi)

---

## 📝 SCRIPT TỰ ĐỘNG UPDATE CONTROLLERS

Tôi sẽ tạo script để tự động update controllers:

```bash
# Sẽ tạo trong file riêng: update-controller-paths.sh
```

---

## 🧪 TESTING

### Test từng module:

1. **Auth pages:**
   ```
   http://localhost:8080/sign-in
   http://localhost:8080/sign-up
   http://localhost:8080/reset-password
   ```

2. **User pages:**
   ```
   http://localhost:8080/profile
   http://localhost:8080/edit-personal-info
   ```

3. **Product pages:**
   ```
   http://localhost:8080/products/1
   http://localhost:8080/category/coffee
   ```

4. **Cart:**
   ```
   http://localhost:8080/cart
   ```

5. **Order pages:**
   ```
   http://localhost:8080/checkout
   http://localhost:8080/my-orders
   ```

6. **Payment pages:**
   ```
   http://localhost:8080/payment
   http://localhost:8080/payment-success
   ```

7. **Admin pages:**
   ```
   http://localhost:8080/admin/dashboard
   http://localhost:8080/admin/users
   ```

---

## ⚠️ LƯU Ý

### 1. Fragment paths
Tất cả fragments đều ở `shared/fragments/` nên phải update trong TOÀN BỘ HTML files.

### 2. Email templates
Email templates ở `shared/email/` - update trong EmailService nếu cần.

### 3. Admin templates
Admin templates ở `shared/admin/` vì được dùng chung.

### 4. Index.html
Giữ nguyên ở root level vì là home page.

---

## 🔄 ROLLBACK

Nếu có vấn đề:
```bash
git reset --hard HEAD
```

---

## ✅ KẾT QUẢ MONG ĐỢI

Sau khi refactor:
- ✅ Templates tổ chức theo modules
- ✅ Dễ tìm kiếm files
- ✅ Rõ ràng template nào thuộc module nào
- ✅ Shared components tách biệt
- ✅ Tất cả pages hoạt động bình thường

---

**Thời gian ước tính:** 1-2 giờ (bao gồm update controllers và testing)
