package poly.edu.java5_asm.common.exception;

/**
 * Exception khi không tìm thấy địa chỉ
 */
public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(Long id) {
        super("Không tìm thấy địa chỉ với ID: " + id);
    }
}
