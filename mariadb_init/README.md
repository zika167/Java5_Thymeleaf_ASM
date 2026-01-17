# MariaDB Initialization Scripts

Thư mục này chứa các script SQL sẽ được tự động chạy khi khởi động MariaDB container lần đầu tiên.

## 📁 Cấu trúc

```
mariadb_init/
├── 01-schema.sql    # Tạo cấu trúc database (tables, indexes, views)
├── 02-data.sql      # Insert dữ liệu mẫu
└── README.md        # File này
```

## 🚀 Cách hoạt động

1. **Lần đầu chạy Docker Compose:**
   - MariaDB sẽ tự động chạy tất cả file `.sql` trong thư mục này
   - File được chạy theo thứ tự alphabet (01, 02, ...)
   - Database `java5_asm` sẽ được tạo với đầy đủ tables và data

2. **Các lần chạy sau:**
   - Script KHÔNG chạy lại (vì data đã tồn tại trong `mariadb_data/`)
   - Nếu muốn reset database, xóa thư mục `mariadb_data/` và chạy lại

## 🔄 Reset Database

Nếu muốn reset database về trạng thái ban đầu:

```bash
# 1. Stop container
docker-compose down

# 2. Xóa data cũ
rm -rf mariadb_data/

# 3. Start lại (sẽ chạy init scripts)
docker-compose up -d
```

## 📊 Dữ liệu mẫu

### Users (4 users)
- **admin** / password123 (ADMIN)
- **imrankhan** / password123 (USER)
- **johnsmith** / password123 (USER)
- **maryjane** / password123 (USER)

### Products (8 coffee products)
- Coffee Beans - Espresso Arabica and Robusta Beans ($47.00)
- Lavazza Coffee Blends ($53.00 → $49.00)
- Lavazza - Caffè Espresso Black Tin ($99.99)
- Starbucks Pike Place Roast ($32.00 → $28.00)
- Trung Nguyen Creative 3 ($45.00)
- Nescafe Gold Instant Coffee ($24.00)
- Lavazza Qualità Rossa ($38.00 → $35.00)
- Starbucks French Roast ($35.00)

### Categories
- Departments → Coffee → Coffee Beans, Ground Coffee, Instant Coffee
- Grocery
- Beauty

### Brands
- Lavazza
- welikecoffee
- Starbucks
- Nescafe
- Trung Nguyen

### Orders (3 sample orders)
- ORD-20260115-001: Delivered
- ORD-20260116-002: Shipped
- ORD-20260117-003: Processing

## 🔧 Chỉnh sửa

Nếu muốn thay đổi schema hoặc data:

1. Sửa file `01-schema.sql` hoặc `02-data.sql`
2. Reset database (xem hướng dẫn trên)
3. Chạy lại Docker Compose

## ⚠️ Lưu ý

- **KHÔNG** sửa file khi container đang chạy
- **KHÔNG** commit thư mục `mariadb_data/` vào Git (đã có trong .gitignore)
- Password trong file này chỉ dùng cho development, KHÔNG dùng cho production
