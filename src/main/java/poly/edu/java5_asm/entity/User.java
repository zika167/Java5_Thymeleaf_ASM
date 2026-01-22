package poly.edu.java5_asm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity Người dùng
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // Tên đăng nhập

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Email

    @Column(length = 255)
    private String password; // Mật khẩu (đã mã hóa) - nullable cho OAuth2 users

    @Column(name = "full_name", length = 100)
    private String fullName; // Họ tên đầy đủ

    @Column(length = 20)
    private String phone; // Số điện thoại

    @Builder.Default
    @Column(name = "avatar_url", length = 255)
    private String avatarUrl = "/assets/img/avatar.jpg"; // Ảnh đại diện

    @Column(name = "registered_date")
    private LocalDate registeredDate; // Ngày đăng ký

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true; // Trạng thái kích hoạt

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Role role = Role.USER; // Vai trò (USER/ADMIN)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Ngày tạo

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Ngày cập nhật

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt; // Lần đăng nhập cuối

    @Builder.Default
    @Column(name = "login_count")
    private Integer loginCount = 0; // Số lần đăng nhập

    // OAuth2 fields
    @Column(name = "provider", length = 20)
    private String provider; // google, facebook, local

    @Column(name = "provider_id", length = 100)
    private String providerId; // ID từ OAuth2 provider

    // Quan hệ: 1 user có nhiều địa chỉ
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    // Quan hệ: 1 user có nhiều đánh giá
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Quan hệ: 1 user có nhiều giỏ hàng
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    // Quan hệ: 1 user có nhiều đơn hàng
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    // Quan hệ: 1 user có nhiều wishlist
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (registeredDate == null) {
            registeredDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Enum cho Role
    public enum Role {
        USER, ADMIN
    }
}
