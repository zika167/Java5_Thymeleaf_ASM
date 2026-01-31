package poly.edu.java5_asm.common.exception;

/**
 * Exception khi không tìm thấy đơn hàng
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(Long id) {
        super("Không tìm thấy đơn hàng với ID: " + id);
    }

    public static OrderNotFoundException byOrderNumber(String orderNumber) {
        return new OrderNotFoundException("Không tìm thấy đơn hàng: " + orderNumber);
    }
}
