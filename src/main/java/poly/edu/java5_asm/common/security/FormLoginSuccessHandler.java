package poly.edu.java5_asm.common.security;

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
import poly.edu.java5_asm.module.auth.service.AuthService;

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
    private final AuthService authService;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        log.info("Form login success for user: {}", authentication.getName());

        // Update last login time and login count
        authService.updateLastLogin(authentication.getName());
        log.info("Updated login count for user: {}", authentication.getName());

        // Check if "remember-me" checkbox was checked
        String rememberMe = request.getParameter("remember-me");
        boolean isRememberMe = "on".equals(rememberMe) || "true".equals(rememberMe);
        
        log.info("Remember me: {}", isRememberMe);

        // Clear any existing JWT cookie first to prevent duplicates
        clearExistingJwtCookie(response);

        // Generate new JWT token
        String jwt = jwtUtils.generateToken(authentication);

        // Create new HTTP-Only Cookie with JWT
        // If remember-me is checked, cookie lasts for JWT expiration time
        // If not checked, cookie is session-only (deleted when browser closes)
        Cookie jwtCookie = createJwtCookie(jwt, isRememberMe);
        response.addCookie(jwtCookie);

        log.info("JWT token created and stored in cookie for user: {} (remember-me: {})", 
                authentication.getName(), isRememberMe);

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
     * @param jwt JWT token
     * @param rememberMe if true, cookie persists; if false, session-only cookie
     */
    private Cookie createJwtCookie(String jwt, boolean rememberMe) {
        Cookie jwtCookie = new Cookie(jwtCookieName, jwt);
        jwtCookie.setHttpOnly(true); // Prevent XSS attacks
        jwtCookie.setSecure(false); // Set true for HTTPS in production
        jwtCookie.setPath("/");
        
        if (rememberMe) {
            // Persistent cookie - lasts for JWT expiration time (e.g., 7 days)
            jwtCookie.setMaxAge((int) (jwtExpiration / 1000));
        } else {
            // Session cookie - deleted when browser closes
            // MaxAge = -1 means session cookie
            jwtCookie.setMaxAge(-1);
        }
        
        jwtCookie.setAttribute("SameSite", "Lax"); // CSRF protection
        return jwtCookie;
    }
}
