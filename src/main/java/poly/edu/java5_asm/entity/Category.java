package poly.edu.java5_asm.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity Danh mục sản phẩm
 * Hỗ trợ cấu trúc cây (parent-child)
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Tên danh mục

    @Column(nullable = false, unique = true, length = 100)
    private String slug; // Đường dẫn URL thân thiện

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả

    @Column(name = "icon_url", length = 255)
    private String iconUrl; // Đường dẫn icon

    @Builder.Default
    @Column(name = "display_order")
    private Integer displayOrder = 0; // Thứ tự hiển thị

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true; // Trạng thái kích hoạt

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    // Quan hệ: Danh mục cha (tự tham chiếu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Quan hệ: 1 danh mục có nhiều danh mục con
    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subCategories = new ArrayList<>();

    // Quan hệ: 1 danh mục có nhiều sản phẩm
    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
