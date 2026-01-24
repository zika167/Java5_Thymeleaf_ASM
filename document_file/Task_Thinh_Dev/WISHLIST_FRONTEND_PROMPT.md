# 🎨 Wishlist Feature - Frontend Implementation Prompt

## 📋 Overview / Tổng Quan

**Feature:** Wishlist (Danh Sách Yêu Thích)  
**Backend Status:** ✅ Complete (7 REST API endpoints)  
**Frontend Status:** ❌ Not Implemented  
**Task:** Create complete frontend UI for Wishlist feature

---

## 🎯 Requirements / Yêu Cầu

### 1. Wishlist Page / Trang Wishlist (`/wishlist` or `/favourite`)

**URL:** `/wishlist` hoặc `/favourite`  
**Template:** `src/main/resources/templates/wishlist.html`  
**Controller Method:** Add to `HomeController.java` or create `WishlistPageController.java`

**Features Required:**
- Display all wishlist items in a grid/list
- Show product image, name, price, discount price
- "Remove" button for each item
- "Clear All" button
- "Add to Cart" button for each item
- Empty state message when no items
- Item count display
- Responsive design (mobile-friendly)

**Vietnamese:**
- Hiển thị tất cả sản phẩm yêu thích dạng lưới/danh sách
- Hiển thị ảnh, tên, giá, giá giảm của sản phẩm
- Nút "Xóa" cho từng item
- Nút "Xóa Tất Cả"
- Nút "Thêm Vào Giỏ" cho từng item
- Thông báo khi danh sách trống
- Hiển thị số lượng items
- Responsive (thân thiện mobile)

---

### 2. Wishlist Icon in Header / Icon Wishlist Trong Header

**Location:** `src/main/resources/templates/fragments/header.html`

**Features Required:**
- Heart icon (outline when empty, filled when has items)
- Badge showing item count
- Link to wishlist page
- Update count dynamically when items added/removed

**Vietnamese:**
- Icon trái tim (outline khi trống, filled khi có items)
- Badge hiển thị số lượng
- Link đến trang wishlist
- Cập nhật số lượng tự động khi thêm/xóa

---

### 3. Wishlist Button on Product Cards / Nút Wishlist Trên Product Card

**Locations to Add:**
- Home page product cards (`index.html`)
- Category page product cards (`category.html`)
- Product detail page (`product-detail.html`)

**Features Required:**
- Heart icon button (top-right corner of product card)
- Toggle functionality (add/remove on click)
- Visual feedback (filled/outline, color change)
- Show loading state during API call
- Toast notification on success/error

**Vietnamese:**
- Nút icon trái tim (góc trên bên phải của product card)
- Chức năng toggle (thêm/xóa khi click)
- Phản hồi trực quan (filled/outline, đổi màu)
- Hiển thị trạng thái loading khi gọi API
- Thông báo toast khi thành công/lỗi

---

## 🔌 API Endpoints Available / API Endpoints Có Sẵn

### Base URL
```
http://localhost:8080/api/wishlist
```

### 1. Get Wishlist Count
```http
GET /api/wishlist/count
Response: {"count": 0}
```

### 2. Check Product in Wishlist
```http
GET /api/wishlist/products/{productId}/check
Response: {"inWishlist": false}
```

### 3. Add Product to Wishlist
```http
POST /api/wishlist/products/{productId}
Response: {
  "id": 1,
  "createdAt": "2026-01-25T01:32:14",
  "productId": 1,
  "productName": "Coffee Beans",
  "productPrice": 47,
  "productDiscountPrice": 40,
  "productImageUrl": "/assets/img/product/item-1.png",
  "productStock": 100,
  "productCategoryName": "Coffee",
  "productBrandName": "Lavazza"
}
```

### 4. Get User Wishlist
```http
GET /api/wishlist
Response: [
  {
    "id": 1,
    "createdAt": "2026-01-25T01:32:14",
    "productId": 1,
    "productName": "Coffee Beans",
    ...
  }
]
```

### 5. Remove Product from Wishlist
```http
DELETE /api/wishlist/products/{productId}
Response: {"message": "Đã xóa khỏi danh sách yêu thích"}
```

### 6. Toggle Product (Add/Remove)
```http
POST /api/wishlist/products/{productId}/toggle
Response: {
  "inWishlist": true,
  "message": "Đã thêm vào yêu thích"
}
```

### 7. Clear Wishlist
```http
DELETE /api/wishlist
Response: {"message": "Đã xóa toàn bộ danh sách yêu thích"}
```

---

## 📁 File Structure / Cấu Trúc Files

