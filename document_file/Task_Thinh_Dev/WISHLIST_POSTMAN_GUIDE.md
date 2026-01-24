# 📮 Wishlist API - Postman Testing Guide

## 🎯 Hướng Dẫn Test API Qua Postman (Dễ Hiểu)

**Ngôn ngữ:** Tiếng Việt + English  
**Tool:** Postman  
**Thời gian:** 10-15 phút  
**Độ khó:** ⭐ Dễ

---

## 📋 Chuẩn Bị / Preparation

### Bước 1: Cài Đặt Postman

**Download:** https://www.postman.com/downloads/

Hoặc dùng Postman Web: https://web.postman.com/

### Bước 2: Kiểm Tra Server Đang Chạy

```bash
# Mở browser và truy cập:
http://localhost:8080/actuator/health

# Kết quả mong đợi:
{"status":"UP"}
```

✅ Nếu thấy `"status":"UP"` → Server đang chạy, tiếp tục!  
❌ Nếu không load được → Start server trước:
```bash
.\mvnw spring-boot:run
```

---

## 🔐 BƯỚC 1: ĐĂNG NHẬP (Quan Trọng!)

### Tại Sao Phải Đăng Nhập?

Wishlist API cần **authentication** (xác thực). Khi đăng nhập, server sẽ tạo **JWT token** và lưu vào **cookie**. Postman sẽ tự động gửi cookie này trong các request tiếp theo.

### Cách Đăng Nhập

**Method:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Body Type:** `x-www-form-urlencoded`

**Parameters:**
| Key | Value |
|-----|-------|
| username | imrankhan |
| password | password123 |

### Hình Ảnh Minh Họa

```
┌─────────────────────────────────────────┐
│ POST http://localhost:8080/auth/login  │
├─────────────────────────────────────────┤
│ Body: x-www-form-urlencoded            │
│                                         │
│ username: imrankhan                     │
│ password: password123                   │
│                                         │
│ [Send] ←── Click vào đây               │
└─────────────────────────────────────────┘
```

### Các Bước Chi Tiết

1. **Tạo Request Mới:**
   - Click "New" → "HTTP Request"
   - Hoặc nhấn `Ctrl + N`

2. **Chọn Method:**
   - Dropdown bên trái chọn `POST`

3. **Nhập URL:**
   ```
   http://localhost:8080/auth/login
   ```

4. **Chọn Body Tab:**
   - Click tab "Body" (dưới URL)
   - Chọn radio button `x-www-form-urlencoded`

5. **Nhập Parameters:**
   - Row 1: Key = `username`, Value = `imrankhan`
   - Row 2: Key = `password`, Value = `password123`

6. **Click Send:**
   - Nhấn nút "Send" màu xanh

### Kết Quả Mong Đợi

**Status:** `302 Found` (redirect) hoặc `200 OK`

**Cookies Tab:**
Sau khi send, click tab "Cookies" (dưới response), bạn sẽ thấy:
```
JWT_TOKEN = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

✅ **Thành công!** Postman đã lưu cookie, bạn có thể test các API tiếp theo!

---

## 🧪 BƯỚC 2: TEST CÁC API ENDPOINTS

### 📊 Test 1: Đếm Số Sản Phẩm Trong Wishlist

**Mục đích:** Xem có bao nhiêu sản phẩm trong wishlist

**Method:** `GET`  
**URL:** `http://localhost:8080/api/wishlist/count`  
**Authentication:** Không bắt buộc (trả về 0 nếu chưa login)

**Các Bước:**
1. Tạo request mới
2. Chọn method `GET`
3. Nhập URL: `http://localhost:8080/api/wishlist/count`
4. Click "Send"

**Response Mong Đợi:**
```json
{
  "count": 0
}
```

**Giải Thích:**
- `count: 0` → Chưa có sản phẩm nào trong wishlist
- Status `200 OK` → Request thành công

---

### 🔍 Test 2: Kiểm Tra Sản Phẩm Có Trong Wishlist Không

**Mục đích:** Kiểm tra sản phẩm ID = 1 có trong wishlist không

**Method:** `GET`  
**URL:** `http://localhost:8080/api/wishlist/products/1/check`  
**Authentication:** Không bắt buộc

