package poly.edu.java5_asm.exception;

/**
 * Exception khi sản phẩm không khả dụng (inactive hoặc out of stock)
 */
public class ProductUnavailableException extends WishlistException {

    public ProductUnavailableException(String message) {
        super(message);
    }

    public ProductUnavailableException(Long productId, String reason) {
        super(String.format("Sản phẩm ID %d không khả dụng: %s", productId, reason));
    }
}
