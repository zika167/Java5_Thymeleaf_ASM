package poly.edu.java5_asm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Filter này chạy trước mỗi request để:
 * 1. Trích xuất JWT token từ Cookie
 * 2. Validate token
 * 3. Set authentication vào SecurityContext
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Bỏ qua JWT validation cho static resources
        String requestPath = request.getRequestURI();
        if (isStaticResource(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Lấy JWT token từ Cookie
            String jwt = getJwtFromCookie(request);

            // Nếu có token và token hợp lệ
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                // Lấy username từ token
                String username = jwtUtils.getUsernameFromToken(jwt);

                // Load user details từ database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Tạo authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Set thêm thông tin request vào authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Set authentication for user: {}", username);
            }
        } catch (Exception ex) {
            log.error("Cannot set user authentication: {}", ex.getMessage());
        }

        // Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem request có phải là static resource không
     */
    private boolean isStaticResource(String requestPath) {
        return requestPath.startsWith("/assets/") ||
                requestPath.startsWith("/css/") ||
                requestPath.startsWith("/js/") ||
                requestPath.startsWith("/images/") ||
                requestPath.equals("/favicon.ico") ||
                requestPath.endsWith(".woff") ||
                requestPath.endsWith(".woff2") ||
                requestPath.endsWith(".ttf") ||
                requestPath.endsWith(".eot") ||
                requestPath.endsWith(".svg") ||
                requestPath.endsWith(".png") ||
                requestPath.endsWith(".jpg") ||
                requestPath.endsWith(".jpeg") ||
                requestPath.endsWith(".gif") ||
                requestPath.endsWith(".css") ||
                requestPath.endsWith(".js");
    }

    /**
     * Trích xuất JWT token từ Cookie
     */
    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
