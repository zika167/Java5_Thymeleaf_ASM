package poly.edu.java5_asm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Sản phẩm
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name; // Tên sản phẩm

    @Column(nullable = false, unique = true, length = 255)
    private String slug; // Đường dẫn URL thân thiện

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả chi tiết

    @Column(name = "short_description", length = 500)
    private String shortDescription; // Mô tả ngắn

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Giá gốc

    @Column(name = "discount_price", precision = 10, scale = 2)
    private BigDecimal discountPrice; // Giá giảm

    @Builder.Default
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0; // Số lượng tồn kho

    @Builder.Default
    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 10; // Ngưỡng cảnh báo hết hàng

    @Builder.Default
    @Column(name = "is_out_of_stock")
    private Boolean isOutOfStock = false; // Trạng thái hết hàng

    @Builder.Default
    @Column(name = "image_url", length = 255)
    private String imageUrl = "/assets/img/products/default.jpg"; // Đường dẫn hình ảnh

    @Column(name = "search_keywords", columnDefinition = "TEXT")
    private String searchKeywords; // Từ khóa tìm kiếm

    @Column(length = 500)
    private String tags; // Tags phân loại

    @Column(unique = true, length = 50)
    private String sku; // Mã SKU

    @Column(length = 50)
    private String weight; // Trọng lượng

    @Builder.Default
    @Column(name = "is_featured")
    private Boolean isFeatured = false; // Sản phẩm nổi bật

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true; // Trạng thái kích hoạt

    @Builder.Default
    @Column(name = "view_count")
    private Integer viewCount = 0; // Số lượt xem

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Ngày cập nhật

    // Quan hệ: Nhiều sản phẩm thuộc 1 danh mục
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Quan hệ: Nhiều sản phẩm thuộc 1 thương hiệu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // Quan hệ: 1 sản phẩm có nhiều đánh giá
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Quan hệ: 1 sản phẩm có nhiều wishlist
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists = new ArrayList<>();

    // Quan hệ: 1 sản phẩm có nhiều cart items
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    // Quan hệ: 1 sản phẩm có nhiều order items
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
