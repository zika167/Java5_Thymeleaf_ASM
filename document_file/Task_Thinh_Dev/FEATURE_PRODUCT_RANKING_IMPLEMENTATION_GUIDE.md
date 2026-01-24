# 🚀 HƯỚNG DẪN IMPLEMENT TÍNH NĂNG XẾP HẠNG SẢN PHẨM

## 📋 TỔNG QUAN

Tính năng này cho phép:
- ✅ Đếm số người đã yêu thích sản phẩm
- ✅ Xếp hạng sản phẩm theo số lượt wishlist
- ✅ Hiển thị "Top 10 sản phẩm được yêu thích nhất"
- ✅ Hiển thị "Sản phẩm trending" (hot trong 7 ngày gần đây)
- ✅ Hiển thị "X người đã yêu thích sản phẩm này"

---

## 🎯 USE CASES

### 1. Product Detail Page
```
Hiển thị: "1,234 người đã yêu thích sản phẩm này"
```

### 2. Home Page - Trending Section
```
Sản Phẩm Đang Hot:
1. Coffee Beans A - 5,432 lượt thích
2. Coffee Beans B - 4,321 lượt thích
3. Coffee Beans C - 3,210 lượt thích
```

### 3. Admin Dashboard
```
Top 10 Sản Phẩm Được Yêu Thích Nhất
- Analytics và insights
```

---

## 📝 IMPLEMENTATION STEPS

### BƯỚC 1: Tạo DTO

**File**: `src/main/java/poly/edu/java5_asm/dto/response/ProductRankingResponse.java`

```java
package poly.edu.java5_asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho Product Ranking (sản phẩm được yêu thích nhiều nhất)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRankingResponse {
    
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImageUrl;
    private Long wishlistCount;      // Số lượt wishlist
    private Integer rank;            // Thứ hạng (1, 2, 3, ...)
}
```

---

### BƯỚC 2: Thêm Methods Vào WishlistRepository

**File**: `src/main/java/poly/edu/java5_asm/repository/WishlistRepository.java`

**Thêm vào cuối interface (trước dấu `}`)**:

```java
    // ========== PRODUCT RANKING & ANALYTICS ==========

    /**
     * Đếm số người đã yêu thích sản phẩm này
     * Use case: Hiển thị "X người đã yêu thích", Product analytics
     */
    long countByProductId(Long productId);

    /**
     * Lấy top sản phẩm được yêu thích nhiều nhất
     * Use case: Trending products, Most popular products
     * 
     * @param pageable Pagination (ví dụ: PageRequest.of(0, 10) để lấy top 10)
     * @return List of [productId, wishlistCount]
     */
    @Query("SELECT w.product.id as productId, COUNT(w) as wishlistCount " +
           "FROM Wishlist w " +
           "GROUP BY w.product.id " +
           "ORDER BY COUNT(w) DESC")
    List<Object[]> findMostWishlistedProducts(Pageable pageable);

    /**
     * Lấy top sản phẩm được yêu thích nhiều nhất với thông tin đầy đủ
     * Use case: Trending section với product details
     */
    @Query("SELECT w.product.id as productId, " +
           "w.product.name as productName, " +
           "w.product.price as productPrice, " +
           "w.product.imageUrl as productImage, " +
           "COUNT(w) as wishlistCount " +
           "FROM Wishlist w " +
           "GROUP BY w.product.id, w.product.name, w.product.price, w.product.imageUrl " +
           "ORDER BY COUNT(w) DESC")
    List<Object[]> findMostWishlistedProductsWithDetails(Pageable pageable);

    /**
     * Lấy sản phẩm trending (được thêm vào wishlist nhiều trong khoảng thời gian gần đây)
     * Use case: Hot products, Trending now
     * 
     * @param days Số ngày gần đây (ví dụ: 7 cho 1 tuần)
     */
    @Query("SELECT w.product.id as productId, COUNT(w) as wishlistCount " +
           "FROM Wishlist w " +
           "WHERE w.createdAt >= CURRENT_TIMESTAMP - :days DAY " +
           "GROUP BY w.product.id " +
           "ORDER BY COUNT(w) DESC")
    List<Object[]> findTrendingProducts(@Param("days") int days, Pageable pageable);
```

---

### BƯỚC 3: Thêm Methods Vào WishlistService Interface

**File**: `src/main/java/poly/edu/java5_asm/service/WishlistService.java`

**Thêm vào cuối interface (trước dấu `}`)**:

```java
    // ========== PRODUCT RANKING & ANALYTICS ==========

    /**
     * Đếm số người đã yêu thích sản phẩm
     * 
     * @param productId ID sản phẩm
     * @return Số lượng người đã wishlist
     */
    long getProductWishlistCount(Long productId);

    /**
     * Lấy top sản phẩm được yêu thích nhiều nhất
     * 
     * @param limit Số lượng sản phẩm (ví dụ: 10 cho top 10)
     * @return List of ProductRankingResponse
     */
    List<ProductRankingResponse> getMostWishlistedProducts(int limit);

    /**
     * Lấy sản phẩm trending (hot trong thời gian gần đây)
     * 
     * @param days Số ngày gần đây (ví dụ: 7 cho 1 tuần)
     * @param limit Số lượng sản phẩm
     * @return List of ProductRankingResponse
     */
    List<ProductRankingResponse> getTrendingProducts(int days, int limit);
```