### Templates to Create/Modify

```
src/main/resources/templates/
├── wishlist.html                    # NEW - Wishlist page
├── fragments/
│   └── header.html                  # MODIFY - Add wishlist icon
├── index.html                       # MODIFY - Add wishlist button to product cards
├── category.html                    # MODIFY - Add wishlist button to product cards
└── product-detail.html              # MODIFY - Add wishlist button
```

### JavaScript Files

```
src/main/resources/static/assets/js/
├── wishlist.js                      # NEW - Wishlist functionality
└── scripts.js                       # MODIFY - Add wishlist icon update
```

### CSS Files

```
src/main/resources/static/assets/css/
└── main.css                         # MODIFY - Add wishlist styles
```

Or use SCSS:
```
src/main/resources/scss/
├── pages/
│   └── _wishlist.scss              # NEW - Wishlist page styles
└── components/
    └── _wishlist-button.scss       # NEW - Wishlist button styles
```

### Controller

```java
src/main/java/poly/edu/java5_asm/controller/
└── HomeController.java              # MODIFY - Add wishlist page mapping
```

Or create new:
```java
src/main/java/poly/edu/java5_asm/controller/
└── WishlistPageController.java      # NEW - Dedicated wishlist controller
```

---

## 🎨 Design Guidelines / Hướng Dẫn Thiết Kế

### Colors / Màu Sắc

```css
/* Wishlist Heart Icon */
--wishlist-empty: #e0e0e0;          /* Gray outline when empty */
--wishlist-filled: #ff4757;          /* Red filled when in wishlist */
--wishlist-hover: #ff6b81;           /* Light red on hover */

/* Badge */
--badge-bg: #ff4757;                 /* Red background */
--badge-text: #ffffff;               /* White text */
```

### Icons / Biểu Tượng

Use existing icon system or add:
- Heart outline: `♡` or `<i class="far fa-heart"></i>` (Font Awesome)
- Heart filled: `♥` or `<i class="fas fa-heart"></i>` (Font Awesome)

### Layout / Bố Cục

**Wishlist Page:**
- Grid layout: 4 columns on desktop, 2 on tablet, 1 on mobile
- Card style similar to product cards
- Sticky header with count and "Clear All" button

**Product Card Button:**
- Position: absolute top-right corner
- Size: 40x40px
- Border-radius: 50% (circular)
- Background: white with shadow
- Z-index: 10 (above product image)

---

## 💻 Implementation Examples / Ví Dụ Triển Khai

### 1. Wishlist Page HTML Structure

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Wishlist - My Favorite Products</title>
    <!-- Include head fragment -->
    <th:block th:replace="~{fragments/head :: head}"></th:block>
</head>
<body>
    <!-- Include header -->
    <th:block th:replace="~{fragments/header :: header}"></th:block>

    <main class="wishlist-page">
        <div class="container">
            <div class="wishlist-header">
                <h1>My Wishlist</h1>
                <div class="wishlist-actions">
                    <span class="wishlist-count">0 items</span>
                    <button class="btn btn-danger" id="clearAllBtn">
                        Clear All
                    </button>
                </div>
            </div>

            <!-- Empty State -->
            <div class="wishlist-empty" id="emptyState" style="display: none;">
                <img src="/assets/img/empty-wishlist.svg" alt="Empty">
                <h2>Your wishlist is empty</h2>
                <p>Add products you love to your wishlist</p>
                <a href="/" class="btn btn-primary">Start Shopping</a>
            </div>

            <!-- Wishlist Grid -->
            <div class="wishlist-grid" id="wishlistGrid">
                <!-- Items will be loaded here via JavaScript -->
            </div>
        </div>
    </main>

    <!-- Include footer -->
    <th:block th:replace="~{fragments/footer :: footer}"></th:block>

    <!-- Wishlist JavaScript -->
    <script src="/assets/js/wishlist.js"></script>
</body>
</html>
```

### 2. Wishlist JavaScript (wishlist.js)

```javascript
// Wishlist functionality
class WishlistManager {
    constructor() {
        this.baseUrl = '/api/wishlist';
        this.init();
    }

    async init() {
        await this.loadWishlist();
        this.attachEventListeners();
    }

    async loadWishlist() {
        try {
            const response = await fetch(this.baseUrl);
            const items = await response.json();
            
            this.renderWishlist(items);
            this.updateCount(items.length);
        } catch (error) {
            console.error('Error loading wishlist:', error);
            this.showError('Failed to load wishlist');
        }
    }

