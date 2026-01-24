package poly.edu.java5_asm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import poly.edu.java5_asm.security.CustomUserDetailsService;
import poly.edu.java5_asm.security.FormLoginSuccessHandler;
import poly.edu.java5_asm.security.JwtAuthenticationFilter;
import poly.edu.java5_asm.security.OAuth2LoginFailureHandler;
import poly.edu.java5_asm.security.OAuth2LoginSuccessHandler;

/**
 * Security Configuration cho Spring Boot 3.4 + Spring Security 6
 * Hỗ trợ:
 * - Form-based authentication với JWT
 * - Google OAuth2 authentication với JWT
 * - Stateless session management
 * - HTTP-Only Cookie để lưu JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ===== CSRF CONFIGURATION =====
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") // Tắt CSRF cho API endpoints
            )
            
            // ===== SESSION MANAGEMENT =====
            // IF_REQUIRED: Cho phép tạo session khi cần (OAuth2 flow), nhưng vẫn dùng JWT cho authentication
            // OAuth2 login flow cần session để lưu authorization request và state
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            
            // ===== AUTHORIZATION CONFIGURATION =====
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/", "/index", "/products/**", "/category/**").permitAll()
                .requestMatchers("/auth/**", "/sign-in", "/sign-up").permitAll()
                .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/actuator/health").permitAll() // Health check cho Docker
                
                // API endpoints - Review APIs are public for GET, require auth for POST/DELETE
                .requestMatchers("/api/reviews/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").permitAll()
                
                // Admin pages
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // OAuth2 endpoints
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // ===== FORM LOGIN CONFIGURATION =====
            .formLogin(form -> form
                .loginPage("/sign-in")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(formLoginSuccessHandler) // Tạo JWT sau khi login thành công
                .failureUrl("/sign-in?error=true")
                .permitAll()
            )
            
            // ===== OAUTH2 LOGIN CONFIGURATION =====
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/sign-in")
                .successHandler(oAuth2LoginSuccessHandler) // Tạo JWT sau khi OAuth2 login thành công
                .failureHandler(oAuth2LoginFailureHandler) // Log chi tiết lỗi OAuth2
            )
            
            // ===== LOGOUT CONFIGURATION =====
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/sign-in?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "JWT_TOKEN") // Xóa cả session cookie và JWT cookie
                .permitAll()
            )
            
            // ===== USER DETAILS SERVICE =====
            .userDetailsService(userDetailsService);

        // ===== ADD JWT FILTER =====
        // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