---

### BƯỚC 4: Implement Trong WishlistServiceImpl

**File**: `src/main/java/poly/edu/java5_asm/service/impl/WishlistServiceImpl.java`

**Thêm vào cuối class (trước dấu `}` cuối cùng)**:

```java
    // ========== PRODUCT RANKING & ANALYTICS ==========

    /**
     * Đếm số người đã yêu thích sản phẩm
     */
    @Override
    public long getProductWishlistCount(Long productId) {
        return wishlistRepository.countByProductId(productId);
    }

    /**
     * Lấy top sản phẩm được yêu thích nhiều nhất
     */
    @Override
    public List<ProductRankingResponse> getMostWishlistedProducts(int limit) {
        log.info("Getting top {} most wishlisted products", limit);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = wishlistRepository.findMostWishlistedProductsWithDetails(pageable);
        
        List<ProductRankingResponse> rankings = new ArrayList<>();
        int rank = 1;
        
        for (Object[] row : results) {
            ProductRankingResponse ranking = ProductRankingResponse.builder()
                    .productId((Long) row[0])
                    .productName((String) row[1])
                    .productPrice((Double) row[2])
                    .productImageUrl((String) row[3])
                    .wishlistCount((Long) row[4])
                    .rank(rank++)
                    .build();
            rankings.add(ranking);
        }
        
        log.info("Found {} most wishlisted products", rankings.size());
        return rankings;
    }

    /**
     * Lấy sản phẩm trending (hot trong thời gian gần đây)
     */
    @Override
    public List<ProductRankingResponse> getTrendingProducts(int days, int limit) {
        log.info("Getting trending products in last {} days (limit: {})", days, limit);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = wishlistRepository.findTrendingProducts(days, pageable);
        
        List<ProductRankingResponse> rankings = new ArrayList<>();
        int rank = 1;
        
        for (Object[] row : results) {
            Long productId = (Long) row[0];
            Long wishlistCount = (Long) row[1];
            
            // Load product details
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                ProductRankingResponse ranking = ProductRankingResponse.builder()
                        .productId(productId)
                        .productName(product.getName())
                        .productPrice(product.getPrice())
                        .productImageUrl(product.getImageUrl())
                        .wishlistCount(wishlistCount)
                        .rank(rank++)
                        .build();
                rankings.add(ranking);
            }
        }
        
        log.info("Found {} trending products", rankings.size());
        return rankings;
    }
```

---

### BƯỚC 5: Thêm API Endpoints Vào WishlistController

**File**: `src/main/java/poly/edu/java5_asm/controller/WishlistController.java`

**Thêm vào cuối class (trước dấu `}` cuối cùng)**:

```java
    // ========== PRODUCT RANKING & ANALYTICS ==========

    /**
     * Lấy số người đã yêu thích sản phẩm
     * GET /api/wishlist/products/{productId}/wishlist-count
     */
    @GetMapping("/products/{productId}/wishlist-count")
    public ResponseEntity<?> getProductWishlistCount(@PathVariable Long productId) {
        log.debug("Getting wishlist count for product {}", productId);
        
        long count = wishlistService.getProductWishlistCount(productId);
        
        return ResponseEntity.ok(Map.of(
                "productId", productId,
                "wishlistCount", count,
                "message", count + " người đã yêu thích sản phẩm này"
        ));
    }

    /**
     * Lấy top sản phẩm được yêu thích nhiều nhất
     * GET /api/wishlist/most-wishlisted?limit=10
     */
    @GetMapping("/most-wishlisted")
    public ResponseEntity<?> getMostWishlistedProducts(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Getting top {} most wishlisted products", limit);
        
        var products = wishlistService.getMostWishlistedProducts(limit);
        
        return ResponseEntity.ok(Map.of(
                "message", "Top " + limit + " sản phẩm được yêu thích nhiều nhất",
                "products", products
        ));
    }

    /**
     * Lấy sản phẩm trending (hot trong thời gian gần đây)
     * GET /api/wishlist/trending?days=7&limit=10
     */
    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingProducts(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Getting trending products in last {} days (limit: {})", days, limit);
        
        var products = wishlistService.getTrendingProducts(days, limit);
        
        return ResponseEntity.ok(Map.of(
                "message", "Sản phẩm hot trong " + days + " ngày qua",
                "days", days,
                "products", products
        ));
    }
```

---

## 🧪 TESTING

### Test API Endpoints

#### 1. Đếm số người wishlist sản phẩm
```bash
GET http://localhost:8080/api/wishlist/products/1/wishlist-count

Response:
{
  "productId": 1,
  "wishlistCount": 234,
  "message": "234 người đã yêu thích sản phẩm này"
}
```

