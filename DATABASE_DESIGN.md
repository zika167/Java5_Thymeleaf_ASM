# THIẾT KẾ DATABASE - GROCERY STORE

## 📊 PHÂN TÍCH NGHIỆP VỤ TỪ GIAO DIỆN

### Các chức năng chính:
1. **Quản lý người dùng** (User Management)
   - Đăng ký, đăng nhập, quên mật khẩu
   - Profile: avatar, tên, email, ngày đăng ký
   - Địa chỉ giao hàng (nhiều địa chỉ)
   - Phương thức thanh toán (nhiều thẻ)

2. **Quản lý sản phẩm** (Product Management)
   - Sản phẩm: tên, mô tả, giá, hình ảnh, brand
   - Danh mục sản phẩm (Categories)
   - Đánh giá sản phẩm (Reviews & Ratings)
   - Biến thể sản phẩm (Weight/Size options)
   - Hình ảnh sản phẩm (nhiều ảnh)

3. **Giỏ hàng & Đơn hàng** (Cart & Orders)
   - Giỏ hàng tạm thời
   - Đơn hàng với trạng thái
   - Chi tiết đơn hàng
   - Phí vận chuyển

4. **Danh sách yêu thích** (Wishlist/Favourite)
   - Lưu sản phẩm yêu thích

5. **Slideshow & Banners**
   - Banner quảng cáo trang chủ

---

## 🗄️ DATABASE SCHEMA

### 1. USERS - Quản lý người dùng
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    registered_date DATE DEFAULT CURRENT_DATE,
    is_active BOOLEAN DEFAULT TRUE,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Giải thích:**
- `username`: Tên đăng nhập duy nhất
- `email`: Email duy nhất cho reset password
- `password`: Mật khẩu đã mã hóa (BCrypt)
- `avatar_url`: Link ảnh đại diện
- `registered_date`: Ngày đăng ký (hiển thị trong profile)
- `role`: Phân quyền USER/ADMIN

---

### 2. ADDRESSES - Địa chỉ giao hàng
```sql
CREATE TABLE addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'Vietnam',
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Giải thích:**
- Một user có nhiều địa chỉ
- `is_default`: Địa chỉ mặc định khi checkout
- Hiển thị trong trang shipping

---

### 3. PAYMENT_METHODS - Phương thức thanh toán
```sql
CREATE TABLE payment_methods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    card_type ENUM('VISA', 'MASTERCARD', 'AMEX', 'DISCOVER') NOT NULL,
    card_number VARCHAR(20) NOT NULL,
    card_holder_name VARCHAR(100) NOT NULL,
    expiry_month INT NOT NULL,
    expiry_year INT NOT NULL,
    cvv VARCHAR(4) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Giải thích:**
- Lưu thông tin thẻ thanh toán
- Hiển thị trong trang payment và add-new-card
- `is_default`: Thẻ mặc định

---

### 4. CATEGORIES - Danh mục sản phẩm
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    parent_id BIGINT,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);
```

**Giải thích:**
- Hỗ trợ danh mục cha-con (parent_id)
- `slug`: URL-friendly name (e.g., "coffee-beans")
- `icon_url`: Icon cho menu dropdown
- `display_order`: Thứ tự hiển thị

**Ví dụ dữ liệu:**
```
Departments (parent)
  ├── Coffee (child)
  │   ├── Coffee Beans
  │   └── Ground Coffee
  ├── Electronics
  └── Beauty
```

---

### 5. BRANDS - Thương hiệu
```sql
CREATE TABLE brands (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    logo_url VARCHAR(255),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Giải thích:**
- Thương hiệu sản phẩm (Lavazza, welikecoffee, etc.)
- Hiển thị trong product card

---

### 6. PRODUCTS - Sản phẩm
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    discount_price DECIMAL(10,2),
    category_id BIGINT,
    brand_id BIGINT,
    stock_quantity INT DEFAULT 0,
    sku VARCHAR(50) UNIQUE,
    weight VARCHAR(50),
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    view_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE SET NULL
);
```

**Giải thích:**
- `slug`: URL-friendly (e.g., "coffee-beans-espresso")
- `discount_price`: Giá khuyến mãi (nếu có)
- `stock_quantity`: Số lượng tồn kho
- `sku`: Mã sản phẩm
- `is_featured`: Sản phẩm nổi bật (hiển thị trang chủ)
- `view_count`: Số lượt xem

---

### 7. PRODUCT_IMAGES - Hình ảnh sản phẩm
```sql
CREATE TABLE product_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
```

**Giải thích:**
- Một sản phẩm có nhiều ảnh
- `is_primary`: Ảnh chính (hiển thị trong list)
- `display_order`: Thứ tự hiển thị trong product detail

---

### 8. PRODUCT_VARIANTS - Biến thể sản phẩm
```sql
CREATE TABLE product_variants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    variant_name VARCHAR(100) NOT NULL,
    variant_value VARCHAR(100) NOT NULL,
    price_adjustment DECIMAL(10,2) DEFAULT 0,
    stock_quantity INT DEFAULT 0,
    sku VARCHAR(50) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
