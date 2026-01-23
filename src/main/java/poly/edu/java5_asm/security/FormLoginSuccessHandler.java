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
        // Tạo JWT token
        String jwt = jwtUtils.generateToken(authentication);

        // Tạo HTTP-Only Cookie chứa JWT
        Cookie jwtCookie = new Cookie(jwtCookieName, jwt);
        jwtCookie.setHttpOnly(true); // Bảo mật: không thể truy cập từ JavaScript
        jwtCookie.setSecure(false); // Set true nếu dùng HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtExpiration / 1000)); // Convert milliseconds to seconds

        response.addCookie(jwtCookie);

        log.info("JWT token created and stored in cookie for user: {}", authentication.getName());

        // Redirect về trang chủ
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
