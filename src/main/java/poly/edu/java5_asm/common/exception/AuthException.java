package poly.edu.java5_asm.common.exception;

/**
 * Exception cho các lỗi liên quan đến xác thực
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public static AuthException usernameExists() {
        return new AuthException("Username đã tồn tại");
    }

    public static AuthException emailExists() {
        return new AuthException("Email đã được sử dụng");
    }

    public static AuthException passwordMismatch() {
        return new AuthException("Password xác nhận không khớp");
    }

    public static AuthException invalidCredentials() {
        return new AuthException("Thông tin đăng nhập không chính xác");
    }

    public static AuthException invalidPassword() {
        return new AuthException("Mật khẩu phải từ 6-100 ký tự");
    }
}