```

**Giải thích:**
- Biến thể như: Weight (200g, 300g, 500g)
- `price_adjustment`: Chênh lệch giá so với giá gốc
- Hiển thị trong product detail dropdown

**Ví dụ:**
```
Product: Coffee Beans
Variants:
  - 200g: +$0
  - 300g: +$5
  - 500g: +$10
```

---

### 9. REVIEWS - Đánh giá sản phẩm
```sql
CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(200),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id)
);
```

**Giải thích:**
- Đánh giá 1-5 sao
- `is_verified_purchase`: Đã mua hàng mới được review
- Một user chỉ review 1 lần cho 1 sản phẩm

---

### 10. CARTS - Giỏ hàng
```sql
CREATE TABLE carts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    session_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Giải thích:**
- `user_id`: Cho user đã đăng nhập
- `session_id`: Cho guest user (chưa đăng nhập)

---

### 11. CART_ITEMS - Chi tiết giỏ hàng
```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    variant_id BIGINT,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (variant_id) REFERENCES product_variants(id) ON DELETE SET NULL
);
```

**Giải thích:**
- Lưu giá tại thời điểm thêm vào giỏ
- `variant_id`: Biến thể đã chọn (nếu có)

---

### 12. ORDERS - Đơn hàng
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    
    -- Shipping info
    shipping_address_id BIGINT,
    shipping_method VARCHAR(100),
    shipping_fee DECIMAL(10,2) DEFAULT 0,
    
    -- Payment info
    payment_method_id BIGINT,
    payment_status ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    
    -- Order totals
    subtotal DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    
    -- Order status
    status ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    
    -- Notes
    customer_note TEXT,
    admin_note TEXT,
    
    -- Timestamps
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP NULL,
    shipped_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (shipping_address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE SET NULL
);
```

**Giải thích:**
- `order_number`: Mã đơn hàng duy nhất (ORD-20260117-001)
- Lưu snapshot của địa chỉ và payment method
- Tracking trạng thái đơn hàng
- Timestamps cho từng trạng thái

---

### 13. ORDER_ITEMS - Chi tiết đơn hàng
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    variant_id BIGINT,
    product_name VARCHAR(255) NOT NULL,
    variant_name VARCHAR(100),
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (variant_id) REFERENCES product_variants(id) ON DELETE SET NULL
);
```

**Giải thích:**
- Lưu snapshot tên sản phẩm và giá tại thời điểm đặt hàng
- Tránh bị ảnh hưởng khi sản phẩm thay đổi giá

---

### 14. WISHLISTS - Danh sách yêu thích
```sql
CREATE TABLE wishlists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id)
);
```

**Giải thích:**
- Lưu sản phẩm yêu thích
- Một user không thể thêm trùng sản phẩm

---

### 15. BANNERS - Banner quảng cáo
```sql
CREATE TABLE banners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200),
    image_url VARCHAR(255) NOT NULL,
    image_mobile_url VARCHAR(255),
    link_url VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Giải thích:**
- Banner slideshow trang chủ
- `image_mobile_url`: Ảnh riêng cho mobile
- `start_date`, `end_date`: Thời gian hiển thị

---

## 📈 INDEXES - Tối ưu hiệu suất

```sql
-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Products
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand_id);
CREATE INDEX idx_products_slug ON products(slug);
CREATE INDEX idx_products_featured ON products(is_featured, is_active);

-- Orders
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_number ON orders(order_number);
CREATE INDEX idx_orders_date ON orders(ordered_at);

-- Reviews
CREATE INDEX idx_reviews_product ON reviews(product_id);
CREATE INDEX idx_reviews_user ON reviews(user_id);

-- Wishlists
CREATE INDEX idx_wishlists_user ON wishlists(user_id);

-- Cart Items
CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);
```

---

## 📊 VIEWS - Truy vấn thường dùng

### 1. Product với rating trung bình
```sql
CREATE VIEW v_products_with_rating AS
SELECT 
    p.*,
    COALESCE(AVG(r.rating), 0) as avg_rating,
    COUNT(r.id) as review_count
FROM products p
LEFT JOIN reviews r ON p.id = r.product_id
GROUP BY p.id;
```

### 2. Order summary
```sql
CREATE VIEW v_order_summary AS
SELECT 
    o.id,
    o.order_number,
    o.user_id,
    u.full_name as customer_name,
    o.total_amount,
    o.status,
    o.ordered_at,
    COUNT(oi.id) as item_count
FROM orders o
JOIN users u ON o.user_id = u.id
LEFT JOIN order_items oi ON o.id = oi.order_id
GROUP BY o.id;
```

---

## 🔢 SAMPLE DATA

Xem file: `src/main/resources/data.sql`

---

## 📝 NOTES

1. **Cascade Delete:**
   - User xóa → Xóa addresses, payment_methods, carts, wishlists
   - Product xóa → Xóa images, variants, cart_items
   - Order KHÔNG xóa khi user xóa (RESTRICT)

2. **Price Snapshot:**
   - Lưu giá vào cart_items và order_items
   - Tránh thay đổi giá ảnh hưởng đơn hàng cũ

3. **Session Cart:**
   - Guest user dùng session_id
   - Khi login, merge cart từ session vào user cart

4. **Soft Delete:**
   - Dùng `is_active` thay vì xóa thật
   - Giữ dữ liệu cho báo cáo

---

*Thiết kế database hoàn chỉnh cho Grocery Store E-commerce*