**Các Bước:**
1. Tạo request mới
2. Chọn method `GET`
3. Nhập URL: `http://localhost:8080/api/wishlist/products/1/check`
4. Click "Send"

**Response Mong Đợi:**
```json
{
  "inWishlist": false
}
```

**Giải Thích:**
- `inWishlist: false` → Sản phẩm 1 chưa có trong wishlist
- Thay `1` bằng ID khác để check sản phẩm khác

**Thử Với Sản Phẩm Khác:**
- Product 2: `http://localhost:8080/api/wishlist/products/2/check`
- Product 3: `http://localhost:8080/api/wishlist/products/3/check`

---

### ➕ Test 3: Thêm Sản Phẩm Vào Wishlist

**Mục đích:** Thêm sản phẩm ID = 1 vào wishlist

**Method:** `POST`  
**URL:** `http://localhost:8080/api/wishlist/products/1`  
**Authentication:** ✅ Bắt buộc (phải login trước)

**Các Bước:**
1. Tạo request mới
2. Chọn method `POST`
3. Nhập URL: `http://localhost:8080/api/wishlist/products/1`
4. Click "Send"

**Response Mong Đợi:**
```json
{
  "id": 11,
  "createdAt": "2026-01-25T01:32:14.0670421",
  "productId": 1,
  "productName": "Coffee Beans - Espresso Arabica and Robusta Beans",
  "productPrice": 47,
  "productDiscountPrice": 40,
  "productImageUrl": "/assets/img/product/item-1.png",
  "productStock": 100,
  "productCategoryName": "Coffee",
  "productBrandName": "Lavazza"
}
```

**Status:** `201 Created`

**Giải Thích:**
- `id: 11` → ID của wishlist item (tự động tăng)
- `productId: 1` → ID của sản phẩm
- `productName` → Tên sản phẩm
- `productPrice` → Giá gốc
- `productDiscountPrice` → Giá giảm (nếu có)
- Status `201` → Tạo mới thành công

**Thử Thêm Sản Phẩm Khác:**
- Product 2: `POST http://localhost:8080/api/wishlist/products/2`
- Product 3: `POST http://localhost:8080/api/wishlist/products/3`

**Lỗi Thường Gặp:**

❌ **Error: "Bạn cần đăng nhập"**
```json
{
  "error": "Bạn cần đăng nhập"
}
```
→ **Giải pháp:** Quay lại Bước 1, đăng nhập lại

❌ **Error: "Sản phẩm đã có trong danh sách yêu thích"**
```json
{
  "error": "Sản phẩm đã có trong danh sách yêu thích"
}
```
→ **Giải pháp:** Sản phẩm đã được thêm rồi, thử sản phẩm khác

---

### 📋 Test 4: Xem Danh Sách Wishlist

**Mục đích:** Xem tất cả sản phẩm trong wishlist

**Method:** `GET`  
**URL:** `http://localhost:8080/api/wishlist`  
**Authentication:** ✅ Bắt buộc

**Các Bước:**
1. Tạo request mới
2. Chọn method `GET`
3. Nhập URL: `http://localhost:8080/api/wishlist`
4. Click "Send"

**Response Mong Đợi:**
```json
[
  {
    "id": 11,
    "createdAt": "2026-01-25T01:32:14.0670421",
    "productId": 1,
    "productName": "Coffee Beans - Espresso Arabica and Robusta Beans",
    "productPrice": 47,
    "productDiscountPrice": 40,
    "productImageUrl": "/assets/img/product/item-1.png",
    "productStock": 100,
    "productCategoryName": "Coffee",
    "productBrandName": "Lavazza"
  },
  {
    "id": 12,
    "createdAt": "2026-01-25T01:32:14.1029575",
    "productId": 2,
    "productName": "Lavazza Coffee Blends - Try the Italian Espresso",
    "productPrice": 53,
    "productDiscountPrice": 45,
    "productImageUrl": "/assets/img/product/item-2.png",
    "productStock": 85,
    "productCategoryName": "Coffee",
    "productBrandName": "Lavazza"
  }
]
```

**Status:** `200 OK`

**Giải Thích:**
- Response là **array** (danh sách) các sản phẩm
- Mỗi item có đầy đủ thông tin sản phẩm
- Nếu wishlist trống → `[]` (array rỗng)

