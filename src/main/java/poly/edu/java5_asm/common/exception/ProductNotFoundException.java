package poly.edu.java5_asm.exception;

/**
 * Exception khi không tìm thấy sản phẩm
 */
public class ProductNotFoundException extends WishlistException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId) {
        super(String.format("Không tìm thấy sản phẩm với ID: %d", productId));
    }
}