    renderWishlist(items) {
        const grid = document.getElementById('wishlistGrid');
        const emptyState = document.getElementById('emptyState');

        if (items.length === 0) {
            grid.style.display = 'none';
            emptyState.style.display = 'block';
            return;
        }

        grid.style.display = 'grid';
        emptyState.style.display = 'none';

        grid.innerHTML = items.map(item => `
            <div class="wishlist-item" data-id="${item.id}" data-product-id="${item.productId}">
                <div class="wishlist-item-image">
                    <img src="${item.productImageUrl}" alt="${item.productName}">
                </div>
                <div class="wishlist-item-info">
                    <h3>${item.productName}</h3>
                    <div class="wishlist-item-price">
                        ${item.productDiscountPrice ? 
                            `<span class="price-discount">$${item.productDiscountPrice}</span>
                             <span class="price-original">$${item.productPrice}</span>` :
                            `<span class="price">$${item.productPrice}</span>`
                        }
                    </div>
                    <div class="wishlist-item-actions">
                        <button class="btn btn-primary add-to-cart-btn" data-product-id="${item.productId}">
                            Add to Cart
                        </button>
                        <button class="btn btn-outline remove-btn" data-product-id="${item.productId}">
                            Remove
                        </button>
                    </div>
                </div>
            </div>
        `).join('');

        this.attachItemEventListeners();
    }

    attachEventListeners() {
        // Clear all button
        const clearAllBtn = document.getElementById('clearAllBtn');
        if (clearAllBtn) {
            clearAllBtn.addEventListener('click', () => this.clearAll());
        }
    }

    attachItemEventListeners() {
        // Remove buttons
        document.querySelectorAll('.remove-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const productId = e.target.dataset.productId;
                this.removeItem(productId);
            });
        });

        // Add to cart buttons
        document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const productId = e.target.dataset.productId;
                this.addToCart(productId);
            });
        });
    }

    async removeItem(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/products/${productId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showSuccess('Removed from wishlist');
                await this.loadWishlist();
            }
        } catch (error) {
            console.error('Error removing item:', error);
            this.showError('Failed to remove item');
        }
    }

    async clearAll() {
        if (!confirm('Are you sure you want to clear your wishlist?')) {
            return;
        }

        try {
            const response = await fetch(this.baseUrl, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showSuccess('Wishlist cleared');
                await this.loadWishlist();
            }
        } catch (error) {
            console.error('Error clearing wishlist:', error);
            this.showError('Failed to clear wishlist');
        }
    }

    async addToCart(productId) {
        // Implement add to cart functionality
        // This should call your existing cart API
        console.log('Add to cart:', productId);
        this.showSuccess('Added to cart');
    }

    updateCount(count) {
        const countElement = document.querySelector('.wishlist-count');
        if (countElement) {
            countElement.textContent = `${count} item${count !== 1 ? 's' : ''}`;
        }

        // Update header badge
        const badge = document.querySelector('.wishlist-badge');
        if (badge) {
            badge.textContent = count;
            badge.style.display = count > 0 ? 'block' : 'none';
        }
    }

    showSuccess(message) {
        // Implement toast notification
        alert(message); // Replace with proper toast
    }

    showError(message) {
        // Implement error notification
        alert(message); // Replace with proper toast
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    new WishlistManager();
});
```

### 3. Wishlist Button Component (for product cards)

```html
<!-- Add this to product card -->
<button class="wishlist-btn" 
        data-product-id="${product.id}"
        onclick="toggleWishlist(${product.id}, this)">
    <i class="far fa-heart"></i>
</button>
```

```javascript
// Toggle wishlist function
async function toggleWishlist(productId, button) {
    try {
        button.disabled = true;
        button.classList.add('loading');

        const response = await fetch(`/api/wishlist/products/${productId}/toggle`, {
            method: 'POST'
        });

        const data = await response.json();

        // Update button state
        const icon = button.querySelector('i');
        if (data.inWishlist) {
            icon.classList.remove('far');
            icon.classList.add('fas');
            button.classList.add('active');
        } else {
            icon.classList.remove('fas');
            icon.classList.add('far');
            button.classList.remove('active');
        }

        // Update header count
        updateWishlistCount();

        // Show notification
        showToast(data.message);

    } catch (error) {
        console.error('Error toggling wishlist:', error);
        showToast('Error updating wishlist', 'error');
    } finally {
        button.disabled = false;
        button.classList.remove('loading');
    }
}