---

### 🔄 Test 5: Toggle Sản Phẩm (Thêm/Xóa Tự Động)

**Mục đích:** Nếu sản phẩm chưa có → Thêm vào. Nếu đã có → Xóa đi.

**Method:** `POST`  
**URL:** `http://localhost:8080/api/wishlist/products/3/toggle`  
**Authentication:** ✅ Bắt buộc

**Các Bước:**
1. Tạo request mới
2. Chọn method `POST`
3. Nhập URL: `http://localhost:8080/api/wishlist/products/3/toggle`
4. Click "Send"

**Response Khi Thêm:**
```json
{
  "inWishlist": true,
  "message": "Đã thêm vào yêu thích"
}
```

**Response Khi Xóa:**
```json
{
  "inWishlist": false,
  "message": "Đã xóa khỏi yêu thích"
}
```

**Status:** `200 OK`

**Giải Thích:**
- `inWishlist: true` → Sản phẩm đã được thêm
- `inWishlist: false` → Sản phẩm đã được xóa
- Click "Send" nhiều lần để thấy toggle hoạt động

**Thử Toggle:**
1. Lần 1: Send → `inWishlist: true` (thêm)
2. Lần 2: Send → `inWishlist: false` (xóa)
3. Lần 3: Send → `inWishlist: true` (thêm lại)

---

### ➖ Test 6: Xóa Sản Phẩm Khỏi Wishlist

**Mục đích:** Xóa sản phẩm ID = 1 khỏi wishlist

**Method:** `DELETE`  
**URL:** `http://localhost:8080/api/wishlist/products/1`  
**Authentication:** ✅ Bắt buộc

**Các Bước:**
1. Tạo request mới
2. Chọn method `DELETE`
3. Nhập URL: `http://localhost:8080/api/wishlist/products/1`
4. Click "Send"

**Response Mong Đợi:**
```json
{
  "message": "Đã xóa khỏi danh sách yêu thích"
}
```

**Status:** `200 OK`

**Giải Thích:**
- Sản phẩm 1 đã được xóa khỏi wishlist
- Thay `1` bằng ID khác để xóa sản phẩm khác

**Lỗi Thường Gặp:**

❌ **Error: "Sản phẩm không có trong danh sách yêu thích"**
```json
{
  "error": "Sản phẩm không có trong danh sách yêu thích"
}
```
→ **Giải pháp:** Sản phẩm chưa được thêm vào wishlist, thêm trước rồi xóa

---

### 🗑️ Test 7: Xóa Toàn Bộ Wishlist

**Mục đích:** Xóa tất cả sản phẩm trong wishlist

**Method:** `DELETE`  
**URL:** `http://localhost:8080/api/wishlist`  
**Authentication:** ✅ Bắt buộc

**Các Bước:**
1. Tạo request mới
2. Chọn method `DELETE`
3. Nhập URL: `http://localhost:8080/api/wishlist`
4. Click "Send"

**Response Mong Đợi:**
```json
{
  "message": "Đã xóa toàn bộ danh sách yêu thích"
}
```

**Status:** `200 OK`

**Giải Thích:**
- Tất cả sản phẩm trong wishlist đã bị xóa
- Wishlist bây giờ trống

**Verify (Kiểm Tra):**
Sau khi xóa, test lại:
```
GET http://localhost:8080/api/wishlist/count
→ {"count": 0}
```

---

## 🎯 QUY TRÌNH TEST ĐẦY ĐỦ (Complete Flow)

### Scenario: User Thêm và Quản Lý Wishlist

**Thời gian:** 5 phút  
**Số requests:** 12

