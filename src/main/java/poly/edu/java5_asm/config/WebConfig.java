package poly.edu.java5_asm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * Cấu hình Web MVC cho development
 * Tắt cache cho static resources để thấy thay đổi ngay khi F5
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cấu hình cache control cho static resources
     * Trong development, không cache static files để thấy thay đổi ngay
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.noCache()
                        .noStore()
                        .mustRevalidate()
                        .cachePrivate());

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.noCache()
                        .noStore()
                        .mustRevalidate()
                        .cachePrivate());

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.noCache()
                        .noStore()
                        .mustRevalidate()
                        .cachePrivate());

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.noCache()
                        .noStore()
                        .mustRevalidate()
                        .cachePrivate());
    }
}
