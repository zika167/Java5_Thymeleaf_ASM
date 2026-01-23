# CC-Doctor (Caffeine Calculator) Feature

**Date**: 21/01/2026  
**Status**: ✅ COMPLETED  
**Build**: ✅ SUCCESS

---

## 📋 TỔNG QUAN

Tính năng CC-Doctor (Caffeine Calculator) giúp người dùng tính toán lượng caffeine nạp vào cơ thể và so sánh với mức an toàn dựa trên độ tuổi, giới tính và tình trạng mang thai.

---

## 🎯 TÍNH NĂNG

### Input
- **Tuổi** (int): Độ tuổi của người dùng
- **Giới tính** (radio): Nam/Nữ
- **Đang mang thai** (checkbox): Chỉ áp dụng cho nữ giới
- **Loại đồ uống** (dropdown): Chọn từ danh sách đồ uống có sẵn
- **Số lượng** (int): Số lượng đồ uống đã tiêu thụ

### Output
- **Tổng lượng caffeine**: Số mg caffeine đã nạp
- **Giới hạn an toàn**: Mức an toàn theo độ tuổi
- **Phần trăm**: % so với giới hạn an toàn
- **Trạng thái**: An toàn (xanh) / Cảnh báo (vàng) / Nguy hiểm (đỏ)
- **Thông báo**: Message cụ thể về tình trạng
- **Khuyến nghị**: Lời khuyên dựa trên kết quả

---

## 📊 GIỚI HẠN AN TOÀN (Hardcoded)

```java
Người lớn (18+):           400mg/ngày
Phụ nữ mang thai:          200mg/ngày
Thanh thiếu niên (12-18):  100mg/ngày
Trẻ em (<12 tuổi):         0mg/ngày
```

---

## 🍵 LOẠI ĐỒ UỐNG (23 loại)

### ☕ Cà phê (8 loại)
```
Espresso (1 shot, 30ml):           63mg
Cà phê phin (1 ly, 150ml):         100mg
Cà phê sữa đá (1 ly, 200ml):       80mg
Americano (1 ly, 240ml):           95mg
Cappuccino (1 ly, 240ml):          75mg
Latte (1 ly, 240ml):               75mg
Cà phê đen (1 ly, 240ml):          95mg
Cà phê pha máy (1 ly, 240ml):      120mg
```

### 🍵 Trà (4 loại)
```
Trà đen (1 ly, 240ml):             47mg
Trà xanh (1 ly, 240ml):            28mg
Trà ô long (1 ly, 240ml):          38mg
Trà sữa (1 ly, 240ml):             30mg
```

### ⚡ Nước tăng lực (4 loại)
```
Red Bull (1 lon, 250ml):           80mg
Monster Energy (1 lon, 500ml):     160mg
Sting (1 lon, 330ml):              100mg
Number 1 (1 chai, 330ml):          50mg
```

### 🥤 Nước ngọt & Khác (7 loại)
```
Coca Cola (1 lon, 330ml):          34mg
Pepsi (1 lon, 330ml):              38mg
Sô-cô-la nóng (1 ly, 240ml):       25mg
Sô-cô-la đen (1 thanh, 40g):       20mg
```

---

## 🎨 LOGIC TÍNH TOÁN

### 1. Tính tổng caffeine
```java
totalCaffeine = caffeinePerDrink × quantity
```

### 2. Xác định giới hạn an toàn
```java
if (age < 12) → 0mg
else if (isPregnant) → 200mg
else if (age < 18) → 100mg
else → 400mg
```

### 3. Tính phần trăm
```java
percentage = (totalCaffeine / safeLimit) × 100
```

### 4. Xác định trạng thái
```java
if (safeLimit == 0) → DANGER (red)
else if (percentage < 70) → SAFE (green)
else if (percentage <= 100) → WARNING (yellow)
else → DANGER (red)
```

---

## 💻 CẤU TRÚC CODE

### Backend (5 files)

#### 1. DTO - Request
**File**: `src/main/java/poly/edu/java5_asm/dto/CaffeineCalculationRequest.java`
```java
- age: Integer
- gender: String (MALE, FEMALE)
- isPregnant: Boolean
- drinkType: String
- quantity: Integer
```

