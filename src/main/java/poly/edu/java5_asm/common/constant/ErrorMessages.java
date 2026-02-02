package poly.edu.java5_asm.common.constant;

/**
 * Error Messages - Tập trung tất cả thông báo lỗi
 * 
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║                    TẠI SAO CẦN CONSTANTS?                        ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║ 1. Tránh typo khi viết string nhiều lần                          ║
 * ║ 2. Dễ tìm kiếm và thay đổi message                               ║
 * ║ 3. Hỗ trợ đa ngôn ngữ (i18n) sau này                             ║
 * ║ 4. IDE autocomplete giúp code nhanh hơn                          ║
 * ╚══════════════════════════════════════════════════════════════════╝
 * 
 * CÁCH SỬ DỤNG:
 * throw new ProductNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND);
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Private constructor - không cho phép tạo instance
    }

    // ==================== PRODUCT ====================
    public static final String PRODUCT_NOT_FOUND = "Sản phẩm không tồn tại";
    public static final String PRODUCT_OUT_OF_STOCK = "Sản phẩm đã hết hàng";
    public static final String PRODUCT_INSUFFICIENT_STOCK = "Số lượng tồn kho không đủ";
    public static final String PRODUCT_INACTIVE = "Sản phẩm không còn hoạt động";

    // ==================== USER ====================
    public static final String USER_NOT_FOUND = "Người dùng không tồn tại";
    public static final String USER_ALREADY_EXISTS = "Tên đăng nhập đã tồn tại";
    public static final String EMAIL_ALREADY_EXISTS = "Email đã được sử dụng";
    public static final String INVALID_CREDENTIALS = "Tên đăng nhập hoặc mật khẩu không đúng";
    public static final String USER_INACTIVE = "Tài khoản đã bị vô hiệu hóa";

    // ==================== CART ====================
    public static final String CART_NOT_FOUND = "Giỏ hàng không tồn tại";
    public static final String CART_ITEM_NOT_FOUND = "Sản phẩm không có trong giỏ hàng";
    public static final String CART_EMPTY = "Giỏ hàng trống";
    public static final String CART_ITEM_NOT_BELONG = "Sản phẩm không thuộc giỏ hàng của bạn";
    public static final String QUANTITY_MUST_BE_POSITIVE = "Số lượng phải lớn hơn 0";

    // ==================== ORDER ====================
    public static final String ORDER_NOT_FOUND = "Đơn hàng không tồn tại";
    public static final String ORDER_CANNOT_CANCEL = "Không thể hủy đơn hàng này";
    public static final String ORDER_ALREADY_PAID = "Đơn hàng đã được thanh toán";

    // ==================== WISHLIST ====================
    public static final String WISHLIST_NOT_FOUND = "Sản phẩm yêu thích không tồn tại";
    public static final String WISHLIST_DUPLICATE = "Sản phẩm đã có trong danh sách yêu thích";

    // ==================== ADDRESS ====================
    public static final String ADDRESS_NOT_FOUND = "Địa chỉ không tồn tại";
    public static final String ADDRESS_NOT_BELONG = "Địa chỉ không thuộc về bạn";

    // ==================== CATEGORY & BRAND ====================
    public static final String CATEGORY_NOT_FOUND = "Danh mục không tồn tại";
    public static final String BRAND_NOT_FOUND = "Thương hiệu không tồn tại";

    // ==================== REVIEW ====================
    public static final String REVIEW_NOT_FOUND = "Đánh giá không tồn tại";
    public static final String REVIEW_ALREADY_EXISTS = "Bạn đã đánh giá sản phẩm này";

    // ==================== VALIDATION ====================
    public static final String INVALID_INPUT = "Dữ liệu không hợp lệ";
    public static final String REQUIRED_FIELD = "Trường này là bắt buộc";

    // ==================== GENERAL ====================
    public static final String UNAUTHORIZED = "Bạn cần đăng nhập để thực hiện thao tác này";
    public static final String FORBIDDEN = "Bạn không có quyền thực hiện thao tác này";
    public static final String INTERNAL_ERROR = "Đã xảy ra lỗi. Vui lòng thử lại sau";
}
