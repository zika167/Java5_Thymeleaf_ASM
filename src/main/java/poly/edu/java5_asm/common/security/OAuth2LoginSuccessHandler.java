package poly.edu.java5_asm.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OAuth2 Login Success Handler
 * Xử lý sau khi user đăng nhập thành công qua Google:
 * 1. Lấy thông tin user từ Google
 * 2. Kiểm tra user đã tồn tại trong DB chưa
 * 3. Nếu chưa có -> Tạo user mới
 * 4. Tạo JWT token
 * 5. Lưu JWT vào HTTP-Only Cookie
 * 6. Redirect về trang chủ
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("=== OAuth2 Login Success Handler Started ===");
        log.info("Authentication class: {}", authentication.getClass().getName());
        log.info("Authentication principal class: {}", authentication.getPrincipal().getClass().getName());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2User attributes: {}", oAuth2User.getAttributes());

        // Lấy thông tin từ Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String googleId = oAuth2User.getAttribute("sub"); // Google User ID

        log.info("OAuth2 Login Success - Email: {}, Name: {}, GoogleId: {}", email, name, googleId);

        // Kiểm tra user đã tồn tại chưa
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Tạo user mới nếu chưa tồn tại
                    log.info("Creating new user from Google OAuth2: {}", email);

                    // Tạo random password cho OAuth2 user (không bao giờ dùng để login)
                    String randomPassword = UUID.randomUUID().toString();
                    String encodedPassword = passwordEncoder.encode(randomPassword);

                    User newUser = User.builder()
                            .email(email)
                            .username(email) // Sử dụng email làm username
                            .password(encodedPassword) // Set password để tránh lỗi NOT NULL
                            .fullName(name)
                            .avatarUrl(picture)
//                            .provider("google")
//                            .providerId(googleId)
                            .isActive(true)
                            .role(User.Role.USER)
                            .registeredDate(LocalDate.now())
                            .loginCount(0)
                            .build();
                    return userRepository.save(newUser);
                });

        // Cập nhật thông tin đăng nhập
        user.setLastLoginAt(LocalDateTime.now());
        user.setLoginCount(user.getLoginCount() + 1);
        userRepository.save(user);

        // Clear any existing JWT cookie first to prevent duplicates
        clearExistingJwtCookie(response);

        // Generate new JWT token
        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

        // Create new HTTP-Only Cookie with JWT
        Cookie jwtCookie = createJwtCookie(jwt);
        response.addCookie(jwtCookie);

        log.info("JWT token created and stored in cookie for user: {}", user.getUsername());
        log.info("Redirecting to home page: /");

        // Redirect to home page
        getRedirectStrategy().sendRedirect(request, response, "/");

        log.info("=== OAuth2 Login Success Handler Completed ===");
    }

    /**
     * Clear existing JWT cookie to prevent duplicates
     */
    private void clearExistingJwtCookie(HttpServletResponse response) {
        Cookie clearCookie = new Cookie(jwtCookieName, null);
        clearCookie.setPath("/");
        clearCookie.setMaxAge(0);
        clearCookie.setHttpOnly(true);
        response.addCookie(clearCookie);
    }

    /**
     * Create JWT cookie with proper security settings
     */
    private Cookie createJwtCookie(String jwt) {
        Cookie jwtCookie = new Cookie(jwtCookieName, jwt);
        jwtCookie.setHttpOnly(true); // Prevent XSS attacks
        jwtCookie.setSecure(false); // Set true for HTTPS in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtExpiration / 1000));
        return jwtCookie;
    }
}
