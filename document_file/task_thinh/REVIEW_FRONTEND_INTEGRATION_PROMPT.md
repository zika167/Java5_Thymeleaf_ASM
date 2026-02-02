# 🎨 PROMPT: TẠO FRONTEND CHO REVIEW FEATURE

## 📋 YÊU CẦU

Tôi cần bạn tích hợp giao diện (Frontend) cho tính năng Review đã có sẵn Backend API.

---

## ✅ BACKEND ĐÃ CÓ SẴN

### API Endpoints
1. `GET /api/reviews/products/{id}/rating` - Lấy rating summary
2. `GET /api/reviews/products/{id}` - Lấy danh sách reviews (có pagination)
3. `POST /api/reviews/products/{id}` - Tạo review mới (cần auth)
4. `DELETE /api/reviews/{id}` - Xóa review (cần auth)
5. `GET /api/reviews/products/{id}/check` - Kiểm tra đã review chưa
6. `GET /api/reviews/my-reviews` - Lấy reviews của user hiện tại

### Response Format
```json
// Rating Summary
{
  "productId": 1,
  "averageRating": 4.0,
  "totalReviews": 3,
  "ratingDistribution": {"1": 0, "2": 0, "3": 1, "4": 1, "5": 1},
  "ratingPercentage": {"1": 0.0, "2": 0.0, "3": 33.3, "4": 33.3, "5": 33.3}
}

// Reviews List
{
  "reviews": [
    {
      "id": 1,
      "rating": 5,
      "title": "Excellent coffee!",
      "comment": "Best espresso beans...",
      "isVerifiedPurchase": true,
      "createdAt": "2026-01-23T07:01:12",
      "userId": 2,
      "userName": "Imran Khan",
      "userAvatar": "/assets/img/avatar.jpg",
      "productId": 1,
      "productName": "Coffee Beans...",
      "productImage": "/assets/img/product/item-1.png"
    }
  ],
  "totalElements": 3,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10
}
```

---

## 🎯 CẦN TẠO FRONTEND

### 1. **Product Detail Page Integration**

**File**: `src/main/resources/templates/product-detail.html`

**Thêm vào cuối page (trước footer)**:

#### A. Rating Summary Section
- Hiển thị average rating (4.0/5.0)
- Hiển thị tổng số reviews
- Progress bars cho rating distribution (1-5 sao)
- Phần trăm cho mỗi mức rating

#### B. Review Form Section (nếu đã login)
- Star rating input (1-5 sao, interactive)
- Title input (optional, max 200 chars)
- Comment textarea (optional, max 2000 chars)
- Submit button
- Validation messages
- Hiển thị "Bạn đã review rồi" nếu đã review

#### C. Reviews List Section
- Danh sách reviews với:
  - User avatar và tên
  - Star rating display
  - Verified purchase badge (nếu có)
  - Review title và comment
  - Timestamp (relative: "2 ngày trước")
  - Delete button (chỉ hiện cho owner/admin)
- Pagination (nếu > 10 reviews)
- Sort options (mới nhất, rating cao/thấp)

### 2. **My Reviews Page** (Optional)

**File**: `src/main/resources/templates/my-reviews.html`

- Danh sách reviews của user hiện tại
- Hiển thị sản phẩm đã review
- Có thể xóa reviews
- Pagination

---

## 🎨 STYLING

### Sử dụng CSS có sẵn
- File: `src/main/resources/static/assets/css/main.css`
- Style guide: Theo design hiện tại của project
- Colors: Sử dụng CSS variables có sẵn
- Responsive: Mobile-friendly

### Components cần style
1. **Rating Stars**: ⭐⭐⭐⭐⭐
2. **Progress Bars**: Cho rating distribution
3. **Review Cards**: Card layout cho mỗi review
4. **Form**: Star rating input, text inputs
5. **Buttons**: Submit, Delete
6. **Badges**: Verified Purchase badge

---

## 💻 JAVASCRIPT

### File: `src/main/resources/static/assets/js/reviews.js`

**Functions cần implement**:

