package poly.edu.java5_asm.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Tự động load file .env vào Spring Environment
 * Chỉ load khi chạy local (không ảnh hưởng Docker)
 */
public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            // Load .env file (nếu có)
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing() // Không báo lỗi nếu không có file .env
                    .load();

            // Chuyển các biến từ .env vào Spring Environment
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Map<String, Object> dotenvMap = new HashMap<>();
            
            dotenv.entries().forEach(entry -> {
                dotenvMap.put(entry.getKey(), entry.getValue());
            });

            // Thêm vào Spring Environment với priority cao
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenvProperties", dotenvMap));

            System.out.println("✅ Loaded .env file successfully");
        } catch (Exception e) {
            System.out.println("⚠️ No .env file found or error loading it: " + e.getMessage());
        }
    }
}
