package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến thanh toán
 */
public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public static PaymentException momoError(String message) {
        return new PaymentException("Lỗi thanh toán Momo: " + message);
    }

    public static PaymentException vnpayError(String message) {
        return new PaymentException("Lỗi thanh toán VNPay: " + message);
    }

    public static PaymentException signatureError() {
        return new PaymentException("Lỗi tạo chữ ký thanh toán");
    }
}
