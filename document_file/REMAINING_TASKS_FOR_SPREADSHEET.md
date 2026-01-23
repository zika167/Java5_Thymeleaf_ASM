# Remaining Tasks - For Planning Spreadsheet

**Ngày**: 21/01/2026  
**Format**: Task | Note  
**Trạng Thái**: Chưa bắt đầu

---

## 📋 COPY & PASTE VÀO SPREADSHEET

### 🔴 HIGH PRIORITY - Payment Integration (5 tasks)

```
VNPay Service Implementation | Tạo VNPayService với methods: createPaymentUrl, verifyPayment, processCallback
VNPay Configuration | Cấu hình VNPay credentials trong application.properties (merchant_id, secret_key, return_url)
Momo Service Implementation | Tạo MomoService với methods: createPaymentUrl, verifyPayment, processCallback
Payment Gateway Configuration | Cấu hình Momo credentials và payment endpoints
Payment Callback Handler | Tạo PaymentController với endpoints: /payment/vnpay/callback, /payment/momo/callback, xử lý success/failure
Payment Success Page | Tạo payment-success.html hiển thị thông tin thanh toán thành công, order details
Payment Failure Page | Tạo payment-failure.html hiển thị lỗi thanh toán, retry button
Update OrderService | Thêm method updatePaymentStatus, tích hợp với VNPay/Momo services
Payment Method Selection UI | Update checkout.html với radio buttons cho COD/VNPay/Momo, hiển thị payment gateway logos
```

---

### 🟡 MEDIUM PRIORITY - Missing Services (6 tasks)

```
ReviewService Interface | Tạo ReviewService interface với methods: createReview, getProductReviews, getUserReviews, deleteReview
ReviewService Implementation | Implement ReviewServiceImpl với validation (user đã mua sản phẩm), rating calculation, review moderation
Review Repository | Tạo ReviewRepository với custom queries: findByProduct, findByUser, calculateAverageRating
WishlistService Interface | Tạo WishlistService interface với methods: addToWishlist, removeFromWishlist, getUserWishlist, isInWishlist
WishlistService Implementation | Implement WishlistServiceImpl với duplicate check, product availability check
Wishlist Repository | Tạo WishlistRepository với custom queries: findByUser, findByUserAndProduct, countByProduct
AddressService Interface | Tạo AddressService interface với methods: createAddress, updateAddress, deleteAddress, setDefaultAddress, getUserAddresses
AddressService Implementation | Implement AddressServiceImpl với validation (max 5 addresses per user), default address logic
Address Repository | Tạo AddressRepository với custom queries: findByUser, findDefaultAddress, countByUser
```

---

### 🟡 MEDIUM PRIORITY - Frontend UI (4 tasks)

```
Review System UI | Tạo review-form component trong product-detail.html, rating stars, comment textarea, submit button
Review Display UI | Hiển thị reviews list trong product-detail.html, user avatar, rating, date, helpful button
Wishlist Page | Tạo wishlist.html (favourite.html) với product cards, remove button, add to cart button, empty state
Wishlist Integration | Thêm wishlist icon vào product cards, toggle add/remove, update count trong header
```

---

### 🟢 LOW PRIORITY - Testing (8 tasks)

```
ReviewService Unit Tests | Test createReview, getProductReviews, rating calculation, validation rules
WishlistService Unit Tests | Test addToWishlist, removeFromWishlist, duplicate check, product availability
AddressService Unit Tests | Test createAddress, setDefaultAddress, max addresses validation, delete address
Payment Integration Tests | Test VNPay callback, Momo callback, payment status update, order confirmation
Review UI Integration Tests | Test submit review, display reviews, rating calculation, user validation
Wishlist UI Integration Tests | Test add to wishlist, remove from wishlist, wishlist page display, empty state
E2E Checkout Flow Test | Test complete flow: browse → cart → checkout → payment → order confirmation → email
E2E User Journey Test | Test registration → login → browse → wishlist → cart → checkout → my orders
```

---

## 📊 TỔNG KẾT

