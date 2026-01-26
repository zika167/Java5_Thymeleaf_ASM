package poly.edu.java5_asm.exception;

/**
 * Exception khi sản phẩm đã có trong wishlist
 */
public class WishlistDuplicateException extends WishlistException {

    public WishlistDuplicateException(String message) {
        super(message);
    }

    public WishlistDuplicateException(Long userId, Long productId) {
        super(String.format("Sản phẩm ID %d đã có trong danh sách yêu thích của người dùng ID %d",
                productId, userId));
    }
}