```javascript
// 1. Load rating summary
async function loadProductRating(productId) {
    const response = await fetch(`/api/reviews/products/${productId}/rating`);
    const data = await response.json();
    displayRatingSummary(data);
}

// 2. Load reviews list
async function loadProductReviews(productId, page = 0, size = 10) {
    const response = await fetch(`/api/reviews/products/${productId}?page=${page}&size=${size}`);
    const data = await response.json();
    displayReviews(data);
    displayPagination(data);
}

// 3. Check review status
async function checkUserReview(productId) {
    const response = await fetch(`/api/reviews/products/${productId}/check`);
    const data = await response.json();
    // Show/hide review form based on hasReviewed
}

// 4. Create review
async function createReview(productId, reviewData) {
    const response = await fetch(`/api/reviews/products/${productId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reviewData)
    });
    // Handle success/error
}

// 5. Delete review
async function deleteReview(reviewId) {
    if (!confirm('Bạn có chắc chắn muốn xóa?')) return;
    const response = await fetch(`/api/reviews/${reviewId}`, {
        method: 'DELETE'
    });
    // Reload reviews after delete
}

// 6. Star rating interaction
function initStarRating() {
    // Click to select rating
    // Hover effect
    // Update hidden input value
}

// 7. Display functions
function displayRatingSummary(data) { }
function displayReviews(data) { }
function displayPagination(data) { }
function generateStarRating(rating) { }
function formatDate(dateString) { }
```

---

## 🔧 INTEGRATION STEPS

### Step 1: Update product-detail.html
```html
<!-- Thêm vào <head> -->
<link rel="stylesheet" th:href="@{/assets/css/reviews.css}">

<!-- Thêm vào cuối <body>, trước footer -->
<div class="container">
    <div class="review-section">
        <!-- Rating Summary -->
        <div id="ratingSummary"></div>
        
        <!-- Review Form -->
        <div id="reviewFormContainer"></div>
        
        <!-- Reviews List -->
        <div id="reviewsList"></div>
        
        <!-- Pagination -->
        <div id="reviewsPagination"></div>
    </div>
</div>

<!-- Thêm script -->
<script th:src="@{/assets/js/reviews.js}"></script>
<script>
    // Get productId from URL or page
    const productId = /*[[${product.id}]]*/ 1;
    
    // Initialize on page load
    document.addEventListener('DOMContentLoaded', () => {
        loadProductRating(productId);
        loadProductReviews(productId);
        checkUserReview(productId);
        initStarRating();
    });
