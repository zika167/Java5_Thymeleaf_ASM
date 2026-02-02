# ⚡ QUICK ADD: Tính Năng Xếp Hạng Sản Phẩm

## 🎯 MỤC ĐÍCH
Đếm số người yêu thích sản phẩm và xếp hạng sản phẩm theo độ phổ biến.

---

## 📝 5 BƯỚC NHANH

### 1. Tạo DTO
Copy file: `ProductRankingResponse.java` từ guide

### 2. WishlistRepository
Thêm 4 methods:
- `countByProductId()`
- `findMostWishlistedProducts()`
- `findMostWishlistedProductsWithDetails()`
- `findTrendingProducts()`

### 3. WishlistService
Thêm 3 methods:
- `getProductWishlistCount()`
- `getMostWishlistedProducts()`
- `getTrendingProducts()`

### 4. WishlistServiceImpl
Implement 3 methods trên

### 5. WishlistController
Thêm 3 endpoints:
- `GET /api/wishlist/products/{id}/wishlist-count`
- `GET /api/wishlist/most-wishlisted?limit=10`
- `GET /api/wishlist/trending?days=7&limit=10`

---

## 🚀 API ENDPOINTS

```bash
# Đếm số người wishlist
GET /api/wishlist/products/1/wishlist-count

# Top 10 sản phẩm
GET /api/wishlist/most-wishlisted?limit=10

# Trending 7 ngày
GET /api/wishlist/trending?days=7&limit=10
```

---

## 📄 CHI TIẾT

Xem file: `FEATURE_PRODUCT_RANKING_IMPLEMENTATION_GUIDE.md`

---

**COPY PASTE LÀ XONG!** ⚡
