package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến giỏ hàng
 */
public class CartException extends RuntimeException {

    public CartException(String message) {
        super(message);
    }

    public static CartException emptyCart() {
        return new CartException("Giỏ hàng trống");
    }

    public static CartException itemNotFound(Long itemId) {
        return new CartException("Không tìm thấy sản phẩm trong giỏ hàng: " + itemId);
    }

    public static CartException itemNotBelongToCart() {
        return new CartException("Sản phẩm không thuộc giỏ hàng của bạn");
    }
}
