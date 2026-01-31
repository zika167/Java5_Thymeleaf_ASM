package poly.edu.java5_asm.common.audit;

/**
 * Enum định nghĩa các loại action cần audit
 */
public enum AuditAction {
    // Authentication
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGOUT,
    REGISTER,
    PASSWORD_CHANGE,
    PASSWORD_RESET,
    
    // Order
    ORDER_CREATE,
    ORDER_CONFIRM,
    ORDER_CANCEL,
    ORDER_STATUS_UPDATE,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    
    // Cart
    CART_ADD_ITEM,
    CART_UPDATE_ITEM,
    CART_REMOVE_ITEM,
    CART_CLEAR,
    
    // Product (Admin)
    PRODUCT_CREATE,
    PRODUCT_UPDATE,
    PRODUCT_DELETE,
    PRODUCT_STOCK_UPDATE,
    
    // User
    USER_UPDATE_PROFILE,
    USER_UPDATE_ADDRESS,
    USER_DELETE,
    
    // Admin
    ADMIN_USER_UPDATE,
    ADMIN_USER_DELETE,
    ADMIN_ORDER_UPDATE,
    ADMIN_SETTINGS_UPDATE,
    
    // Review
    REVIEW_CREATE,
    REVIEW_UPDATE,
    REVIEW_DELETE,
    
    // Wishlist
    WISHLIST_ADD,
    WISHLIST_REMOVE,
    
    // System
    SYSTEM_ERROR,
    DATA_EXPORT,
    DATA_IMPORT
}