```
┌─────────────────────────────────────────────────────┐
│ COMPLETE WISHLIST TEST FLOW                        │
├─────────────────────────────────────────────────────┤
│                                                     │
│ 1. Login                                            │
│    POST /auth/login                                 │
│    → 302 Found (có JWT_TOKEN cookie)               │
│                                                     │
│ 2. Check count ban đầu                             │
│    GET /api/wishlist/count                         │
│    → {"count": 0}                                  │
│                                                     │
│ 3. Check product 1                                 │
│    GET /api/wishlist/products/1/check              │
│    → {"inWishlist": false}                         │
│                                                     │
│ 4. Add product 1                                   │
│    POST /api/wishlist/products/1                   │
│    → 201 Created (product details)                 │
│                                                     │
│ 5. Check count sau khi add                        │
│    GET /api/wishlist/count                         │
│    → {"count": 1}                                  │
│                                                     │
│ 6. Add product 2                                   │
│    POST /api/wishlist/products/2                   │
│    → 201 Created                                   │
│                                                     │
│ 7. Get full wishlist                              │
│    GET /api/wishlist                               │
│    → [product1, product2]                          │
│                                                     │
│ 8. Toggle product 3 (add)                         │
│    POST /api/wishlist/products/3/toggle            │
│    → {"inWishlist": true}                          │
│                                                     │
│ 9. Check count                                     │
│    GET /api/wishlist/count                         │
│    → {"count": 3}                                  │
│                                                     │
│ 10. Remove product 1                              │
│     DELETE /api/wishlist/products/1                │
│     → {"message": "Đã xóa..."}                     │
│                                                     │
│ 11. Clear all                                      │
│     DELETE /api/wishlist                           │
│     → {"message": "Đã xóa toàn bộ..."}            │
│                                                     │
│ 12. Check count cuối                              │
│     GET /api/wishlist/count                        │
│     → {"count": 0}                                 │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 📦 IMPORT POSTMAN COLLECTION

### Cách 1: Tạo Collection Thủ Công

1. **Tạo Collection:**
   - Click "New" → "Collection"
   - Đặt tên: "Wishlist API"

2. **Thêm Requests:**
   - Click "Add request" trong collection
   - Tạo 7 requests theo bảng dưới

### Cách 2: Import JSON (Nhanh Hơn)

Copy JSON này và import vào Postman:

```json
{
  "info": {
    "name": "Wishlist API",
    "description": "API endpoints for Wishlist feature",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "0. Login",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "urlencoded",
          "urlencoded": [
            {
              "key": "username",
              "value": "imrankhan",
              "type": "text"
            },
            {
              "key": "password",
              "value": "password123",
              "type": "text"
            }
          ]
        },
        "url": {
          "raw": "http://localhost:8080/auth/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["auth", "login"]
        }
      }
    },
    {
      "name": "1. Get Wishlist Count",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist/count",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist", "count"]
        }
      }
    },
    {
      "name": "2. Check Product in Wishlist",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist/products/1/check",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist", "products", "1", "check"]
        }
      }
    },
    {
      "name": "3. Add Product to Wishlist",
      "request": {
        "method": "POST",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist/products/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist", "products", "1"]
        }
      }
    },
    {
      "name": "4. Get User Wishlist",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist"]
        }
      }
    },
    {
      "name": "5. Toggle Product",
      "request": {
        "method": "POST",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist/products/3/toggle",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist", "products", "3", "toggle"]
        }
      }
    },
    {
      "name": "6. Remove Product",
      "request": {
        "method": "DELETE",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist/products/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist", "products", "1"]
        }
      }
    },
    {
      "name": "7. Clear Wishlist",
      "request": {
        "method": "DELETE",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/wishlist",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "wishlist"]
        }
      }
    }
  ]
}
```

**Cách Import:**
1. Mở Postman
2. Click "Import" (góc trên bên trái)
3. Chọn tab "Raw text"
4. Paste JSON ở trên
5. Click "Import"

✅ **Done!** Bạn có sẵn 8 requests để test!

---

## 📊 BẢNG TỔNG HỢP API

| # | Name | Method | URL | Auth | Response |
|---|------|--------|-----|------|----------|
| 0 | Login | POST | /auth/login | ❌ | 302 Found |
| 1 | Get Count | GET | /api/wishlist/count | ⚠️ | {"count": 0} |
| 2 | Check Product | GET | /api/wishlist/products/{id}/check | ⚠️ | {"inWishlist": false} |
| 3 | Add Product | POST | /api/wishlist/products/{id} | ✅ | Product details |
| 4 | Get Wishlist | GET | /api/wishlist | ✅ | Array of products |
| 5 | Toggle | POST | /api/wishlist/products/{id}/toggle | ✅ | {"inWishlist": true/false} |
| 6 | Remove | DELETE | /api/wishlist/products/{id} | ✅ | {"message": "..."} |
| 7 | Clear | DELETE | /api/wishlist | ✅ | {"message": "..."} |

**Legend:**
- ✅ Required - Bắt buộc phải login
- ⚠️ Optional - Không bắt buộc (trả về giá trị mặc định nếu chưa login)
- ❌ No auth - Không cần auth

---

## 🐛 TROUBLESHOOTING / XỬ LÝ LỖI

### Lỗi 1: "Could not send request"

**Nguyên nhân:** Server không chạy

**Giải pháp:**
```bash
# Start server
.\mvnw spring-boot:run

