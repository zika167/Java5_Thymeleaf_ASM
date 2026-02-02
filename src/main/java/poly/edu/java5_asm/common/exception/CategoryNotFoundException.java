package poly.edu.java5_asm.common.exception;

/**
 * Exception khi không tìm thấy danh mục
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(Long id) {
        super("Không tìm thấy danh mục với ID: " + id);
    }

    public static CategoryNotFoundException bySlug(String slug) {
        return new CategoryNotFoundException("Không tìm thấy danh mục: " + slug);
    }
}
