# 📊 ADMIN STATISTICS API - HƯỚNG DẪN

## 🔐 Yêu cầu Authentication

**QUAN TRỌNG**: Tất cả API Admin Statistics yêu cầu:
- ✅ Đăng nhập với tài khoản **ADMIN**
- ✅ Role: `ADMIN`

**Tài khoản Admin mặc định**:
- Username: `admin`
- Password: `password123`
- Email: `admin@grocerystore.com`

---

## 📋 DANH SÁCH API

### 1️⃣ **Dashboard - Thống kê tổng quan**
```
GET /api/admin/statistics/dashboard
```

**Mô tả**: Lấy tất cả thống kê tổng quan cho Dashboard Admin

**Response**:
```json
{
  "totalUsers": 150,
  "activeUsers": 145,
  "newUsersToday": 5,
  "newUsersThisWeek": 23,
  "newUsersThisMonth": 87,
  
  "totalPageViewsToday": 1250,
  "totalPageViewsThisWeek": 8500,
  "totalPageViewsThisMonth": 35000,
  "uniqueVisitorsToday": 320,
  "uniqueVisitorsThisWeek": 1850,
  "uniqueVisitorsThisMonth": 7200,
  
  "totalLogins": 2500,
  "totalProductViews": 15000,
  "totalSearches": 3200,
  "totalAddToCarts": 1800,
  "totalCheckouts": 450
}
```

---

### 2️⃣ **Thống kê đăng ký theo khoảng thời gian**
```
GET /api/admin/statistics/registrations?startDate=2024-01-01&endDate=2024-01-31
```

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu (format: YYYY-MM-DD)
- `endDate` (required): Ngày kết thúc (format: YYYY-MM-DD)

**Response**:
```json
[
  {
    "date": "2024-01-01",
    "registrationCount": 12
  },
  {
    "date": "2024-01-02",
    "registrationCount": 8
  },
  {
    "date": "2024-01-03",
    "registrationCount": 15
  }
]
```

---

### 3️⃣ **Thống kê traffic theo khoảng thời gian**
```
GET /api/admin/statistics/traffic?startDate=2024-01-01&endDate=2024-01-31
```

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu (format: YYYY-MM-DD)
- `endDate` (required): Ngày kết thúc (format: YYYY-MM-DD)

**Response**:
```json
[
  {
    "date": "2024-01-01",
    "totalPageViews": 1250,
    "uniqueVisitors": 320,
    "loginCount": 85,
    "productViewCount": 450,
    "searchCount": 120,
    "addToCartCount": 65,
    "checkoutCount": 18
  },
  {
    "date": "2024-01-02",
    "totalPageViews": 1180,
    "uniqueVisitors": 295,
    "loginCount": 78,
    "productViewCount": 420,
    "searchCount": 110,
    "addToCartCount": 58,
    "checkoutCount": 15
  }
]
```

---

### 4️⃣ **Thống kê đăng ký 7 ngày gần nhất**
```
GET /api/admin/statistics/registrations/last-7-days
```

**Mô tả**: Lấy thống kê đăng ký từ 6 ngày trước đến hôm nay

**Response**: Giống API #2

---

### 5️⃣ **Thống kê traffic 7 ngày gần nhất**
```
GET /api/admin/statistics/traffic/last-7-days
```

**Mô tả**: Lấy thống kê traffic từ 6 ngày trước đến hôm nay

**Response**: Giống API #3

---

### 6️⃣ **Thống kê đăng ký 30 ngày gần nhất**
```
GET /api/admin/statistics/registrations/last-30-days
```

**Mô tả**: Lấy thống kê đăng ký từ 29 ngày trước đến hôm nay

**Response**: Giống API #2

---

### 7️⃣ **Thống kê traffic 30 ngày gần nhất**
```
GET /api/admin/statistics/traffic/last-30-days
```

**Mô tả**: Lấy thống kê traffic từ 29 ngày trước đến hôm nay

**Response**: Giống API #3

---

## 🔧 CÁCH TEST VỚI POSTMAN

### Bước 1: Đăng nhập để lấy Session

**Request**:
```
POST http://localhost:8080/auth/login
Content-Type: application/x-www-form-urlencoded

username=admin
password=password123
```

**Hoặc dùng Form Login**:
1. Mở browser: `http://localhost:8080/sign-in`
2. Đăng nhập với `admin` / `password123`
3. Copy cookie `JSESSIONID` từ browser

### Bước 2: Thêm Cookie vào Postman

**Cách 1: Tự động (Khuyến nghị)**
1. Trong Postman, chọn tab **Cookies**
2. Add Domain: `localhost`
3. Add Cookie: `JSESSIONID=<value_from_browser>`

**Cách 2: Thủ công**
1. Trong request, chọn tab **Headers**
2. Add header:
   - Key: `Cookie`
   - Value: `JSESSIONID=<your_session_id>`

### Bước 3: Test API

**Ví dụ 1: Dashboard Stats**
```
GET http://localhost:8080/api/admin/statistics/dashboard
Cookie: JSESSIONID=<your_session_id>
```

**Ví dụ 2: Thống kê 7 ngày**
```
GET http://localhost:8080/api/admin/statistics/registrations/last-7-days
Cookie: JSESSIONID=<your_session_id>
```

