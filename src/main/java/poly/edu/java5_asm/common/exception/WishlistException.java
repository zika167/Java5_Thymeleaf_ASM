package poly.edu.java5_asm.exception;

/**
 * Base exception cho Wishlist operations
 */
public class WishlistException extends RuntimeException {
    
    public WishlistException(String message) {
        super(message);
    }
    
    public WishlistException(String message, Throwable cause) {
        super(message, cause);
    }
}
