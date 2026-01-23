package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.ProfileUpdateRequest;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.UserRepository;

/**
 * Service xử lý các nghiệp vụ liên quan đến User.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lấy thông tin user theo username.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    /**
     * Lấy thông tin user theo ID.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    /**
     * Cập nhật thông tin cá nhân của user.
     */
    @Transactional
    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = findById(userId);

        // Kiểm tra email đã được sử dụng bởi user khác chưa
        if (!user.getEmail().equals(request.getEmail()) 
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng bởi tài khoản khác");
        }

        // Cập nhật thông tin cơ bản
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Cập nhật password nếu có nhập mới
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("Mật khẩu xác nhận không khớp");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.save(user);
    }
}
