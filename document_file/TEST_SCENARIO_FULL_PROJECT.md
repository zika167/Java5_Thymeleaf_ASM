# KỊCH BẢN TEST TOÀN BỘ DỰ ÁN GROCERY STORE

## Tổng quan dự án

### Modules có sẵn:
| Module | Controller | Template | API | Status |
|--------|------------|----------|-----|--------|
| Auth | ✅ AuthController | ✅ sign-in, sign-up, reset-password | - | ✅ Đầy đủ |
| User/Profile | ✅ ProfileController | ✅ profile, edit-personal-info | - | ✅ Đầy đủ |
| Product | ✅ ProductRestController | ✅ category, product-detail | ✅ REST API | ✅ Đầy đủ |
| Cart | ✅ CartController | ✅ cart | ✅ REST API | ✅ Đầy đủ |
| Order | ✅ OrderController | ✅ checkout, my-orders, order-detail, shipping | ✅ REST API | ✅ Đầy đủ |
| Wishlist | ✅ WishlistController | ✅ favourite | ✅ REST API | ✅ Đầy đủ |
| Review | ✅ ReviewController | ✅ (fragment) | ✅ REST API | ✅ Đầy đủ |
| Address | ✅ AddressController | ✅ addresses | ✅ REST API | ✅ Đầy đủ |
| Payment | ✅ PaymentController | ✅ payment, payment-success, payment-failure | ✅ VNPay, Momo | ✅ Đầy đủ |
| Admin | ✅ AdminController + APIs | ✅ dashboard, users, orders, products | ✅ REST API | ✅ Đầy đủ |
| Caffeine | ✅ CaffeineController | ✅ cc-doctor | - | ✅ Đầy đủ |
| Email | ✅ EmailService | ✅ email templates | - | ✅ Đầy đủ |

---

## PHẦN 1: TEST GIAO DIỆN (UI)

### 1.1 Trang chủ (Home)
```
URL: http://localhost:8080/
```
- [ ] Hiển thị slideshow/banner
- [ ] Hiển thị danh sách categories
- [ ] Hiển thị sản phẩm nổi bật (Featured Products)
- [ ] Hiển thị sản phẩm mới nhất (Latest Products)
- [ ] Header: Logo, Search, Cart icon, User menu
- [ ] Footer: Thông tin liên hệ, links

### 1.2 Trang đăng ký (Sign Up)
```
URL: http://localhost:8080/sign-up
```
- [ ] Form đăng ký hiển thị đầy đủ fields
- [ ] Validation: username, email, password
- [ ] Hiển thị lỗi khi validation fail
- [ ] Đăng ký thành công → redirect sign-in
- [ ] Link "Đã có tài khoản? Đăng nhập"

### 1.3 Trang đăng nhập (Sign In)
```
URL: http://localhost:8080/sign-in
```
- [ ] Form đăng nhập: username, password
- [ ] Nút "Đăng nhập"
- [ ] Nút "Đăng nhập với Google" (OAuth2)
- [ ] Link "Quên mật khẩu?"
- [ ] Link "Chưa có tài khoản? Đăng ký"
- [ ] Đăng nhập thành công → redirect home

### 1.4 Trang danh mục sản phẩm (Category)
```
URL: http://localhost:8080/category
```
- [ ] Hiển thị danh sách sản phẩm
- [ ] Sidebar: Filter theo category, brand
- [ ] Pagination
- [ ] Sort: Giá, Mới nhất, Tên
- [ ] Nút "Thêm vào giỏ" trên mỗi sản phẩm
- [ ] Nút "Yêu thích" trên mỗi sản phẩm

### 1.5 Trang chi tiết sản phẩm (Product Detail)
```
URL: http://localhost:8080/product/{id}
```
- [ ] Hình ảnh sản phẩm
- [ ] Tên, giá, mô tả
- [ ] Chọn số lượng
- [ ] Nút "Thêm vào giỏ"
- [ ] Nút "Yêu thích"
- [ ] Phần đánh giá (Reviews)
- [ ] Sản phẩm liên quan

