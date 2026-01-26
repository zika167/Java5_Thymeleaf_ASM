package poly.edu.java5_asm.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Form Login Success Handler
 * Xử lý sau khi user đăng nhập thành công qua form:
 * 1. Tạo JWT token
 * 2. Lưu JWT vào HTTP-Only Cookie
 * 3. Redirect về trang chủ
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FormLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Form login success for user: {}", authentication.getName());

        // Clear any existing JWT cookie first to prevent duplicates
        clearExistingJwtCookie(response);

        // Generate new JWT token
        String jwt = jwtUtils.generateToken(authentication);

        // Create new HTTP-Only Cookie with JWT
        Cookie jwtCookie = createJwtCookie(jwt);
        response.addCookie(jwtCookie);

        log.info("JWT token created and stored in cookie for user: {}", authentication.getName());

        // Redirect to home page
        getRedirectStrategy().sendRedirect(request, response, "/");
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
