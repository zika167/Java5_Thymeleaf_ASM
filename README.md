# Java5 Thymeleaf ASM - Grocery Store

🛒 **Website bán cafe/grocery store** được xây dựng bằng Spring Boot + Thymeleaf

---

## 📚 TÀI LIỆU DỰ ÁN

> **🎯 BẮT ĐẦU TẠI ĐÂY:**
> - **[📖 PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)** - Tài liệu tổng hợp đầy đủ ⭐
> - **[📑 DOCS_INDEX.md](DOCS_INDEX.md)** - Chỉ mục tất cả tài liệu

---

## 📋 Mô tả dự án

Dự án Java 5 Assignment - Website thương mại điện tử bán cafe và grocery với giao diện hiện đại, responsive và đầy đủ tính năng.

## 🚀 Công nghệ sử dụng

### Backend
- **Spring Boot 4.0.1** - Framework chính
- **Spring Data JPA** - ORM và database access
- **Thymeleaf** - Template engine
- **MariaDB** - Database
- **Lombok** - Giảm boilerplate code
- **Maven** - Build tool

### Frontend
- **HTML5 + CSS3** - Markup và styling
- **JavaScript** - Interactive features
- **SCSS** - CSS preprocessor
- **Responsive Design** - Mobile-first approach

## 📁 Cấu trúc dự án

```
java5_asm/
├── src/main/
│   ├── java/poly/edu/java5_asm/
│   │   ├── controller/          # Controllers
│   │   ├── entity/              # JPA Entities
│   │   ├── repository/          # Repositories
│   │   ├── service/             # Business logic
│   │   └── Java5AsmApplication.java
│   │
│   └── resources/
│       ├── static/
│       │   └── assets/
│       │       ├── css/         # Compiled CSS
│       │       ├── js/          # JavaScript files
│       │       ├── img/         # Images
│       │       ├── icon/        # SVG icons
│       │       ├── fonts/       # Web fonts
│       │       └── favicon/     # Favicon files
│       │
│       ├── templates/
│       │   ├── fragments/       # Reusable fragments
│       │   │   ├── head.html
│       │   │   ├── header.html
│       │   │   └── footer.html
│       │   ├── index.html
│       │   ├── sign-in.html
│       │   ├── sign-up.html
│       │   ├── category.html
│       │   ├── product-detail.html
│       │   ├── checkout.html
│       │   ├── profile.html
│       │   └── ...
│       │
│       ├── scss/                # SCSS source files
│       └── application.properties
│
├── docker-compose.yml           # Docker configuration
├── pom.xml                      # Maven configuration
└── README.md
```

## ✨ Tính năng

### 🔐 Authentication
- [x] Đăng ký tài khoản
- [x] Đăng nhập
- [x] Quên mật khẩu
- [x] Đăng xuất

### 🛍️ Shopping
- [x] Xem danh sách sản phẩm
- [x] Xem chi tiết sản phẩm
- [x] Tìm kiếm sản phẩm
- [x] Lọc theo danh mục
- [x] Giỏ hàng
- [x] Thanh toán
- [x] Danh sách yêu thích

### 👤 User Profile
- [x] Xem thông tin cá nhân
- [x] Chỉnh sửa thông tin
- [x] Quản lý địa chỉ giao hàng
- [x] Quản lý phương thức thanh toán
- [x] Lịch sử đơn hàng

### 🎨 UI/UX
- [x] Responsive design (Mobile, Tablet, Desktop)
- [x] Dark mode / Light mode
- [x] Smooth animations
- [x] Interactive dropdowns
- [x] Image slideshow
- [x] Product carousel

## 🔧 Cài đặt và chạy

### Yêu cầu
- Java 21+
- Maven 3.8+
- MariaDB 10.6+
- Docker (optional)

### 1. Clone repository
```bash
git clone https://github.com/zika167/Java5_Thymeleaf_ASM.git
cd Java5_Thymeleaf_ASM
```

### 2. Cấu hình database

#### Option A: Sử dụng Docker
```bash
docker-compose up -d
```

#### Option B: Cài đặt MariaDB thủ công
1. Cài đặt MariaDB
2. Tạo database:
```sql
CREATE DATABASE java5_asm;
```

3. Cập nhật `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/java5_asm
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build và chạy
```bash
# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

### 4. Truy cập ứng dụng
Mở trình duyệt và truy cập: `http://localhost:8080`

## 📸 Screenshots

### Trang chủ
![Home Page](docs/screenshots/home.png)

### Danh mục sản phẩm
![Category](docs/screenshots/category.png)

### Chi tiết sản phẩm
![Product Detail](docs/screenshots/product-detail.png)

### Giỏ hàng
![Checkout](docs/screenshots/checkout.png)

## 🗂️ Database Schema

```sql
-- Users
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    category_id BIGINT,
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Orders
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    total_amount DECIMAL(10,2),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 📝 API Endpoints

### Public
- `GET /` - Trang chủ
- `GET /category` - Danh mục sản phẩm
- `GET /product/{id}` - Chi tiết sản phẩm
- `GET /sign-in` - Đăng nhập
- `GET /sign-up` - Đăng ký

### Protected (Requires Authentication)
- `GET /profile` - Thông tin cá nhân
- `GET /checkout` - Giỏ hàng
- `GET /favourite` - Danh sách yêu thích
- `POST /logout` - Đăng xuất

## 🎨 SCSS Structure

```
scss/
├── abstracts/          # Variables, mixins
├── base/              # Reset, base styles
├── components/        # Buttons, forms, cards
├── layout/            # Header, footer
├── pages/             # Page-specific styles
├── theme/             # Light/dark themes
└── main.scss          # Main entry point
```

## 🔄 Git Workflow

```bash
# Tạo branch mới
git checkout -b feature/ten-tinh-nang

# Commit changes
git add .
git commit -m "Add: mô tả thay đổi"

# Push lên GitHub
git push origin feature/ten-tinh-nang

# Tạo Pull Request trên GitHub
```

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MariaDB Documentation](https://mariadb.org/documentation/)

## 👥 Tác giả

- **Tên:** [Your Name]
- **Email:** [Your Email]
- **GitHub:** [@zika167](https://github.com/zika167)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Frontend template: F8 Project 08
- Icons: Custom SVG icons
- Fonts: Gordita font family

---

⭐ **Nếu bạn thấy project hữu ích, hãy cho một star nhé!** ⭐
