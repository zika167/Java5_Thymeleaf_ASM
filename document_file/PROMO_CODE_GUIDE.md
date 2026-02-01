# Hướng dẫn sử dụng Mã giảm giá

## Tổng quan
Hệ thống đã được tích hợp chức năng mã giảm giá cho giỏ hàng. Hiện tại có 1 mã giảm giá cố định.

## Mã giảm giá hiện có

### GIAM10K
- **Mã**: `GIAM10K`
- **Mô tả**: Giảm giá để tổng đơn hàng (đã bao gồm thuế VAT) còn 10.000đ
- **Cách hoạt động**: 
  - Tính tổng = Tạm tính + Thuế VAT (10%) + Phí vận chuyển
  - Nếu tổng > 10.000đ → Giảm giá = Tổng - 10.000đ
  - Nếu tổng ≤ 10.000đ → Không áp dụng giảm giá
- **Ví dụ**:
  - Tạm tính: 1.225.000đ + VAT: 122.500đ = 1.347.500đ → Giảm: 1.337.500đ → Còn: 10.000đ
  - Tạm tính: 500.000đ + VAT: 50.000đ = 550.000đ → Giảm: 540.000đ → Còn: 10.000đ

## Cách sử dụng

### Trên giao diện web
1. Vào trang giỏ hàng: `/cart`
2. Tìm phần "🎁 Mã giảm giá" ở bên phải
3. Nhập mã `GIAM10K` vào ô input
4. Click nút "Áp dụng"
5. Hệ thống sẽ hiển thị:
   - Tạm tính (giá gốc)
   - Giảm giá (số tiền được giảm)
   - Tổng cộng (sau khi giảm + VAT)

### Xóa mã giảm giá
- Click vào nút "✕" bên cạnh tên mã giảm giá trong phần tổng đơn hàng

## API Endpoints

### Áp dụng mã giảm giá
```http
POST /api/cart/apply-promo?promoCode=GIAM10K
```

**Response thành công (200)**:
```json
{
  "id": 1,
  "items": [...],
  "totalItems": 3,
  "totalPrice": 500000,
  "promoCode": "GIAM10K",
  "discountAmount": 490000,
  "finalPrice": 10000
}
```

**Response lỗi (400)**:
```
Mã giảm giá không hợp lệ
```

### Xóa mã giảm giá
```http
DELETE /api/cart/remove-promo
```

**Response thành công (200)**:
```json
{
  "id": 1,
  "items": [...],
  "totalItems": 3,
  "totalPrice": 500000,
  "promoCode": null,
  "discountAmount": 0,
  "finalPrice": 500000
}
```

## Cấu trúc dữ liệu

### Database
## Cấu trúc dữ liệu

### Database

#### Bảng `carts`
- `promo_code` VARCHAR(50) NULL - Lưu mã giảm giá đã áp dụng

#### Bảng `orders`
- `promo_code` VARCHAR(50) NULL - Mã giảm giá đã sử dụng
- `discount_amount` DECIMAL(10,2) DEFAULT 0.00 - Số tiền giảm giá

### Entity

#### Cart.java
```java
@Column(name = "promo_code", length = 50)
private String promoCode;
```

#### Order.java
```java
@Column(name = "promo_code", length = 50)
private String promoCode;

@Builder.Default
@Column(name = "discount_amount", precision = 10, scale = 2)
private BigDecimal discountAmount = BigDecimal.ZERO;
```

### DTO Response

#### CartResponse.java
```java
private String promoCode;
private BigDecimal discountAmount;
private BigDecimal finalPrice;
```

#### OrderResponse.java
```java
private String promoCode;
private BigDecimal discountAmount;
```

## Tính toán giá

### Công thức
1. **Tạm tính** = Tổng giá trị các sản phẩm trong giỏ
2. **Thuế VAT** = Tạm tính × 10%
3. **Tổng trước giảm giá** = Tạm tính + Thuế VAT + Phí vận chuyển
4. **Giảm giá** = Tổng trước giảm giá - 10.000đ (nếu áp dụng mã GIAM10K)
5. **Tổng cộng** = Tổng trước giảm giá - Giảm giá

### Ví dụ chi tiết

#### Ví dụ 1: Giỏ hàng 1.225.000đ
```
Giỏ hàng:
- Sản phẩm A: 500.000đ × 2 = 1.000.000đ
- Sản phẩm B: 225.000đ × 1 = 225.000đ

Tạm tính: 1.225.000đ
Thuế VAT (10%): 122.500đ
Phí vận chuyển: 0đ (Miễn phí)
Tổng trước giảm: 1.347.500đ

Áp dụng mã GIAM10K:
Giảm giá: 1.347.500đ - 10.000đ = 1.337.500đ
Tổng cộng: 10.000đ
```

