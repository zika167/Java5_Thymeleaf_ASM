# 🧪 HƯỚNG DẪN TEST API VỚI POSTMAN

## 📌 Thông tin Server
- **Base URL**: `http://localhost:8080`
- **Server Status**: ✅ Đang chạy
- **Database**: MariaDB (localhost:3307)

---

## 🔥 DANH SÁCH API CẦN TEST

### 1️⃣ **Lấy tất cả sản phẩm** (Có phân trang)
```
GET http://localhost:8080/api/products
```

**Query Parameters** (Optional):
- `page` - Số trang (mặc định: 0)
- `size` - Số sản phẩm mỗi trang (mặc định: 12)
- `sortBy` - Sắp xếp theo field (mặc định: createdAt)
- `sortDirection` - ASC hoặc DESC (mặc định: DESC)

**Ví dụ**:
```
GET http://localhost:8080/api/products?page=0&size=10&sortBy=price&sortDirection=ASC
```

---

### 2️⃣ **Tìm kiếm và lọc sản phẩm**
```
GET http://localhost:8080/api/products/search
```

**Query Parameters** (Tất cả đều optional):
- `keyword` - Từ khóa tìm kiếm (tìm trong tên và mô tả)
- `categoryId` - Lọc theo category ID
- `brandId` - Lọc theo brand ID
- `minPrice` - Giá tối thiểu
- `maxPrice` - Giá tối đa
- `page` - Số trang (mặc định: 0)
- `size` - Số sản phẩm mỗi trang (mặc định: 12)
- `sortBy` - Sắp xếp theo field (mặc định: createdAt)
- `sortDirection` - ASC hoặc DESC (mặc định: DESC)

**Ví dụ 1**: Tìm sản phẩm có từ "coffee"
```
GET http://localhost:8080/api/products/search?keyword=coffee
```

**Ví dụ 2**: Lọc theo category và khoảng giá
```
GET http://localhost:8080/api/products/search?categoryId=1&minPrice=10&maxPrice=50
```

**Ví dụ 3**: Tìm kiếm tổng hợp
```
GET http://localhost:8080/api/products/search?keyword=arabica&categoryId=1&brandId=2&minPrice=20&maxPrice=100&sortBy=price&sortDirection=ASC
```

---

### 3️⃣ **Lấy chi tiết sản phẩm theo ID**
```
GET http://localhost:8080/api/products/{id}
```

**Ví dụ**:
```
GET http://localhost:8080/api/products/1
GET http://localhost:8080/api/products/5
```

---

### 4️⃣ **Lấy sản phẩm nổi bật**
```
GET http://localhost:8080/api/products/featured
```

**Query Parameters** (Optional):
- `page` - Số trang (mặc định: 0)
- `size` - Số sản phẩm mỗi trang (mặc định: 12)

**Ví dụ**:
```
GET http://localhost:8080/api/products/featured?page=0&size=8
```

---

### 5️⃣ **Lấy sản phẩm mới nhất**
```
GET http://localhost:8080/api/products/latest
```

**Query Parameters** (Optional):
- `page` - Số trang (mặc định: 0)
- `size` - Số sản phẩm mỗi trang (mặc định: 12)

**Ví dụ**:
```
GET http://localhost:8080/api/products/latest?size=10
```

---

### 6️⃣ **Lấy sản phẩm bán chạy**
```
GET http://localhost:8080/api/products/best-selling
```

**Query Parameters** (Optional):
- `page` - Số trang (mặc định: 0)
- `size` - Số sản phẩm mỗi trang (mặc định: 12)

**Ví dụ**:
```
GET http://localhost:8080/api/products/best-selling?size=5
```

---

### 7️⃣ **Lấy danh sách Categories**
```
GET http://localhost:8080/api/products/categories
```

**Response**: Danh sách tất cả categories kèm số lượng sản phẩm

---

### 8️⃣ **Lấy danh sách Brands**
```
GET http://localhost:8080/api/products/brands
```

**Response**: Danh sách tất cả brands kèm số lượng sản phẩm

---

## 📋 CẤU TRÚC RESPONSE MẪU

