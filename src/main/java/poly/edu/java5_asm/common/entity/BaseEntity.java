package poly.edu.java5_asm.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Entity - Lớp cha chung cho tất cả Entity trong hệ thống
 * 
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║                    TẠI SAO CẦN BASE ENTITY?                      ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║ 1. DRY (Don't Repeat Yourself):                                  ║
 * ║    - Tránh viết lại id, createdAt, updatedAt trong mỗi entity    ║
 * ║    - Thay đổi 1 chỗ → áp dụng cho tất cả                         ║
 * ║                                                                  ║
 * ║ 2. Consistency (Nhất quán):                                      ║
 * ║    - Tất cả entity đều có cùng cấu trúc cơ bản                   ║
 * ║    - Dễ dàng query, audit, debug                                 ║
 * ║                                                                  ║
 * ║ 3. Maintainability (Dễ bảo trì):                                 ║
 * ║    - Thêm field mới (ví dụ: version) chỉ cần sửa 1 file          ║
 * ║    - Giảm thiểu lỗi khi copy-paste                               ║
 * ╚══════════════════════════════════════════════════════════════════╝
 * 
 * CÁCH SỬ DỤNG:
 * ```java
 * @Entity
 * public class Product extends BaseEntity {
 *     // Không cần khai báo id, createdAt, updatedAt nữa
 *     private String name;
 *     private BigDecimal price;
 * }
 * ```
 */
@MappedSuperclass  // Đánh dấu đây là lớp cha, không tạo table riêng
@EntityListeners(AuditingEntityListener.class)  // Tự động cập nhật audit fields
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    /**
     * ID - Khóa chính tự động tăng
     * Tất cả entity đều có ID kiểu Long
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Thời gian tạo record
     * - Tự động set khi INSERT
     * - Không thể update sau khi tạo (updatable = false)
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Thời gian cập nhật cuối cùng
     * - Tự động update mỗi khi entity thay đổi
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Callback trước khi persist (INSERT)
     * Đảm bảo createdAt và updatedAt được set
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    /**
     * Callback trước khi update
     * Tự động cập nhật updatedAt
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Kiểm tra entity đã được persist chưa
     * @return true nếu đã có ID (đã lưu vào DB)
     */
    public boolean isNew() {
        return id == null;
    }
}
