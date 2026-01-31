package poly.edu.java5_asm.common.exception;

/**
 * Exception khi không tìm thấy thương hiệu
 */
public class BrandNotFoundException extends RuntimeException {

    public BrandNotFoundException(String message) {
        super(message);
    }

    public BrandNotFoundException(Long id) {
        super("Không tìm thấy thương hiệu với ID: " + id);
    }

    public static BrandNotFoundException bySlug(String slug) {
        return new BrandNotFoundException("Không tìm thấy thương hiệu: " + slug);
    }
}