#### 2. DTO - Result
**File**: `src/main/java/poly/edu/java5_asm/dto/CaffeineCalculationResult.java`
```java
- totalCaffeine: Integer (mg)
- safeLimit: Integer (mg)
- percentage: Double (%)
- status: String (SAFE, WARNING, DANGER)
- statusColor: String (green, yellow, red)
- message: String
- recommendation: String
```

#### 3. Service Interface
**File**: `src/main/java/poly/edu/java5_asm/service/CaffeineService.java`
```java
- calculateCaffeine(request): CaffeineCalculationResult
- getDrinkTypes(): Map<String, Integer>
- getSafeLimit(age, gender, isPregnant): Integer
```

#### 4. Service Implementation
**File**: `src/main/java/poly/edu/java5_asm/service/impl/CaffeineServiceImpl.java`
- Hardcoded safe limits constants
- Hardcoded drink types with caffeine content
- Logic tính toán và so sánh
- Validation input
- Generate messages và recommendations

#### 5. Controller
**File**: `src/main/java/poly/edu/java5_asm/controller/CaffeineController.java`
```java
GET  /cc-doctor          → Show calculator page
POST /cc-doctor/calculate → Calculate and show result
```

### Frontend (2 files)

#### 1. HTML Page
**File**: `src/main/resources/templates/cc-doctor.html`
- Form với Thymeleaf binding (th:object, th:field)
- Dropdown với optgroup (Cà phê, Trà, Nước tăng lực, Khác)
- Result display với color-coded status
- Progress bar animation
- Safe limits info box
- Responsive design (col-8 / col-4)

#### 2. Header Menu
**File**: `src/main/resources/templates/fragments/header.html`
- Added menu item 4: CC-Doctor
- Link to /cc-doctor

---

## 🎨 GIAO DIỆN

### Layout
```
┌─────────────────────────────────────────┐
│ Header (with CC-Doctor menu item)      │
├─────────────────────────────────────────┤
│ Breadcrumbs: Trang chủ > CC-Doctor     │
├─────────────────────────────────────────┤
│ ┌─────────────────┬─────────────────┐  │
│ │ Form (col-8)    │ Result (col-4)  │  │
│ │                 │                 │  │
│ │ - Tuổi          │ - Status Badge  │  │
│ │ - Giới tính     │ - Caffeine mg   │  │
│ │ - Mang thai     │ - Progress Bar  │  │
│ │ - Loại đồ uống  │ - Message       │  │
│ │ - Số lượng      │ - Recommendation│  │
│ │                 │ - Safe Limits   │  │
│ │ [Tính toán]     │                 │  │
│ └─────────────────┴─────────────────┘  │
├─────────────────────────────────────────┤
│ Footer                                  │
└─────────────────────────────────────────┘
```

### Color Coding
- **Xanh (green)**: < 70% - An toàn
- **Vàng (yellow)**: 70-100% - Cảnh báo
- **Đỏ (red)**: > 100% hoặc trẻ em - Nguy hiểm

---

## ✅ FEATURES IMPLEMENTED

### Form Features
- [x] Input validation (required, min, max)
- [x] Radio buttons cho giới tính
- [x] Checkbox cho mang thai
- [x] Dropdown với optgroup (phân loại đồ uống)
- [x] Number input với min/max
- [x] Thymeleaf form binding (th:object, th:field)
- [x] Error handling và display

### Calculation Features
- [x] Tính tổng caffeine dựa trên loại và số lượng
- [x] Xác định giới hạn an toàn theo tuổi
- [x] Xử lý đặc biệt cho phụ nữ mang thai
- [x] Xử lý đặc biệt cho trẻ em (<12 tuổi)
- [x] Tính phần trăm so với giới hạn
- [x] Xác định trạng thái (SAFE/WARNING/DANGER)
- [x] Generate messages phù hợp
- [x] Generate recommendations

### UI Features
- [x] Responsive design (desktop & mobile)
- [x] Color-coded status badge
- [x] Animated progress bar
- [x] Safe limits info box
- [x] Result display với formatting
- [x] Empty state (khi chưa tính toán)
- [x] Error messages
- [x] Consistent với design system hiện tại

