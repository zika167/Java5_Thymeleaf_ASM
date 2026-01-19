package poly.edu.java5_asm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) chứa dữ liệu đăng ký tài khoản mới.
 * 
 * Class này được sử dụng để:
 * - Nhận dữ liệu từ form đăng ký (binding với Thymeleaf)
 * - Validate dữ liệu đầu vào trước khi xử lý
 * - Tách biệt dữ liệu request với entity User
 * 
 * Các annotation validation sẽ được kích hoạt khi sử dụng @Valid trong controller.
 * 
 * @Data: Lombok annotation tự động generate getter, setter, toString, equals, hashCode
 */
@Data
public class RegisterRequest {

    /**
     * Tên đăng nhập của người dùng.
     * 
     * Validation:
     * - Không được để trống
     * - Độ dài từ 3 đến 50 ký tự
     */
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
    private String username;

    /**
     * Địa chỉ email của người dùng.
     * 
     * Validation:
     * - Không được để trống
     * - Phải đúng định dạng email (có @ và domain)
     */
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    /**
     * Mật khẩu người dùng chọn.
     * 
     * Validation:
     * - Không được để trống
     * - Tối thiểu 6 ký tự
     * 
     * Lưu ý: Password sẽ được mã hóa bằng BCrypt trước khi lưu vào database
     */
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
    private String password;

    /**
     * Xác nhận mật khẩu - phải khớp với password.
     * 
     * Validation:
     * - Không được để trống
     * 
     * Lưu ý: Việc kiểm tra khớp với password được thực hiện trong AuthService,
     * không phải bằng annotation validation
     */
    @NotBlank(message = "Xác nhận password không được để trống")
    private String confirmPassword;

    /**
     * Họ tên đầy đủ của người dùng (tùy chọn).
     * 
     * Không có validation bắt buộc - người dùng có thể bỏ trống
     * và cập nhật sau trong trang profile.
     */
    private String fullName;
}
