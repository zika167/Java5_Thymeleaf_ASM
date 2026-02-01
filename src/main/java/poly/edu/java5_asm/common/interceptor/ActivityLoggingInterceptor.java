package poly.edu.java5_asm.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.entity.UserActivityLog;
import poly.edu.java5_asm.module.user.repository.UserActivityLogRepository;
import poly.edu.java5_asm.module.user.repository.UserRepository;

/**
 * Interceptor để log hoạt động của user
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityLoggingInterceptor implements HandlerInterceptor {

    private final UserActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // Chỉ log GET requests (page views)
            if (!"GET".equalsIgnoreCase(request.getMethod())) {
                return true;
            }

            // Bỏ qua static resources và API calls
            String uri = request.getRequestURI();
            if (uri.startsWith("/assets/") || 
                uri.startsWith("/api/") || 
                uri.startsWith("/css/") || 
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.endsWith(".css") ||
                uri.endsWith(".js") ||
                uri.endsWith(".png") ||
                uri.endsWith(".jpg") ||
                uri.endsWith(".svg") ||
                uri.endsWith(".ico")) {
                return true;
            }

            // Lấy session ID
            HttpSession session = request.getSession(true);
            String sessionId = session.getId();

            // Lấy thông tin user (nếu đã đăng nhập)
            User user = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                user = userRepository.findByUsername(username).orElse(null);
            }

            // Tạo log
            UserActivityLog activityLog = UserActivityLog.builder()
                    .sessionId(sessionId)
                    .activityType(UserActivityLog.ActivityType.PAGE_VIEW)
                    .ipAddress(getClientIp(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .pageUrl(uri)
                    .user(user)
                    .build();

            activityLogRepository.save(activityLog);
            
            log.debug("Activity logged: {} - {} - {}", sessionId, uri, user != null ? user.getUsername() : "guest");

        } catch (Exception e) {
            log.error("Error logging activity", e);
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Lấy IP đầu tiên nếu có nhiều
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