**Ví dụ 3: Thống kê theo khoảng thời gian**
```
GET http://localhost:8080/api/admin/statistics/traffic?startDate=2024-01-01&endDate=2024-01-31
Cookie: JSESSIONID=<your_session_id>
```

---

## 🐛 XỬ LÝ LỖI

### Lỗi 403 Forbidden
**Nguyên nhân**: 
- Chưa đăng nhập
- Đăng nhập với tài khoản USER (không phải ADMIN)
- Session đã hết hạn

**Giải pháp**:
1. Đăng nhập lại với tài khoản ADMIN
2. Kiểm tra cookie JSESSIONID có đúng không
3. Kiểm tra role của user phải là ADMIN

### Lỗi 401 Unauthorized
**Nguyên nhân**: Chưa gửi cookie authentication

**Giải pháp**: Thêm cookie JSESSIONID vào request

### Lỗi 400 Bad Request
**Nguyên nhân**: 
- Format ngày sai (phải là YYYY-MM-DD)
- Thiếu required parameters

**Giải pháp**: Kiểm tra lại format và parameters

---

## 📊 CẤU TRÚC DỮ LIỆU

### DashboardStatsResponse
```typescript
{
  // Thống kê người dùng
  totalUsers: number           // Tổng số người dùng
  activeUsers: number          // Số người dùng đang hoạt động
  newUsersToday: number        // Số người đăng ký hôm nay
  newUsersThisWeek: number     // Số người đăng ký tuần này
  newUsersThisMonth: number    // Số người đăng ký tháng này
  
  // Thống kê traffic
  totalPageViewsToday: number      // Tổng lượt xem hôm nay
  totalPageViewsThisWeek: number   // Tổng lượt xem tuần này
  totalPageViewsThisMonth: number  // Tổng lượt xem tháng này
  uniqueVisitorsToday: number      // Số người truy cập duy nhất hôm nay
  uniqueVisitorsThisWeek: number   // Số người truy cập duy nhất tuần này
  uniqueVisitorsThisMonth: number  // Số người truy cập duy nhất tháng này
  
  // Thống kê hoạt động
  totalLogins: number          // Tổng số lần đăng nhập
  totalProductViews: number    // Tổng lượt xem sản phẩm
  totalSearches: number        // Tổng số lần tìm kiếm
  totalAddToCarts: number      // Tổng số lần thêm vào giỏ
  totalCheckouts: number       // Tổng số lần thanh toán
}
```

### UserRegistrationStatsResponse
```typescript
{
  date: string                 // Ngày (YYYY-MM-DD)
  registrationCount: number    // Số người đăng ký trong ngày
}
```

### TrafficStatsResponse
```typescript
{
  date: string                 // Ngày (YYYY-MM-DD)
  totalPageViews: number       // Tổng số lượt xem trang
  uniqueVisitors: number       // Số người truy cập duy nhất
  loginCount: number           // Số lần đăng nhập
  productViewCount: number     // Số lượt xem sản phẩm
  searchCount: number          // Số lần tìm kiếm
  addToCartCount: number       // Số lần thêm vào giỏ
  checkoutCount: number        // Số lần thanh toán
}
```

---

## 💡 USE CASES

### 1. Hiển thị Dashboard Admin
```javascript
// Lấy tất cả thống kê tổng quan
fetch('/api/admin/statistics/dashboard')
  .then(res => res.json())
  .then(data => {
    console.log('Total Users:', data.totalUsers);
    console.log('New Users Today:', data.newUsersToday);
    console.log('Page Views Today:', data.totalPageViewsToday);
  });
```

### 2. Vẽ biểu đồ đăng ký 7 ngày
```javascript
// Lấy dữ liệu 7 ngày
fetch('/api/admin/statistics/registrations/last-7-days')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(d => d.date);
    const values = data.map(d => d.registrationCount);
    // Vẽ chart với Chart.js hoặc thư viện khác
  });
```

### 3. Vẽ biểu đồ traffic 30 ngày
```javascript
// Lấy dữ liệu 30 ngày
fetch('/api/admin/statistics/traffic/last-30-days')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(d => d.date);
    const pageViews = data.map(d => d.totalPageViews);
    const visitors = data.map(d => d.uniqueVisitors);
    // Vẽ multi-line chart
  });
```

---

## 📝 GHI CHÚ

- Tất cả API đều yêu cầu role **ADMIN**
- Sử dụng cookie-based authentication (JSESSIONID)
- Thống kê dựa trên bảng `users` và `user_activity_logs`
- Ngày tháng theo múi giờ server
- Unique visitors được tính theo `sessionId`

---

## 🎯 TỔNG KẾT

**Đã tạo 7 API endpoints cho Admin Statistics**:
1. ✅ Dashboard tổng quan
2. ✅ Thống kê đăng ký theo khoảng thời gian
3. ✅ Thống kê traffic theo khoảng thời gian
4. ✅ Thống kê đăng ký 7 ngày
5. ✅ Thống kê traffic 7 ngày
6. ✅ Thống kê đăng ký 30 ngày
7. ✅ Thống kê traffic 30 ngày

**Files đã tạo**:
- 3 DTO Response
- 1 Repository (UserActivityLogRepository)
- 1 Service (AdminStatisticsService)
- 1 Controller (AdminStatisticsController)
- Cập nhật UserRepository + SecurityConfig
