package poly.edu.java5_asm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity Nhật ký hoạt động người dùng
 * Dùng để theo dõi hành vi người dùng (traffic monitoring)
 */
@Entity
@Table(name = "user_activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", length = 255)
    private String sessionId; // Session ID

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 20)
    private ActivityType activityType; // Loại hoạt động

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // Địa chỉ IP

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent; // Thông tin trình duyệt

    @Column(name = "page_url", length = 500)
    private String pageUrl; // URL trang

    @Column(columnDefinition = "JSON")
    private String metadata; // Dữ liệu bổ sung (JSON format)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Thời gian hoạt động

    // Quan hệ: Nhiều logs thuộc 1 user (nullable cho guest)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Enum cho Activity Type
    public enum ActivityType {
        LOGIN,          // Đăng nhập
        LOGOUT,         // Đăng xuất
        PAGE_VIEW,      // Xem trang
        PRODUCT_VIEW,   // Xem sản phẩm
        SEARCH,         // Tìm kiếm
        ADD_TO_CART,    // Thêm vào giỏ hàng
        CHECKOUT        // Thanh toán
    }
}