// Update wishlist count in header
async function updateWishlistCount() {
    try {
        const response = await fetch('/api/wishlist/count');
        const data = await response.json();
        
        const badge = document.querySelector('.wishlist-badge');
        if (badge) {
            badge.textContent = data.count;
            badge.style.display = data.count > 0 ? 'block' : 'none';
        }
    } catch (error) {
        console.error('Error updating count:', error);
    }
}
```

### 4. Header Wishlist Icon

```html
<!-- Add to header.html -->
<div class="header-actions">
    <!-- Existing cart icon -->
    <a href="/cart" class="header-icon">
        <i class="fas fa-shopping-cart"></i>
        <span class="badge cart-badge">0</span>
    </a>

    <!-- NEW: Wishlist icon -->
    <a href="/wishlist" class="header-icon">
        <i class="far fa-heart"></i>
        <span class="badge wishlist-badge" style="display: none;">0</span>
    </a>

    <!-- Existing user icon -->
    <a href="/profile" class="header-icon">
        <i class="fas fa-user"></i>
    </a>
</div>
```

### 5. CSS Styles

```css
/* Wishlist Button on Product Card */
.wishlist-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: white;
    border: none;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    z-index: 10;
}

.wishlist-btn i {
    font-size: 20px;
    color: #666;
    transition: color 0.3s ease;
}

.wishlist-btn:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.wishlist-btn.active i {
    color: #ff4757;
}

.wishlist-btn.loading {
    opacity: 0.6;
    cursor: not-allowed;
}

/* Wishlist Page */
.wishlist-page {
    padding: 40px 0;
    min-height: 60vh;
}

.wishlist-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
}

.wishlist-actions {
    display: flex;
    gap: 20px;
    align-items: center;
}

.wishlist-count {
    font-size: 16px;
    color: #666;
}

.wishlist-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
}

@media (max-width: 1024px) {
    .wishlist-grid {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media (max-width: 768px) {
    .wishlist-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 480px) {
    .wishlist-grid {
        grid-template-columns: 1fr;
    }
}

.wishlist-item {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    transition: transform 0.3s ease;
}

.wishlist-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}

.wishlist-item-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
}

.wishlist-item-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.wishlist-item-info {
    padding: 15px;
}

.wishlist-item-info h3 {
    font-size: 16px;
    margin-bottom: 10px;
    color: #333;
}

.wishlist-item-price {
    margin-bottom: 15px;
}

.price-discount {
    font-size: 18px;
    font-weight: bold;
    color: #ff4757;
    margin-right: 10px;
}

.price-original {
    font-size: 14px;
    color: #999;
    text-decoration: line-through;
}

.wishlist-item-actions {
    display: flex;
    gap: 10px;
}

.wishlist-item-actions button {
    flex: 1;
    padding: 10px;
    font-size: 14px;
}

/* Empty State */
.wishlist-empty {
    text-align: center;
    padding: 60px 20px;
}

.wishlist-empty img {
    width: 200px;
    margin-bottom: 20px;
    opacity: 0.5;
}

.wishlist-empty h2 {
    font-size: 24px;
    color: #333;
    margin-bottom: 10px;
}

.wishlist-empty p {
    font-size: 16px;
    color: #666;
    margin-bottom: 20px;
}

/* Header Badge */
.wishlist-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background: #ff4757;
    color: white;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 10px;
    min-width: 18px;
    text-align: center;
}
```

---

## 🔧 Controller Implementation / Triển Khai Controller

### Option 1: Add to HomeController

```java
@Controller
public class HomeController {
    
    // Existing methods...
    
    /**
     * Wishlist page
     */
    @GetMapping("/wishlist")
    public String wishlistPage(Model model, 
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        if (userDetails == null) {
            return "redirect:/sign-in";
        }
        
        model.addAttribute("pageTitle", "My Wishlist");
        return "wishlist";
    }
}
```

### Option 2: Create Dedicated Controller

```java
package poly.edu.java5_asm.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import poly.edu.java5_asm.security.CustomUserDetails;

/**
 * Controller for Wishlist page
 */
