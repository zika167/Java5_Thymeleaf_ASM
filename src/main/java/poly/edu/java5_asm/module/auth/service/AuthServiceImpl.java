package poly.edu.java5_asm.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.exception.AuthException;
import poly.edu.java5_asm.module.auth.dto.request.RegisterRequest;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Implementation của AuthService
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw AuthException.usernameExists();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw AuthException.emailExists();
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw AuthException.passwordMismatch();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(User.Role.USER)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);
        });
    }
}
