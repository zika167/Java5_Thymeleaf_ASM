# Contributing Guide

Cảm ơn bạn đã quan tâm đến việc đóng góp cho dự án! 🎉

## 📋 Quy trình đóng góp

### 1. Fork repository
```bash
# Fork trên GitHub, sau đó clone
git clone https://github.com/YOUR_USERNAME/Java5_Thymeleaf_ASM.git
cd Java5_Thymeleaf_ASM
```

### 2. Tạo branch mới
```bash
# Từ branch main
git checkout main
git pull origin main

# Tạo branch feature/bugfix
git checkout -b feature/ten-tinh-nang
# hoặc
git checkout -b fix/ten-bug
```

### 3. Coding standards

#### Java
- Sử dụng Java 21 features khi phù hợp
- Follow Google Java Style Guide
- Đặt tên class: `PascalCase`
- Đặt tên method/variable: `camelCase`
- Đặt tên constant: `UPPER_SNAKE_CASE`

#### Commit messages
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: Tính năng mới
- `fix`: Sửa bug
- `docs`: Thay đổi documentation
- `style`: Format code (không thay đổi logic)
- `refactor`: Refactor code
- `test`: Thêm/sửa tests
- `chore`: Maintenance tasks

**Ví dụ:**
```
feat(cart): add quantity validation

- Add min/max quantity check
- Show error message when invalid

Closes #123
```

### 4. Testing
```bash
# Chạy tests
./mvnw test

# Chạy với coverage
./mvnw test jacoco:report
```

### 5. Tạo Pull Request
- Đảm bảo code compile thành công
- Mô tả rõ thay đổi trong PR description
- Link đến issue liên quan (nếu có)
- Request review từ maintainers

## 🏗️ Project Structure

```
src/main/java/poly/edu/java5_asm/
├── common/           # Shared components
│   ├── config/       # Configuration classes
│   ├── security/     # Security components
│   ├── exception/    # Custom exceptions
│   └── util/         # Utility classes
│
└── module/           # Feature modules
    ├── product/      # Product management
    ├── cart/         # Shopping cart
    ├── order/        # Order processing
    └── ...
```

## 🔧 Development Setup

### Prerequisites
- JDK 21+
- Maven 3.8+
- Docker (for database)
- IDE: IntelliJ IDEA (recommended)

### Quick Start
```bash
# Start database
docker-compose up -d mariadb

# Copy environment file
cp .env.example .env
# Edit .env with your values

# Run application
./mvnw spring-boot:run
```

## 📝 Code Review Checklist

- [ ] Code follows project conventions
- [ ] No hardcoded values (use config/constants)
- [ ] Proper error handling
- [ ] No sensitive data in logs
- [ ] Comments for complex logic
- [ ] Tests added/updated (if applicable)

## 🐛 Reporting Bugs

Khi báo cáo bug, vui lòng cung cấp:
1. Mô tả bug
2. Các bước reproduce
3. Expected vs Actual behavior
4. Screenshots (nếu có)
5. Environment (OS, Browser, Java version)

## 💡 Feature Requests

Mở issue với label `enhancement` và mô tả:
1. Vấn đề cần giải quyết
2. Giải pháp đề xuất
3. Alternatives đã xem xét

## 📞 Contact

- **GitHub Issues:** Cho bugs và features
- **Email:** dev@fatc-grocery.com

---

Happy coding! 🚀
