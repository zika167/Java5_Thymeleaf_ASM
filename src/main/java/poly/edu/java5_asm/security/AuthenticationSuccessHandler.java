package poly.edu.java5_asm.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import poly.edu.java5_asm.service.AuthService;

import java.io.IOException;

/**
 * Custom handler được gọi khi user đăng nhập thành công.
 * 
 * Kế thừa SavedRequestAwareAuthenticationSuccessHandler để:
 * - Giữ nguyên behavior mặc định (redirect về trang user muốn truy cập trước đó)
 * - Thêm logic custom: cập nhật thời gian đăng nhập cuối
 * 
 * Handler này được cấu hình trong SecurityConfig:
 * .formLogin().successHandler(authenticationSuccessHandler)
 * 
 * @Component: Đánh dấu là Spring bean để inject vào SecurityConfig
 * @RequiredArgsConstructor: Lombok tự động tạo constructor với các field final
 */
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * Service xử lý nghiệp vụ xác thực, dùng để cập nhật lastLogin
     */
    private final AuthService authService;

    /**
     * Method được Spring Security gọi khi xác thực thành công.
     * 
     * Quy trình xử lý:
     * 1. Cập nhật thời gian đăng nhập cuối của user vào database
     * 2. Gọi method cha để thực hiện redirect (về trang trước đó hoặc trang mặc định)
     * 
     * @param request HTTP request hiện tại
     * @param response HTTP response để redirect
     * @param authentication Object chứa thông tin user đã xác thực
     *                       - authentication.getName() trả về username
     *                       - authentication.getPrincipal() trả về CustomUserDetails
     * 
     * @throws IOException Nếu có lỗi I/O khi redirect
     * @throws ServletException Nếu có lỗi servlet
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Cập nhật thời gian đăng nhập cuối vào database
        // authentication.getName() trả về username của user vừa đăng nhập
        authService.updateLastLogin(authentication.getName());
        
        // Gọi method cha để thực hiện redirect mặc định
        // SavedRequestAwareAuthenticationSuccessHandler sẽ redirect về:
        // - Trang user muốn truy cập trước khi bị chuyển sang login (nếu có)
        // - Hoặc defaultSuccessUrl được cấu hình trong SecurityConfig
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