#### Ví dụ 2: Giỏ hàng 500.000đ
```
Tạm tính: 500.000đ
Thuế VAT (10%): 50.000đ
Phí vận chuyển: 0đ
Tổng trước giảm: 550.000đ

Áp dụng mã GIAM10K:
Giảm giá: 550.000đ - 10.000đ = 540.000đ
Tổng cộng: 10.000đ
```

#### Ví dụ 3: Với phí vận chuyển
```
Tạm tính: 300.000đ
Thuế VAT (10%): 30.000đ
Phí vận chuyển: 20.000đ (Standard)
Tổng trước giảm: 350.000đ

Áp dụng mã GIAM10K:
Giảm giá: 350.000đ - 10.000đ = 340.000đ
Tổng cộng: 10.000đ
```

## Luồng xử lý từ Cart → Checkout → Payment

### 1. Trang Cart (/cart)
- User nhập mã giảm giá `GIAM10K`
- Hệ thống tính toán và hiển thị:
  - Tạm tính
  - Thuế VAT (10%)
  - Giảm giá (nếu có)
  - Tổng cộng
- Mã giảm giá được lưu vào `carts.promo_code`

### 2. Trang Checkout (/checkout)
- Hệ thống lấy thông tin giỏ hàng bao gồm mã giảm giá
- Hiển thị tổng tiền đã giảm
- User chọn địa chỉ giao hàng và phương thức thanh toán

### 3. Tạo đơn hàng (Create Order)
- Khi user xác nhận đặt hàng:
  - Lấy `promoCode` từ cart
  - Tính toán giảm giá theo công thức
  - Lưu vào bảng `orders`:
    - `promo_code`: Mã giảm giá đã dùng
    - `discount_amount`: Số tiền giảm
    - `total_amount`: Tổng tiền sau giảm
  - Xóa mã giảm giá khỏi cart sau khi tạo đơn

### 4. Trang Payment (/payment)
- Hiển thị thông tin đơn hàng với giá đã giảm
- User thanh toán số tiền `total_amount` (đã bao gồm giảm giá)

### 5. Xác nhận thanh toán
- Số tiền thanh toán = `order.totalAmount` (đã trừ giảm giá)
- Cập nhật trạng thái đơn hàng và thanh toán

### Thêm mã giảm giá mới
Để thêm mã giảm giá mới, chỉnh sửa trong `CartServiceImpl.java`:

```java
@Override
@Transactional
public CartResponse applyPromoCode(String identifier, String promoCode) {
    Cart cart = getOrCreateCartByIdentifier(identifier);
    
    // Thêm logic kiểm tra mã mới
    if ("GIAM10K".equalsIgnoreCase(promoCode.trim())) {
        // Logic cho mã GIAM10K
    } else if ("GIAM50".equalsIgnoreCase(promoCode.trim())) {
        // Logic cho mã giảm 50%
    } else {
        throw new IllegalArgumentException("Mã giảm giá không hợp lệ");
    }
    
    cart.setPromoCode(promoCode.trim().toUpperCase());
    cartRepository.save(cart);
    
    return getCartResponse(cart);
}
```

### Các loại mã giảm giá có thể thêm
1. **Giảm theo phần trăm**: Giảm X% tổng đơn hàng
2. **Giảm cố định**: Giảm X đồng
3. **Giảm theo điều kiện**: Giảm khi đơn hàng > X đồng
4. **Miễn phí vận chuyển**: Không tính phí ship
5. **Mua X tặng Y**: Tặng sản phẩm khi mua đủ số lượng

## Lưu ý
- Mã giảm giá được lưu trong cart của user
- Mã giảm giá được giữ nguyên khi user thêm/xóa sản phẩm
- Mã giảm giá bị xóa khi:
  - User xóa toàn bộ giỏ hàng
  - User hoàn tất đặt hàng (chuyển sang order)
- Mã giảm giá không phân biệt chữ hoa/thường
- Hiện tại chỉ áp dụng được 1 mã giảm giá tại một thời điểm
- **Giảm giá được tính SAU khi cộng thuế VAT và phí vận chuyển**
- Tổng tiền cuối cùng (sau giảm giá) được chuyển sang checkout và payment
- Thông tin giảm giá được lưu vào đơn hàng để theo dõi và báo cáo
- Thông tin giảm giá hiển thị trong:
  - Email xác nhận đơn hàng
  - Trang chi tiết đơn hàng (/order-detail)
  - Trang thanh toán (/payment)
  - Trang checkout (/checkout)