### 1.6 Trang giỏ hàng (Cart)
```
URL: http://localhost:8080/cart
```
- [ ] Danh sách sản phẩm trong giỏ
- [ ] Cập nhật số lượng (+/-)
- [ ] Xóa sản phẩm
- [ ] Tổng tiền
- [ ] Nút "Thanh toán"

### 1.7 Trang thanh toán (Checkout)
```
URL: http://localhost:8080/checkout
```
- [ ] Thông tin giao hàng
- [ ] Chọn địa chỉ
- [ ] Chọn phương thức vận chuyển
- [ ] Chọn phương thức thanh toán
- [ ] Tóm tắt đơn hàng
- [ ] Nút "Đặt hàng"

### 1.8 Trang đơn hàng của tôi (My Orders)
```
URL: http://localhost:8080/my-orders
```
- [ ] Danh sách đơn hàng
- [ ] Trạng thái đơn hàng
- [ ] Link xem chi tiết

### 1.9 Trang chi tiết đơn hàng (Order Detail)
```
URL: http://localhost:8080/order-detail/{id}
```
- [ ] Thông tin đơn hàng
- [ ] Danh sách sản phẩm
- [ ] Trạng thái
- [ ] Nút hủy đơn (nếu có thể)

### 1.10 Trang yêu thích (Wishlist)
```
URL: http://localhost:8080/favourite
```
- [ ] Danh sách sản phẩm yêu thích
- [ ] Nút xóa khỏi yêu thích
- [ ] Nút thêm vào giỏ

### 1.11 Trang Profile
```
URL: http://localhost:8080/profile
```
- [ ] Thông tin cá nhân
- [ ] Avatar
- [ ] Nút chỉnh sửa

### 1.12 Trang chỉnh sửa thông tin
```
URL: http://localhost:8080/edit-personal-info
```
- [ ] Form chỉnh sửa: Họ tên, Email, SĐT
- [ ] Đổi mật khẩu
- [ ] Nút lưu

### 1.13 Trang quản lý địa chỉ
```
URL: http://localhost:8080/addresses
```
- [ ] Danh sách địa chỉ
- [ ] Thêm địa chỉ mới
- [ ] Sửa/Xóa địa chỉ
- [ ] Đặt địa chỉ mặc định

### 1.14 Trang CC-Doctor (Caffeine Calculator)
```
URL: http://localhost:8080/cc-doctor
```
- [ ] Form nhập thông tin: tuổi, giới tính, loại đồ uống
- [ ] Tính toán lượng caffeine
- [ ] Hiển thị kết quả và khuyến nghị

### 1.15 Trang Admin Dashboard
```
URL: http://localhost:8080/admin/dashboard
```
- [ ] Thống kê tổng quan
- [ ] Biểu đồ
- [ ] Sidebar menu

### 1.16 Trang Admin Users
```
URL: http://localhost:8080/admin/users
```
- [ ] Danh sách users
- [ ] Khóa/Mở khóa user
- [ ] Đổi role

### 1.17 Trang Admin Orders
```
URL: http://localhost:8080/admin/orders
```
- [ ] Danh sách đơn hàng
- [ ] Cập nhật trạng thái

### 1.18 Trang Admin Products
```
URL: http://localhost:8080/admin/products
```
- [ ] Danh sách sản phẩm
- [ ] Thêm/Sửa/Xóa sản phẩm

---

## PHẦN 2: TEST API (REST)

### 2.1 Cart API
```
Base URL: /api/cart
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| GET | / | Lấy giỏ hàng | [ ] |
| POST | /add | Thêm sản phẩm | [ ] |
| PUT | /update | Cập nhật số lượng | [ ] |
| DELETE | /remove/{id} | Xóa sản phẩm | [ ] |
| DELETE | /clear | Xóa toàn bộ | [ ] |
| GET | /count | Đếm số lượng | [ ] |

### 2.2 Order API
```
Base URL: /api/orders
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| POST | /checkout | Tạo đơn hàng | [ ] |
| GET | / | Lấy đơn hàng của user | [ ] |
| GET | /{id} | Lấy chi tiết đơn hàng | [ ] |
| GET | /number/{orderNumber} | Lấy theo mã đơn | [ ] |
| PUT | /{id}/confirm | Xác nhận đơn (Admin) | [ ] |
| PUT | /{id}/status | Cập nhật trạng thái | [ ] |
| PUT | /{id}/cancel | Hủy đơn hàng | [ ] |