@Controller
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistPageController {

    @GetMapping
    public String wishlistPage(Model model, 
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        if (userDetails == null) {
            return "redirect:/sign-in";
        }
        
        model.addAttribute("pageTitle", "My Wishlist");
        model.addAttribute("userId", userDetails.getUserId());
        
        return "wishlist";
    }
}
```

---

## ✅ Testing Checklist / Danh Sách Kiểm Tra

### Functionality / Chức Năng

- [ ] Wishlist page loads correctly
- [ ] Empty state shows when no items
- [ ] Items display with correct information
- [ ] Remove button works
- [ ] Clear all button works
- [ ] Add to cart button works
- [ ] Wishlist icon in header shows correct count
- [ ] Wishlist button on product cards works
- [ ] Toggle functionality works (add/remove)
- [ ] Visual feedback on button click
- [ ] Loading states display correctly
- [ ] Error messages show when API fails
- [ ] Success messages show on actions

### Responsive Design / Thiết Kế Responsive

- [ ] Desktop (1920px+) - 4 columns
- [ ] Laptop (1024px-1919px) - 3 columns
- [ ] Tablet (768px-1023px) - 2 columns
- [ ] Mobile (< 768px) - 1 column
- [ ] Touch-friendly buttons on mobile
- [ ] Readable text on all screen sizes

### Performance / Hiệu Suất

- [ ] Page loads in < 2 seconds
- [ ] API calls complete in < 500ms
- [ ] No layout shift during load
- [ ] Smooth animations
- [ ] No memory leaks

### Accessibility / Khả Năng Truy Cập

- [ ] Keyboard navigation works
- [ ] Screen reader compatible
- [ ] Proper ARIA labels
- [ ] Focus indicators visible
- [ ] Color contrast meets WCAG standards

---

## 📚 Additional Resources / Tài Liệu Tham Khảo

### Existing Project Files to Reference

1. **Cart Page:** `src/main/resources/templates/cart.html`
   - Similar layout and functionality
   - Reuse styles and structure

2. **Product Cards:** `src/main/resources/templates/index.html`
   - Product card structure
   - Image and price display

3. **Header:** `src/main/resources/templates/fragments/header.html`
   - Icon placement
   - Badge implementation

4. **JavaScript:** `src/main/resources/static/assets/js/cart-api.js`
   - API call patterns
   - Error handling

### API Documentation

See: `WISHLIST_API_DOCUMENTATION.md` for complete API reference

### Design System

Follow existing design patterns:
- Colors from `src/main/resources/scss/theme/`
- Components from `src/main/resources/scss/components/`
- Layout from `src/main/resources/scss/layout/`

---

## 🚀 Implementation Steps / Các Bước Triển Khai

### Phase 1: Basic Structure (30 minutes)

1. Create `wishlist.html` template
2. Add controller method
3. Create basic layout (header, grid, empty state)
4. Test page loads

### Phase 2: JavaScript Integration (45 minutes)

1. Create `wishlist.js` file
2. Implement WishlistManager class
3. Add API calls (load, remove, clear)
4. Test functionality

### Phase 3: Product Card Integration (30 minutes)

1. Add wishlist button to product cards
2. Implement toggle functionality
3. Add visual feedback
4. Test on all pages (home, category, detail)

### Phase 4: Header Integration (15 minutes)

1. Add wishlist icon to header
2. Add badge with count
3. Update count dynamically
4. Test navigation

### Phase 5: Styling (45 minutes)

1. Add CSS/SCSS styles
2. Implement responsive design
3. Add animations and transitions
4. Test on different screen sizes

### Phase 6: Testing & Polish (30 minutes)

1. Test all functionality
2. Fix bugs
3. Add loading states
4. Add error handling
5. Test edge cases

**Total Time:** ~3 hours

---

## 🎯 Success Criteria / Tiêu Chí Thành Công

### Must Have / Bắt Buộc

✅ Wishlist page displays all items  
✅ Can add/remove items  
✅ Can clear all items  
✅ Header shows item count  
✅ Product cards have wishlist button  
✅ Responsive on all devices  
✅ No console errors  

### Nice to Have / Nên Có

✅ Smooth animations  
✅ Toast notifications  
✅ Loading states  
✅ Empty state illustration  
✅ Add to cart from wishlist  
✅ Keyboard shortcuts  

### Bonus / Thêm

✅ Share wishlist  
✅ Wishlist collections  
✅ Price drop notifications  
✅ Recently viewed integration  

---

## 📝 Notes / Ghi Chú

### Authentication / Xác Thực

- All wishlist features require login
- Redirect to `/sign-in` if not authenticated
- Show login prompt on wishlist button click if not logged in

### Data Persistence / Lưu Trữ Dữ Liệu

- Wishlist is stored in database (not localStorage)
- Synced across devices
- Persists after logout

### Browser Compatibility / Tương Thích Trình Duyệt

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

### Performance Optimization / Tối Ưu Hiệu Suất

- Lazy load images
- Debounce API calls
- Cache wishlist count
- Use CSS animations (not JavaScript)

---

**Created:** 2026-01-25  
**Version:** 1.0  
**Status:** Ready for Implementation  
**Estimated Time:** 3 hours
