package poly.edu.java5_asm.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 Login Failure Handler
 * Xử lý khi OAuth2 login thất bại
 */
@Component
@Slf4j
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("=== OAuth2 Login Failed ===");
        log.error("Error message: {}", exception.getMessage());
        log.error("Error class: {}", exception.getClass().getName());
        log.error("Stack trace:", exception);

        // Redirect về trang login với error message
        setDefaultFailureUrl("/sign-in?error=oauth2");
        super.onAuthenticationFailure(request, response, exception);
    }
}
