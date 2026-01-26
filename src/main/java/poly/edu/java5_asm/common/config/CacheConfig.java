package poly.edu.java5_asm.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuration cho Spring Cache
 * <p>
 * Enables caching cho WishlistService
 * Sử dụng ConcurrentMapCacheManager (in-memory cache)
 * <p>
 * For production, consider:
 * - Redis cache (distributed)
 * - Caffeine cache (high performance)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure CacheManager với ConcurrentMapCacheManager
     * Simple in-memory cache implementation
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList("wishlist"));
        return cacheManager;
    }

    // For Caffeine cache (better performance), add dependency and configure:
    /*
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("wishlist");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }
    */
}
