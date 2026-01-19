package poly.edu.java5_asm.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity Thương hiệu
 */
@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Tên thương hiệu

    @Column(nullable = false, unique = true, length = 100)
    private String slug; // Đường dẫn URL thân thiện

    @Column(name = "logo_url", length = 255)
    private String logoUrl; // Đường dẫn logo

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true; // Trạng thái kích hoạt

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    // Quan hệ: 1 thương hiệu có nhiều sản phẩm
    @Builder.Default
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
