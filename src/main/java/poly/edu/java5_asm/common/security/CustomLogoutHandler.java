package poly.edu.java5_asm.common.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Custom Logout Handler
 * Xử lý logout:
 * 1. Xóa JWT cookie
 * 2. Xóa user khỏi session
 * 3. Clear SecurityContext
 */
@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Processing logout for user: {}", 
            authentication != null ? authentication.getName() : "anonymous");

        // Xóa JWT cookie
        Cookie jwtCookie = new Cookie(jwtCookieName, null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);

        // Xóa user khỏi session
        if (request.getSession(false) != null) {
            request.getSession().removeAttribute("user");
            request.getSession().invalidate();
        }

        // Clear SecurityContext
        SecurityContextHolder.clearContext();

        log.info("Logout completed successfully");
    }
}