### 2.3 Wishlist API
```
Base URL: /api/wishlist
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| GET | / | Lấy danh sách yêu thích | [ ] |
| POST | /products/{id} | Thêm vào yêu thích | [ ] |
| DELETE | /products/{id} | Xóa khỏi yêu thích | [ ] |
| POST | /products/{id}/toggle | Toggle yêu thích | [ ] |
| GET | /products/{id}/check | Kiểm tra có trong list | [ ] |
| GET | /count | Đếm số lượng | [ ] |
| DELETE | / | Xóa toàn bộ | [ ] |

### 2.4 Review API
```
Base URL: /api/reviews
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| POST | / | Tạo đánh giá | [ ] |
| PUT | /{id} | Cập nhật đánh giá | [ ] |
| DELETE | /{id} | Xóa đánh giá | [ ] |
| GET | /product/{id} | Lấy đánh giá sản phẩm | [ ] |
| GET | /product/{id}/average-rating | Lấy rating TB | [ ] |
| GET | /user | Lấy đánh giá của user | [ ] |

### 2.5 Address API
```
Base URL: /api/addresses
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| GET | / | Lấy danh sách địa chỉ | [ ] |
| GET | /{id} | Lấy chi tiết địa chỉ | [ ] |
| GET | /default | Lấy địa chỉ mặc định | [ ] |
| POST | / | Tạo địa chỉ mới | [ ] |
| PUT | /{id} | Cập nhật địa chỉ | [ ] |
| DELETE | /{id} | Xóa địa chỉ | [ ] |
| PATCH | /{id}/set-default | Đặt làm mặc định | [ ] |

### 2.6 Admin Statistics API
```
Base URL: /api/admin/statistics
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| GET | /dashboard | Thống kê dashboard | [ ] |
| GET | /registrations | Thống kê đăng ký | [ ] |
| GET | /traffic | Thống kê traffic | [ ] |

### 2.7 Admin User API
```
Base URL: /api/admin/users
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| GET | / | Lấy danh sách users | [ ] |
| GET | /{id} | Lấy chi tiết user | [ ] |
| PUT | /{id}/toggle-status | Khóa/Mở khóa | [ ] |
| PUT | /{id}/role | Đổi role | [ ] |

### 2.8 Admin Product API
```
Base URL: /api/admin/products
```
| Method | Endpoint | Mô tả | Test |
|--------|----------|-------|------|
| POST | / | Tạo sản phẩm | [ ] |
| PUT | /{id} | Cập nhật sản phẩm | [ ] |
| DELETE | /{id} | Xóa sản phẩm | [ ] |
| PUT | /{id}/stock | Cập nhật tồn kho | [ ] |
| PUT | /{id}/featured | Toggle nổi bật | [ ] |

---

## PHẦN 3: KỊCH BẢN TEST THEO LUỒNG

### Kịch bản 1: Khách hàng mới mua hàng
```
1. Truy cập trang chủ
2. Xem danh mục sản phẩm
3. Xem chi tiết sản phẩm
4. Thêm vào giỏ hàng (guest)
5. Đăng ký tài khoản
6. Đăng nhập
7. Kiểm tra giỏ hàng vẫn còn
8. Thêm địa chỉ giao hàng
9. Tiến hành checkout
10. Chọn phương thức thanh toán
11. Hoàn tất đơn hàng
12. Kiểm tra email xác nhận
13. Xem đơn hàng trong "My Orders"
```

### Kịch bản 2: Khách hàng cũ mua hàng
```
1. Đăng nhập
2. Xem sản phẩm yêu thích
3. Thêm sản phẩm từ wishlist vào giỏ
4. Checkout với địa chỉ có sẵn
5. Thanh toán VNPay/Momo
6. Xem trạng thái đơn hàng
```

### Kịch bản 3: Đánh giá sản phẩm
```
1. Đăng nhập
2. Vào trang chi tiết sản phẩm đã mua
3. Viết đánh giá (rating + comment)
4. Xem đánh giá hiển thị
5. Sửa đánh giá
6. Xóa đánh giá
```

### Kịch bản 4: Quản lý tài khoản
```
1. Đăng nhập
2. Vào trang Profile
3. Chỉnh sửa thông tin cá nhân
4. Đổi mật khẩu
5. Quản lý địa chỉ (thêm/sửa/xóa)
6. Đặt địa chỉ mặc định
```

### Kịch bản 5: Admin quản lý
```
1. Đăng nhập với tài khoản Admin
2. Vào Dashboard xem thống kê
3. Quản lý Users: khóa/mở khóa
4. Quản lý Orders: cập nhật trạng thái
5. Quản lý Products: thêm/sửa/xóa
```

### Kịch bản 6: Hủy đơn hàng
```
1. Đăng nhập
2. Vào "My Orders"
3. Chọn đơn hàng PENDING
4. Hủy đơn hàng
5. Kiểm tra tồn kho được hoàn lại
```

### Kịch bản 7: OAuth2 Login
```
1. Vào trang đăng nhập
2. Click "Đăng nhập với Google"
3. Xác thực Google
4. Redirect về trang chủ
5. Kiểm tra thông tin user được tạo
```

---

## PHẦN 4: TEST CASES CHI TIẾT

### TC-AUTH-001: Đăng ký thành công
```
Precondition: Chưa có tài khoản
Input:
  - Username: testuser123
  - Email: test@example.com
  - Password: Test@123
  - Confirm Password: Test@123
