package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.RegisterRequest;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Service xử lý các nghiệp vụ liên quan đến xác thực (Authentication).
 * Bao gồm: đăng ký tài khoản mới, cập nhật thông tin đăng nhập.
 *
 * @Service: Đánh dấu class này là một Spring Service Bean
 * @RequiredArgsConstructor: Lombok tự động tạo constructor với các field final
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Repository để thao tác với bảng users trong database
     */
    private final UserRepository userRepository;

    /**
     * Encoder để mã hóa password (BCrypt)
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Đăng ký tài khoản người dùng mới.
     * <p>
     * Quy trình xử lý:
     * 1. Kiểm tra username đã tồn tại chưa
     * 2. Kiểm tra email đã được sử dụng chưa
     * 3. Kiểm tra password và confirmPassword có khớp không
     * 4. Tạo User entity mới với role mặc định là USER
     * 5. Mã hóa password trước khi lưu vào database
     *
     * @param request DTO chứa thông tin đăng ký từ form
     * @return User entity đã được lưu vào database
     * @throws RuntimeException nếu validation thất bại
     * @Transactional: Đảm bảo toàn bộ method chạy trong 1 transaction,
     * rollback nếu có exception xảy ra
     */
    @Transactional
    public User register(RegisterRequest request) {
        // Kiểm tra trùng username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        // Kiểm tra trùng email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Kiểm tra password xác nhận
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password xác nhận không khớp");
        }

        // Tạo User entity mới sử dụng Builder pattern
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa password
                .fullName(request.getFullName())
                .role(User.Role.USER)  // Role mặc định cho user mới
                .isActive(true)        // Kích hoạt tài khoản ngay
                .build();

        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin lần đăng nhập cuối cùng của user.
     * Được gọi sau khi user đăng nhập thành công.
     * <p>
     * Cập nhật:
     * - lastLoginAt: Thời điểm đăng nhập
     * - loginCount: Tăng số lần đăng nhập lên 1
     *
     * @param username Username của user vừa đăng nhập
     * @Transactional: Đảm bảo cập nhật được thực hiện trong transaction
     */
    @Transactional
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);
        });
    }
}
