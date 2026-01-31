package poly.edu.java5_asm.module.order.entity;

import jakarta.persistence.*;
import lombok.*;
import poly.edu.java5_asm.module.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity Chi tiết đơn hàng
 */
@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_item_order", columnList = "order_id"),
    @Index(name = "idx_order_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName; // Tên sản phẩm (lưu lại để tránh thay đổi)

    @Column(nullable = false)
    private Integer quantity; // Số lượng

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // Giá đơn vị

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Thành tiền (quantity * unitPrice)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    // Quan hệ: Nhiều order items thuộc 1 đơn hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Quan hệ: Nhiều order items thuộc 1 sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Tự động tính subtotal nếu chưa có
        if (subtotal == null && unitPrice != null && quantity != null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