# Hoặc check xem server có chạy không
curl http://localhost:8080/actuator/health
```

---

### Lỗi 2: 401 Unauthorized

**Nguyên nhân:** Chưa login hoặc JWT hết hạn

**Giải pháp:**
1. Quay lại Bước 1
2. Login lại
3. Kiểm tra cookie `JWT_TOKEN` có tồn tại không

---

### Lỗi 3: 404 Not Found

**Nguyên nhân:** URL sai

**Giải pháp:**
- Kiểm tra lại URL
- Đảm bảo có `/api/wishlist` trong URL
- Kiểm tra product ID có đúng không

---

### Lỗi 4: 400 Bad Request

**Nguyên nhân:** Dữ liệu không hợp lệ

**Ví dụ:**
- Thêm sản phẩm đã có trong wishlist
- Xóa sản phẩm không có trong wishlist
- Product ID không tồn tại

**Giải pháp:**
- Đọc error message trong response
- Kiểm tra lại dữ liệu gửi đi

---

### Lỗi 5: Cookie Không Được Lưu

**Nguyên nhân:** Postman settings

**Giải pháp:**
1. Mở Settings (⚙️ icon)
2. Tìm "Cookies"
3. Bật "Automatically follow redirects"
4. Bật "Send cookies"
5. Login lại

---

## ✅ CHECKLIST TEST THÀNH CÔNG

Sau khi test xong, check các điều sau:

- [ ] Login thành công (có JWT_TOKEN cookie)
- [ ] Get count trả về số đúng
- [ ] Check product trả về true/false đúng
- [ ] Add product thành công (status 201)
- [ ] Get wishlist trả về danh sách đúng
- [ ] Toggle hoạt động (add → remove → add)
- [ ] Remove product thành công
- [ ] Clear wishlist thành công
- [ ] Tất cả response có status 200/201
- [ ] Không có lỗi trong console

---

## 📝 GHI CHÚ / NOTES

### Test Users / Tài Khoản Test

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | ADMIN |
| imrankhan | password123 | USER |
| johnsmith | password123 | USER |
| maryjane | password123 | USER |

### Product IDs Available / ID Sản Phẩm Có Sẵn

Trong database có sẵn các sản phẩm với ID từ 1-20. Bạn có thể test với bất kỳ ID nào trong khoảng này.

### Response Time / Thời Gian Phản Hồi

- Nhanh: < 50ms ⚡⚡⚡⚡⚡
- Trung bình: 50-100ms ⚡⚡⚡⚡
- Chậm: > 100ms ⚡⚡⚡

Nếu response time > 200ms, có thể server đang bận hoặc database chậm.

---

## 🎓 TIPS & TRICKS

### Tip 1: Sử dụng Environment Variables

Tạo environment trong Postman:
```
baseUrl = http://localhost:8080
```

Sau đó dùng:
```
{{baseUrl}}/api/wishlist/count
```

### Tip 2: Sử dụng Tests Tab

Thêm vào Tests tab để tự động verify:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has count", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('count');
});
```

### Tip 3: Collection Runner

Chạy tất cả requests một lúc:
1. Click vào Collection
2. Click "Run"
3. Chọn tất cả requests
4. Click "Run Wishlist API"

### Tip 4: Save Responses

Click "Save Response" để lưu response làm example, dễ so sánh sau này.

---

**Created:** 2026-01-25  
**Version:** 1.0  
**Language:** Vietnamese + English  
**Tool:** Postman  
**Difficulty:** ⭐ Easy
