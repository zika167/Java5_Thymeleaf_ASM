package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến đánh giá sản phẩm
 */
public class ReviewException extends RuntimeException {

    public ReviewException(String message) {
        super(message);
    }

    public static ReviewException alreadyReviewed() {
        return new ReviewException("Bạn đã đánh giá sản phẩm này rồi");
    }

    public static ReviewException notOwner() {
        return new ReviewException("Bạn không có quyền thực hiện thao tác này");
    }

    public static ReviewException notFound(Long id) {
        return new ReviewException("Không tìm thấy đánh giá với ID: " + id);
    }
}