### Theo Ưu Tiên
```
🔴 High Priority:    9 tasks (Payment Integration)
🟡 Medium Priority: 10 tasks (Services + Frontend)
🟢 Low Priority:     8 tasks (Testing)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Total:              27 tasks
```

### Theo Loại
```
Backend Services:    15 tasks
Frontend UI:          4 tasks
Testing:              8 tasks
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Total:               27 tasks
```

### Ước Tính Thời Gian
```
Payment Integration:  8-10 giờ
Missing Services:     6-8 giờ
Frontend UI:          4-5 giờ
Testing:              6-8 giờ
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Total:               24-31 giờ (~3-4 tuần)
```

---

## 📝 CHI TIẾT TỪNG TASK

### 1. Payment Integration (9 tasks)

#### Backend (5 tasks)
1. **VNPay Service Implementation**
   - File: `VNPayService.java`, `VNPayServiceImpl.java`
   - Methods: `createPaymentUrl()`, `verifyPayment()`, `processCallback()`
   - Logic: Generate payment URL với HMAC SHA512, verify signature

2. **VNPay Configuration**
   - File: `application.properties`
   - Properties: `vnpay.merchant.id`, `vnpay.secret.key`, `vnpay.return.url`, `vnpay.api.url`

3. **Momo Service Implementation**
   - File: `MomoService.java`, `MomoServiceImpl.java`
   - Methods: `createPaymentUrl()`, `verifyPayment()`, `processCallback()`
   - Logic: Generate payment URL với HMAC SHA256, verify signature

4. **Payment Gateway Configuration**
   - File: `application.properties`
   - Properties: `momo.partner.code`, `momo.access.key`, `momo.secret.key`, `momo.return.url`, `momo.api.url`

5. **Payment Callback Handler**
   - File: `PaymentController.java`
   - Endpoints: `GET /payment/vnpay/callback`, `GET /payment/momo/callback`
   - Logic: Verify signature, update order payment status, redirect to success/failure page

#### Frontend (3 tasks)
6. **Payment Success Page**
   - File: `payment-success.html`
   - Content: Success icon, order number, payment info, order details, continue shopping button

7. **Payment Failure Page**
   - File: `payment-failure.html`
   - Content: Error icon, error message, retry button, contact support link

8. **Payment Method Selection UI**
   - File: Update `checkout.html`
   - Content: Radio buttons (COD/VNPay/Momo), payment logos, payment instructions

#### Integration (1 task)
9. **Update OrderService**
   - File: `OrderService.java`
   - Methods: `updatePaymentStatus()`, `processPaymentCallback()`
   - Logic: Update order payment status, send email notification

---

### 2. Missing Services (9 tasks)

#### ReviewService (3 tasks)
1. **ReviewService Interface**
   - File: `ReviewService.java`
   - Methods: `createReview()`, `getProductReviews()`, `getUserReviews()`, `deleteReview()`, `calculateAverageRating()`

2. **ReviewService Implementation**
   - File: `ReviewServiceImpl.java`
   - Logic: Validate user purchased product, rating 1-5, calculate average rating, moderation

3. **Review Repository**
   - File: `ReviewRepository.java`
   - Queries: `findByProduct()`, `findByUser()`, `calculateAverageRating()`, `countByProduct()`

#### WishlistService (3 tasks)
4. **WishlistService Interface**
   - File: `WishlistService.java`
   - Methods: `addToWishlist()`, `removeFromWishlist()`, `getUserWishlist()`, `isInWishlist()`, `clearWishlist()`

5. **WishlistService Implementation**
   - File: `WishlistServiceImpl.java`
   - Logic: Check duplicate, validate product exists, check product availability

6. **Wishlist Repository**
   - File: `WishlistRepository.java`
   - Queries: `findByUser()`, `findByUserAndProduct()`, `countByProduct()`, `deleteByUserAndProduct()`

#### AddressService (3 tasks)
7. **AddressService Interface**
   - File: `AddressService.java`
   - Methods: `createAddress()`, `updateAddress()`, `deleteAddress()`, `setDefaultAddress()`, `getUserAddresses()`