#### 2. Top 10 sản phẩm được yêu thích nhất
```bash
GET http://localhost:8080/api/wishlist/most-wishlisted?limit=10

Response:
{
  "message": "Top 10 sản phẩm được yêu thích nhiều nhất",
  "products": [
    {
      "productId": 1,
      "productName": "Coffee Beans A",
      "productPrice": 47.00,
      "productImageUrl": "/assets/img/product/item-1.png",
      "wishlistCount": 5432,
      "rank": 1
    },
    ...
  ]
}
```

#### 3. Sản phẩm trending (7 ngày gần đây)
```bash
GET http://localhost:8080/api/wishlist/trending?days=7&limit=10

Response:
{
  "message": "Sản phẩm hot trong 7 ngày qua",
  "days": 7,
  "products": [
    {
      "productId": 5,
      "productName": "Coffee Beans Hot",
      "productPrice": 53.00,
      "productImageUrl": "/assets/img/product/item-5.png",
      "wishlistCount": 432,
      "rank": 1
    },
    ...
  ]
}
```

---

## 🎨 FRONTEND INTEGRATION

### 1. Product Detail Page - Hiển thị số lượt wishlist

```javascript
// Lấy wishlist count cho sản phẩm
async function loadProductWishlistCount(productId) {
    const response = await fetch(`/api/wishlist/products/${productId}/wishlist-count`);
    const data = await response.json();
    
    // Hiển thị trên UI
    document.getElementById('wishlist-count').textContent = 
        `${data.wishlistCount} người đã yêu thích`;
}
```

```html
<!-- Product Detail Page -->
<div class="product-stats">
    <i class="heart-icon"></i>
    <span id="wishlist-count">Loading...</span>
</div>
```

### 2. Home Page - Trending Section

```javascript
// Lấy top 10 sản phẩm trending
async function loadTrendingProducts() {
    const response = await fetch('/api/wishlist/trending?days=7&limit=10');
    const data = await response.json();
    
    const container = document.getElementById('trending-products');
    data.products.forEach(product => {
        container.innerHTML += `
            <div class="product-card">
                <img src="${product.productImageUrl}" alt="${product.productName}">
                <h3>${product.productName}</h3>
                <p>$${product.productPrice}</p>
                <span class="badge">#${product.rank} Hot</span>
                <span class="wishlist-count">
                    <i class="heart"></i> ${product.wishlistCount}
                </span>
            </div>
        `;
    });
}
```

### 3. Admin Dashboard - Analytics

```javascript
// Lấy top sản phẩm được yêu thích nhất
async function loadMostWishlistedProducts() {
    const response = await fetch('/api/wishlist/most-wishlisted?limit=20');
    const data = await response.json();
    
    // Hiển thị chart hoặc table
    renderChart(data.products);
}
```

---

## 📊 SQL QUERIES GENERATED

### countByProductId()
```sql
SELECT COUNT(*) 
FROM wishlists 
WHERE product_id = ?
```

### findMostWishlistedProducts()
```sql
SELECT 
    w.product_id as productId,
    p.name as productName,
    p.price as productPrice,
    p.image_url as productImage,
    COUNT(w.id) as wishlistCount
FROM wishlists w
INNER JOIN products p ON w.product_id = p.id
GROUP BY w.product_id, p.name, p.price, p.image_url
ORDER BY COUNT(w.id) DESC
LIMIT ?
```

### findTrendingProducts()
```sql
SELECT 
    w.product_id as productId,
    COUNT(w.id) as wishlistCount
FROM wishlists w
WHERE w.created_at >= CURRENT_TIMESTAMP - INTERVAL ? DAY
GROUP BY w.product_id
ORDER BY COUNT(w.id) DESC
LIMIT ?
```

---

## ✅ CHECKLIST IMPLEMENTATION

- [ ] Tạo ProductRankingResponse DTO
- [ ] Thêm methods vào WishlistRepository
- [ ] Thêm methods vào WishlistService interface
- [ ] Implement trong WishlistServiceImpl
- [ ] Thêm API endpoints vào WishlistController
- [ ] Test API với Postman
- [ ] Integrate vào Frontend
- [ ] Test UI

---

## 🚀 KHI NÀO NÊN IMPLEMENT?

✅ **NÊN IMPLEMENT** khi:
- Cần hiển thị "X người đã yêu thích"
- Cần section "Trending Products"
- Cần Admin Analytics Dashboard
- Cần xếp hạng sản phẩm theo độ phổ biến

❌ **CHƯA CẦN** khi:
- Chỉ có basic wishlist functionality
- Chưa có UI design cho features này
- Chưa có business requirement

---

## 📝 NOTES

- Tất cả code đã được tối ưu performance
- Sử dụng pagination để tránh load quá nhiều data
- Có thể cache kết quả để tăng performance
- Có thể thêm filter theo category, brand, etc.

---

**KHI CẦN DÙNG, CHỈ CẦN COPY PASTE CODE TỪ FILE NÀY!** 🚀