Expected: 
  - Redirect đến /sign-in
  - Hiển thị message "Đăng ký thành công"
```

### TC-AUTH-002: Đăng ký với email đã tồn tại
```
Precondition: Email đã được đăng ký
Input:
  - Username: newuser
  - Email: existing@example.com
  - Password: Test@123
Expected:
  - Hiển thị lỗi "Email đã được sử dụng"
```

### TC-CART-001: Thêm sản phẩm vào giỏ
```
Precondition: Đã đăng nhập
Input:
  - Product ID: 1
  - Quantity: 2
Expected:
  - Giỏ hàng có 2 sản phẩm
  - Cart count badge cập nhật
```

### TC-ORDER-001: Tạo đơn hàng thành công
```
Precondition: 
  - Đã đăng nhập
  - Giỏ hàng có sản phẩm
  - Có địa chỉ giao hàng
Input:
  - Shipping method: standard
  - Payment method: COD
Expected:
  - Đơn hàng được tạo với status PENDING
  - Giỏ hàng được xóa
  - Email xác nhận được gửi
```

### TC-REVIEW-001: Tạo đánh giá
```
Precondition:
  - Đã đăng nhập
  - Đã mua sản phẩm (verified purchase)
Input:
  - Product ID: 1
  - Rating: 5
  - Comment: "Sản phẩm tốt"
Expected:
  - Đánh giá được tạo
  - Hiển thị badge "Đã mua hàng"
```

---

## PHẦN 5: CHECKLIST TRƯỚC KHI DEMO

### Environment
- [ ] Docker containers running (MariaDB)
- [ ] Application started successfully
- [ ] Database có dữ liệu mẫu

### Accounts
- [ ] Admin account: admin / admin123
- [ ] User account: user1 / user123
- [ ] Google OAuth2 configured

### Data
- [ ] Có ít nhất 10 sản phẩm
- [ ] Có ít nhất 3 categories
- [ ] Có ít nhất 2 brands
- [ ] Có đơn hàng mẫu

### Features to Demo
- [ ] Đăng ký/Đăng nhập
- [ ] Xem sản phẩm
- [ ] Thêm vào giỏ hàng
- [ ] Checkout
- [ ] Quản lý wishlist
- [ ] Đánh giá sản phẩm
- [ ] Admin dashboard

---

## Ngày tạo: 29/01/2026
## Phiên bản: 1.0
