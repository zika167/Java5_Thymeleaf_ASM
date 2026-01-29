package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.admin.dto.response.AdminUserResponse;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller cho Admin User Management
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;

    /**
     * Lấy danh sách users với phân trang và filter
     */
    @GetMapping
    public ResponseEntity<Page<AdminUserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<User> users = userRepository.findAll(pageable);
        
        Page<AdminUserResponse> response = users.map(user -> AdminUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .active(user.getIsActive())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .loginCount(user.getLoginCount())
                .build());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy chi tiết user
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        AdminUserResponse response = AdminUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .active(user.getIsActive())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .loginCount(user.getLoginCount())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Khóa/Mở khóa user
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleUserStatus(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", user.getIsActive() ? "Đã mở khóa user" : "Đã khóa user");
        response.put("active", user.getIsActive());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Thay đổi role user
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> changeUserRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        user.setRole(User.Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đã cập nhật role thành " + role);
        response.put("role", user.getRole().name());
        
        return ResponseEntity.ok(response);
    }
}
