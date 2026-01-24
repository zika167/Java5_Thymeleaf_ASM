package poly.edu.java5_asm.exception;

/**
 * Exception khi không tìm thấy người dùng
 */
public class UserNotFoundException extends WishlistException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Long userId) {
        super(String.format("Không tìm thấy người dùng với ID: %d", userId));
    }
}
