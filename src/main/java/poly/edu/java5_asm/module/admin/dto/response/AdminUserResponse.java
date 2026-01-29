package poly.edu.java5_asm.module.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO cho Admin User Management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String role;
    private Boolean active;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private Integer loginCount;
}