---

## 🧪 TEST CASES

### Test Case 1: Người lớn - An toàn
```
Input:
- Tuổi: 25
- Giới tính: Nam
- Mang thai: No
- Đồ uống: Cà phê đen (95mg)
- Số lượng: 2

Expected Output:
- Total: 190mg
- Limit: 400mg
- Percentage: 47.5%
- Status: SAFE (green)
```

### Test Case 2: Phụ nữ mang thai - Cảnh báo
```
Input:
- Tuổi: 28
- Giới tính: Nữ
- Mang thai: Yes
- Đồ uống: Cà phê sữa đá (80mg)
- Số lượng: 2

Expected Output:
- Total: 160mg
- Limit: 200mg
- Percentage: 80%
- Status: WARNING (yellow)
```

### Test Case 3: Thanh thiếu niên - Nguy hiểm
```
Input:
- Tuổi: 15
- Giới tính: Nam
- Mang thai: No
- Đồ uống: Monster Energy (160mg)
- Số lượng: 1

Expected Output:
- Total: 160mg
- Limit: 100mg
- Percentage: 160%
- Status: DANGER (red)
```

### Test Case 4: Trẻ em - Nguy hiểm
```
Input:
- Tuổi: 10
- Giới tính: Nữ
- Mang thai: No
- Đồ uống: Coca Cola (34mg)
- Số lượng: 1

Expected Output:
- Total: 34mg
- Limit: 0mg
- Percentage: N/A
- Status: DANGER (red)
- Message: "Trẻ em dưới 12 tuổi không nên sử dụng caffeine!"
```

---

## 📝 USAGE

### 1. Access Page
```
URL: http://localhost:8080/cc-doctor
Menu: Header > CC-Doctor
```

### 2. Fill Form
1. Nhập tuổi
2. Chọn giới tính
3. Check "Đang mang thai" nếu áp dụng
4. Chọn loại đồ uống từ dropdown
5. Nhập số lượng
6. Click "Tính toán"

### 3. View Result
- Xem status badge (màu sắc)
- Xem tổng caffeine và giới hạn
- Xem progress bar
- Đọc message và recommendation
- Tham khảo bảng giới hạn an toàn

---

## 🔧 TECHNICAL DETAILS

### Dependencies
- Spring Boot Web
- Thymeleaf
- Lombok
- Validation

### Design Patterns
- Service Layer Pattern
- DTO Pattern
- Builder Pattern (for Result)

### Validation
- Age: required, min=1, max=120
- Gender: required
- DrinkType: required, must exist in map
- Quantity: required, min=1, max=20

### Error Handling
- IllegalArgumentException for invalid input
- Display error message on page
- Keep form data after error

---

## 📊 STATISTICS

### Files Created: 6
1. CaffeineCalculationRequest.java (DTO)
2. CaffeineCalculationResult.java (DTO)
3. CaffeineService.java (Interface)
4. CaffeineServiceImpl.java (Implementation)
5. CaffeineController.java (Controller)
6. cc-doctor.html (View)

### Files Modified: 1
1. header.html (Added menu item)

### Lines of Code: ~600 lines
- Java: ~250 lines
- HTML: ~350 lines

### Build Status: ✅ SUCCESS
```
[INFO] BUILD SUCCESS
[INFO] Total time:  3.416 s
```

---

## 🎯 NEXT STEPS

### Enhancements (Optional)
1. Add more drink types
2. Save calculation history
3. Export result as PDF
4. Add charts/graphs
5. Multi-language support
6. API endpoint for mobile app
7. Integration with product catalog

### Testing
1. Unit tests for CaffeineService
2. Integration tests for Controller
3. E2E tests for UI

---

## 📚 REFERENCES

- FDA Caffeine Guidelines
- USDA Food Data Central Database
- IFIC Caffeine Calculator: https://ific.org/resources/caffeine-calculator/

---

**Prepared by**: Kiro AI Assistant  
**Date**: 21/01/2026  
**Status**: ✅ COMPLETED  
**Build**: ✅ SUCCESS
