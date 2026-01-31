package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến đơn hàng
 */
public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static OrderException emptyCart() {
        return new OrderException("Giỏ hàng trống");
    }

    public static OrderException invalidStatus(String action, String currentStatus) {
        return new OrderException("Không thể " + action + " đơn hàng ở trạng thái " + currentStatus);
    }

    public static OrderException cannotCancel() {
        return new OrderException("Không thể hủy đơn hàng ở trạng thái này");
    }

    public static OrderException cannotConfirm() {
        return new OrderException("Chỉ có thể xác nhận đơn hàng ở trạng thái PENDING");
    }
}
