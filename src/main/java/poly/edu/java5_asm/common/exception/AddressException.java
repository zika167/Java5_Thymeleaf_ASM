package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến địa chỉ
 */
public class AddressException extends RuntimeException {

    public AddressException(String message) {
        super(message);
    }

    public static AddressException limitReached() {
        return new AddressException("Bạn đã đạt giới hạn 5 địa chỉ. Vui lòng xóa một địa chỉ trước khi thêm mới");
    }

    public static AddressException notOwner() {
        return new AddressException("Bạn không có quyền thực hiện thao tác này");
    }
}
