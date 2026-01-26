package poly.edu.java5_asm.exception;

/**
 * Exception khi không tìm thấy wishlist item
 */
public class WishlistNotFoundException extends WishlistException {
    
    public WishlistNotFoundException(String message) {
        super(message);
    }
    
    public WishlistNotFoundException(Long userId, Long productId) {
        super(String.format("Không tìm thấy sản phẩm ID %d trong danh sách yêu thích của người dùng ID %d", 
                productId, userId));
    }
}
