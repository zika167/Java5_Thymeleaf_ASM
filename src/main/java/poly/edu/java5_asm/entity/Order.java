package poly.edu.java5_asm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Đơn hàng
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber; // Mã đơn hàng (VD: ORD-20260115-001)

    // Thông tin giao hàng
    @Column(name = "shipping_method", length = 100)
    private String shippingMethod; // Phương thức giao hàng

    @Builder.Default
    @Column(name = "shipping_fee", precision = 10, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO; // Phí giao hàng

    // Thông tin thanh toán
    @Builder.Default
    @Column(name = "payment_method", length = 50)
    private String paymentMethod = "COD"; // Phương thức thanh toán (COD, VNPay, Momo...)

    @Column(name = "payment_transaction_id", length = 255)
    private String paymentTransactionId; // Mã giao dịch thanh toán

    @Column(name = "payment_gateway_response", columnDefinition = "TEXT")
    private String paymentGatewayResponse; // Response từ payment gateway

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING; // Trạng thái thanh toán

    // Tổng tiền
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Tổng tiền hàng

    @Builder.Default
    @Column(precision = 10, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO; // Thuế

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // Tổng cộng

    // Trạng thái đơn hàng
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status = OrderStatus.PENDING; // Trạng thái đơn hàng

    // Ghi chú
    @Column(name = "customer_note", columnDefinition = "TEXT")
    private String customerNote; // Ghi chú của khách hàng

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote; // Ghi chú của admin

    // Timestamps
    @Column(name = "ordered_at")
    private LocalDateTime orderedAt; // Thời gian đặt hàng

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt; // Thời gian xác nhận

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt; // Thời gian giao hàng

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt; // Thời gian hoàn thành

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt; // Thời gian hủy

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Ngày cập nhật

    // Quan hệ: Nhiều đơn hàng thuộc 1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ: Nhiều đơn hàng có 1 địa chỉ giao hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    // Quan hệ: 1 đơn hàng có nhiều order items
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderedAt == null) {
            orderedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Enum cho Payment Status
    public enum PaymentStatus {
        PENDING,    // Chờ thanh toán
        PAID,       // Đã thanh toán
        FAILED,     // Thanh toán thất bại
        REFUNDED    // Đã hoàn tiền
    }

    // Enum cho Order Status
    public enum OrderStatus {
        PENDING,    // Chờ xác nhận
        CONFIRMED,  // Đã xác nhận
        PROCESSING, // Đang xử lý
        SHIPPED,    // Đang giao hàng
        DELIVERED,  // Đã giao hàng
        CANCELLED   // Đã hủy
    }
}
