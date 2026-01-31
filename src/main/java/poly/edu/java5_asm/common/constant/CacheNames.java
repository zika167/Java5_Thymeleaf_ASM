package poly.edu.java5_asm.common.constant;

/**
 * Cache Names - Tên các cache trong hệ thống
 * 
 * Sử dụng constants để tránh typo khi dùng @Cacheable
 */
public final class CacheNames {

    private CacheNames() {
    }

    // Cache cho danh mục - ít thay đổi
    public static final String CATEGORIES = "categories";
    
    // Cache cho thương hiệu - ít thay đổi
    public static final String BRANDS = "brands";
    
    // Cache cho sản phẩm nổi bật
    public static final String FEATURED_PRODUCTS = "featuredProducts";
    
    // Cache cho sản phẩm theo ID
    public static final String PRODUCT_BY_ID = "productById";
    
    // Cache cho thống kê
    public static final String STATISTICS = "statistics";
}