### ProductListResponse (Danh sách có phân trang)
```json
{
  "products": [
    {
      "id": 1,
      "name": "Premium Arabica Coffee Beans",
      "slug": "premium-arabica-coffee-beans",
      "shortDescription": "High-quality Arabica beans from Colombia",
      "price": 24.99,
      "discountPrice": 19.99,
      "imageUrl": "/assets/img/product/item-1.png",
      "categoryName": "Coffee Beans",
      "brandName": "Lavazza",
      "averageRating": 4.5,
      "totalReviews": 128,
      "isInStock": true,
      "isFeatured": true
    }
  ],
  "currentPage": 0,
  "totalPages": 5,
  "totalItems": 50,
  "pageSize": 12
}
```

### ProductResponse (Chi tiết sản phẩm)
```json
{
  "id": 1,
  "name": "Premium Arabica Coffee Beans",
  "slug": "premium-arabica-coffee-beans",
  "shortDescription": "High-quality Arabica beans from Colombia",
  "price": 24.99,
  "discountPrice": 19.99,
  "imageUrl": "/assets/img/product/item-1.png",
  "categoryName": "Coffee Beans",
  "brandName": "Lavazza",
  "averageRating": 4.5,
  "totalReviews": 128,
  "isInStock": true,
  "isFeatured": true
}
```

### CategoryResponse
```json
{
  "id": 1,
  "name": "Coffee Beans",
  "slug": "coffee-beans",
  "iconUrl": "/assets/img/category/cate-1.1.svg",
  "productCount": 25
}
```

### BrandResponse
```json
{
  "id": 1,
  "name": "Lavazza",
  "slug": "lavazza",
  "logoUrl": "/assets/img/brands/lavazza.png",
  "productCount": 15
}
```

---

## 🎯 HƯỚNG DẪN TEST TRONG POSTMAN

### Bước 1: Tạo Collection mới
1. Mở Postman
2. Click **New** → **Collection**
3. Đặt tên: `Java5 ASM - Product API`

### Bước 2: Thêm các Request
1. Click **Add request** trong Collection
2. Chọn method **GET**
3. Nhập URL từ danh sách trên
4. Click **Send** để test

### Bước 3: Test với Parameters
1. Chọn tab **Params** trong request
2. Thêm các key-value pairs:
   - Key: `keyword`, Value: `coffee`
   - Key: `page`, Value: `0`
   - Key: `size`, Value: `10`
3. Click **Send**

### Bước 4: Kiểm tra Response
- Status code phải là **200 OK**
- Response body phải có cấu trúc JSON đúng
- Kiểm tra dữ liệu có hợp lý không

---

## ✅ CHECKLIST TEST

- [ ] Test API lấy tất cả sản phẩm
- [ ] Test tìm kiếm theo keyword
- [ ] Test lọc theo category
- [ ] Test lọc theo brand
- [ ] Test lọc theo khoảng giá
- [ ] Test tìm kiếm tổng hợp (nhiều filter cùng lúc)
- [ ] Test phân trang (page, size)
- [ ] Test sắp xếp (sortBy, sortDirection)
- [ ] Test lấy chi tiết sản phẩm
- [ ] Test sản phẩm nổi bật
- [ ] Test sản phẩm mới nhất
- [ ] Test sản phẩm bán chạy
- [ ] Test lấy danh sách categories
- [ ] Test lấy danh sách brands

---

## 🐛 XỬ LÝ LỖI

### Lỗi 404 Not Found
- Kiểm tra URL có đúng không
- Kiểm tra server có đang chạy không

### Lỗi 500 Internal Server Error
- Kiểm tra database có đang chạy không
- Xem log trong console để biết lỗi cụ thể

### Không có dữ liệu trả về
- Kiểm tra database có dữ liệu không
- Chạy lại script `mariadb_init/02-data.sql`

---

## 📝 GHI CHÚ

- Tất cả API đều là **GET** method
- Không cần authentication để test (chưa có security cho API này)
- Response luôn ở dạng JSON
- Phân trang bắt đầu từ page = 0
- Mặc định sắp xếp theo ngày tạo (mới nhất trước)
