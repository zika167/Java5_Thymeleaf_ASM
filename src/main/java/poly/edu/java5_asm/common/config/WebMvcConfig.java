package poly.edu.java5_asm.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import poly.edu.java5_asm.common.interceptor.ActivityLoggingInterceptor;

/**
 * Web MVC Configuration
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ActivityLoggingInterceptor activityLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(activityLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/assets/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/api/**",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.svg",
                        "/**/*.ico"
                );
    }
}