</script>
```

### Step 2: Create reviews.css
- Style cho rating summary
- Style cho review cards
- Style cho form
- Responsive design

### Step 3: Create reviews.js
- Implement tất cả functions trên
- Handle errors
- Show loading states
- Update UI after actions

---

## 🎯 FEATURES CẦN CÓ

### Must Have (Bắt buộc)
- [x] Hiển thị rating summary
- [x] Hiển thị danh sách reviews
- [x] Form tạo review (với star rating)
- [x] Pagination
- [x] Delete review (owner/admin)
- [x] Verified purchase badge
- [x] Responsive design

### Nice to Have (Tùy chọn)
- [ ] Sort reviews (mới nhất, rating cao/thấp)
- [ ] Filter reviews (verified only, rating)
- [ ] Image upload với review
- [ ] Helpful votes
- [ ] Reply to reviews

---

## 📱 RESPONSIVE DESIGN

### Desktop (> 768px)
- 3 columns cho review cards
- Full width rating summary
- Side-by-side form layout

### Tablet (768px - 1024px)
- 2 columns cho review cards
- Stacked form layout

### Mobile (< 768px)
- 1 column cho review cards
- Vertical layout
- Touch-friendly buttons
- Collapsible sections

---

## 🔐 AUTHENTICATION

### Nếu chưa login
- Hiển thị reviews (read-only)
- Hiển thị rating summary
- Ẩn review form
- Hiển thị "Đăng nhập để đánh giá"

### Nếu đã login
- Hiển thị review form
- Có thể tạo review
- Có thể xóa review của mình
- Admin có thể xóa bất kỳ review nào

---

## 🎨 UI/UX REQUIREMENTS

### Rating Summary
```
┌─────────────────────────────────────┐
│  4.0 ⭐⭐⭐⭐                        │
│  3 đánh giá                         │
│                                     │
│  5⭐ ████████████████░░░░░ 33.3% (1)│
│  4⭐ ████████████████░░░░░ 33.3% (1)│
│  3⭐ ████████████████░░░░░ 33.3% (1)│
│  2⭐ ░░░░░░░░░░░░░░░░░░░░░  0.0% (0)│
│  1⭐ ░░░░░░░░░░░░░░░░░░░░░  0.0% (0)│
└─────────────────────────────────────┘
```

### Review Card
```
┌─────────────────────────────────────┐
│ 👤 Imran Khan  ✓ Đã mua hàng       │
│ ⭐⭐⭐⭐⭐  2 ngày trước            │
│                                     │
│ Excellent coffee!                   │
│ Best espresso beans I have ever...  │
│                                     │
│                          [🗑️ Xóa]  │
└─────────────────────────────────────┘
```

### Review Form
```
┌─────────────────────────────────────┐
│ Viết đánh giá của bạn               │
│                                     │
│ Đánh giá: ☆☆☆☆☆ (click to rate)   │
│                                     │
│ Tiêu đề: [________________]         │
│                                     │
│ Nội dung:                           │
│ [_____________________________]     │
│ [_____________________________]     │
│ [_____________________________]     │
│                                     │
│              [Gửi đánh giá]         │
└─────────────────────────────────────┘
```

---

## 🧪 TESTING CHECKLIST

### Functional Testing
- [ ] Load rating summary correctly
- [ ] Load reviews list with pagination
- [ ] Create review successfully
- [ ] Delete review successfully
- [ ] Star rating interaction works
- [ ] Form validation works
- [ ] Pagination works
- [ ] Authentication check works

### UI Testing
- [ ] Responsive on mobile
- [ ] Responsive on tablet
- [ ] Responsive on desktop
- [ ] Star rating displays correctly
- [ ] Progress bars display correctly
- [ ] Verified badge shows correctly
- [ ] Timestamps format correctly

### Error Handling
- [ ] Show error when API fails
- [ ] Show error when validation fails
- [ ] Show error when not authenticated
- [ ] Show error when already reviewed

---

## 📚 REFERENCE FILES

### Existing Files to Reference
1. `src/main/resources/templates/product-detail.html` - Current product page
2. `src/main/resources/static/assets/css/main.css` - Existing styles
3. `src/main/resources/static/assets/js/scripts.js` - Existing JS patterns
4. `src/main/resources/static/assets/js/cart-api.js` - API call examples

### Files Already Created (Backend)
1. `src/main/java/poly/edu/java5_asm/controller/ReviewController.java`
2. `src/main/java/poly/edu/java5_asm/service/ReviewService.java`
3. `src/main/java/poly/edu/java5_asm/repository/ReviewRepository.java`
4. `REVIEW_API_FINAL_REPORT.md` - API documentation

---

## 🎯 DELIVERABLES

Khi hoàn thành, tôi cần:

1. ✅ **Updated product-detail.html** với review section
2. ✅ **reviews.css** với styling đầy đủ
3. ✅ **reviews.js** với tất cả functions
4. ✅ **Test được trên browser** tại `http://localhost:8080/products/detail?id=1`
5. ✅ **Responsive** trên mobile/tablet/desktop
6. ✅ **Working** với API đã có sẵn

---

## 💡 NOTES

- Backend API đã hoạt động 100%
- Đã test với Postman/cURL
- Database đã có 3 reviews mẫu cho product ID 1
- Authentication sử dụng JWT token trong cookie
- Cần handle cả trường hợp user đã login và chưa login

---

## 🚀 QUICK START

Khi bạn nhận prompt này, hãy:

1. Đọc API endpoints và response format
2. Tạo reviews.css với styling
3. Tạo reviews.js với functions
4. Update product-detail.html
5. Test trên browser
6. Fix bugs nếu có

**Thời gian ước tính**: 15-20 phút

---

## ❓ QUESTIONS TO ASK

Nếu cần clarification, hỏi về:
- Có cần tạo trang My Reviews riêng không?
- Có cần thêm sort/filter options không?
- Có cần image upload không?
- Có cần animation/transitions không?

---

**Prepared by**: Kiro AI  
**Date**: 2026-01-24  
**Status**: Ready to use  
**Backend Status**: ✅ Complete and tested

---

## 🎯 USAGE

**Khi muốn tạo frontend, chỉ cần paste prompt này và nói**:

> "Hãy tạo frontend cho Review feature theo prompt trong file REVIEW_FRONTEND_INTEGRATION_PROMPT.md"

**Hoặc ngắn gọn hơn**:

> "Implement review frontend theo prompt đã chuẩn bị"

**Hoặc**:

> "Làm theo REVIEW_FRONTEND_INTEGRATION_PROMPT.md"

---

✅ **Prompt này chứa TẤT CẢ thông tin cần thiết để tạo frontend hoàn chỉnh!**