8. **AddressService Implementation**
   - File: `AddressServiceImpl.java`
   - Logic: Max 5 addresses per user, auto-set first address as default, unset other defaults when setting new

9. **Address Repository**
   - File: `AddressRepository.java`
   - Queries: `findByUser()`, `findDefaultAddress()`, `countByUser()`, `findByUserAndIsDefault()`

---

### 3. Frontend UI (4 tasks)

1. **Review System UI**
   - File: Update `product-detail.html`
   - Components: Review form (rating stars, comment textarea, submit button), validation messages

2. **Review Display UI**
   - File: Update `product-detail.html`
   - Components: Reviews list, user avatar, rating stars, date, helpful button, pagination

3. **Wishlist Page**
   - File: Update `favourite.html`
   - Components: Product cards grid, remove button, add to cart button, empty state message

4. **Wishlist Integration**
   - File: Update `product-card` component
   - Components: Wishlist heart icon, toggle add/remove, update count in header

---

### 4. Testing (8 tasks)

#### Unit Tests (3 tasks)
1. **ReviewService Unit Tests**
   - File: `ReviewServiceTest.java`
   - Tests: Create review, get reviews, calculate rating, validate user purchased, delete review

2. **WishlistService Unit Tests**
   - File: `WishlistServiceTest.java`
   - Tests: Add to wishlist, remove from wishlist, check duplicate, validate product, clear wishlist

3. **AddressService Unit Tests**
   - File: `AddressServiceTest.java`
   - Tests: Create address, set default, max addresses validation, delete address, get user addresses

#### Integration Tests (2 tasks)
4. **Payment Integration Tests**
   - File: `PaymentIntegrationTest.java`
   - Tests: VNPay callback, Momo callback, payment status update, order confirmation, email notification

5. **Review UI Integration Tests**
   - File: `ReviewUITest.java`
   - Tests: Submit review, display reviews, rating calculation, user validation, pagination

#### E2E Tests (3 tasks)
6. **Wishlist UI Integration Tests**
   - File: `WishlistUITest.java`
   - Tests: Add to wishlist, remove from wishlist, wishlist page display, empty state, add to cart

7. **E2E Checkout Flow Test**
   - File: `CheckoutFlowE2ETest.java`
   - Tests: Browse → cart → checkout → payment → order confirmation → email

8. **E2E User Journey Test**
   - File: `UserJourneyE2ETest.java`
   - Tests: Registration → login → browse → wishlist → cart → checkout → my orders

---

## 🎯 ROADMAP

### Week 1: Payment Integration
```
Day 1-2: VNPay integration (service, config, callback)
Day 3-4: Momo integration (service, config, callback)
Day 5:   Payment UI (success/failure pages, checkout update)
```

### Week 2: Missing Services
```
Day 1-2: ReviewService (interface, implementation, repository)
Day 3:   WishlistService (interface, implementation, repository)
Day 4:   AddressService (interface, implementation, repository)
Day 5:   Integration và testing
```

### Week 3: Frontend UI
```
Day 1-2: Review system UI (form, display, integration)
Day 3-4: Wishlist UI (page, integration, empty state)
Day 5:   Polish và bug fixes
```

### Week 4: Testing
```
Day 1-2: Unit tests (Review, Wishlist, Address services)
Day 3:   Integration tests (Payment, Review UI, Wishlist UI)
Day 4-5: E2E tests (Checkout flow, User journey)
```

---

## 💡 NOTES

- **Payment Integration**: Cần test credentials từ VNPay và Momo (sandbox environment)
- **Review System**: Chỉ user đã mua sản phẩm mới được review
- **Wishlist**: Cần check product availability khi display
- **Address**: Max 5 addresses per user, first address auto-set as default
- **Testing**: Focus on critical paths trước (checkout, payment)

---

**Prepared by**: Kiro AI Assistant  
**Date**: 21/01/2026  
**Total Tasks**: 27 tasks  
**Estimated Time**: 24-31 hours (3-4 weeks)
