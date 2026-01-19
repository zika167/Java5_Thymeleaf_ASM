package poly.edu.java5_asm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import poly.edu.java5_asm.security.CustomUserDetailsService;

/**
 * Cấu hình Spring Security cho ứng dụng.
 * Định nghĩa các quy tắc bảo mật, xác thực và phân quyền.
 * 
 * @Configuration: Đánh dấu class này chứa các Bean configuration
 * @EnableWebSecurity: Kích hoạt Spring Security cho ứng dụng web
 * @EnableMethodSecurity: Kích hoạt @PreAuthorize, @PostAuthorize
 * @RequiredArgsConstructor: Lombok tự động tạo constructor với các field final
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /** Service để load thông tin user từ database */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Cấu hình SecurityFilterChain - chuỗi các filter bảo mật.
     * Đây là cấu hình chính của Spring Security.
     * 
     * @param http HttpSecurity object để cấu hình
     * @return SecurityFilterChain đã được cấu hình
     * @throws Exception nếu có lỗi trong quá trình cấu hình
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ===== CẤU HÌNH PHÂN QUYỀN TRUY CẬP URL =====
            .authorizeHttpRequests(auth -> auth
                // Các trang công khai - ai cũng truy cập được
                .requestMatchers("/", "/index", "/products/**", "/category/**").permitAll()
                // REST API Admin - yêu cầu role ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // REST API công khai - cho phép truy cập không cần đăng nhập
                .requestMatchers("/api/**").permitAll()
                // Các trang xác thực - cho phép truy cập để đăng nhập/đăng ký
                .requestMatchers("/auth/**", "/sign-in", "/sign-up").permitAll()
                // Static resources - CSS, JS, images
                .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Trang admin - chỉ user có role ADMIN mới truy cập được
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Tất cả các request khác đều yêu cầu đăng nhập
                .anyRequest().authenticated()
            )
            
            // ===== CẤU HÌNH FORM LOGIN =====
            .formLogin(form -> form
                .loginPage("/sign-in")              // URL trang đăng nhập custom
                .loginProcessingUrl("/auth/login")  // URL xử lý POST request đăng nhập
                .usernameParameter("username")      // Tên parameter username trong form
                .passwordParameter("password")      // Tên parameter password trong form
                .defaultSuccessUrl("/", true)       // Redirect về trang chủ sau khi đăng nhập thành công
                .failureUrl("/sign-in?error=true")  // Redirect khi đăng nhập thất bại
                .permitAll()                        // Cho phép tất cả truy cập trang login
            )
            
            // ===== CẤU HÌNH LOGOUT =====
            .logout(logout -> logout
                .logoutUrl("/auth/logout")                  // URL xử lý logout
                .logoutSuccessUrl("/sign-in?logout=true")   // Redirect sau khi logout
                .invalidateHttpSession(true)                // Hủy session hiện tại
                .deleteCookies("JSESSIONID")                // Xóa cookie session
                .permitAll()
            )
            
            // ===== CẤU HÌNH REMEMBER ME =====
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")          // Key bí mật để mã hóa token
                .tokenValiditySeconds(86400 * 7)   // Token có hiệu lực 7 ngày (86400 giây = 1 ngày)
            )
            
            // Đăng ký UserDetailsService để Spring Security sử dụng
            .userDetailsService(userDetailsService);

        return http.build();
    }

    /**
     * Bean PasswordEncoder sử dụng thuật toán BCrypt.
     * BCrypt là thuật toán hash một chiều, an toàn cho việc lưu trữ password.
     * 
     * Đặc điểm BCrypt:
     * - Tự động tạo salt ngẫu nhiên
     * - Có thể điều chỉnh độ phức tạp (strength)
     * - Chống được rainbow table attack
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean AuthenticationManager để quản lý quá trình xác thực.
     * Được sử dụng khi cần xác thực thủ công (programmatic authentication).
     * 
     * @param config AuthenticationConfiguration từ Spring Security
     * @return AuthenticationManager instance
     * @throws Exception nếu không thể lấy AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
