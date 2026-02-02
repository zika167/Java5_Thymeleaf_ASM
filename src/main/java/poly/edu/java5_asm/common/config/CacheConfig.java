package poly.edu.java5_asm.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poly.edu.java5_asm.common.constant.CacheNames;

import java.util.concurrent.TimeUnit;

/**
 * Configuration cho Spring Cache với Caffeine
 * 
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║                    TẠI SAO CẦN CACHING?                          ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║ 1. PERFORMANCE (Hiệu suất):                                      ║
 * ║    - Giảm query database cho data ít thay đổi                    ║
 * ║    - Response time: 50ms → 1ms                                   ║
 * ║                                                                  ║
 * ║ 2. SCALABILITY (Khả năng mở rộng):                               ║
 * ║    - Giảm tải cho database                                       ║
 * ║    - Hỗ trợ nhiều concurrent users hơn                           ║
 * ║                                                                  ║
 * ║ 3. COST (Chi phí):                                               ║
 * ║    - Ít query = ít tài nguyên DB = tiết kiệm tiền                ║
 * ╚══════════════════════════════════════════════════════════════════╝
 * 
 * CAFFEINE vs ConcurrentMapCache:
 * - Caffeine: TTL (time-to-live), max size, statistics
 * - ConcurrentMap: Đơn giản, không có TTL
 * 
 * CÁCH SỬ DỤNG:
 * @Cacheable(value = CacheNames.CATEGORIES)
 * public List<Category> getAllCategories() { ... }
 * 
 * @CacheEvict(value = CacheNames.CATEGORIES, allEntries = true)
 * public void updateCategory(...) { ... }
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure CacheManager với Caffeine
     * High-performance in-memory cache
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            CacheNames.CATEGORIES,
            CacheNames.BRANDS,
            CacheNames.FEATURED_PRODUCTS,
            CacheNames.PRODUCT_BY_ID,
            CacheNames.STATISTICS,
            "wishlist"  // Giữ lại cache cũ
        );
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
            // Tối đa 500 entries trong cache
            .maximumSize(500)
            // Tự động xóa sau 10 phút không truy cập
            .expireAfterAccess(10, TimeUnit.MINUTES)
            // Tự động xóa sau 30 phút kể từ khi tạo
            .expireAfterWrite(30, TimeUnit.MINUTES)
            // Ghi lại statistics để monitor
            .recordStats()
        );
        
        return cacheManager;
    }
}
