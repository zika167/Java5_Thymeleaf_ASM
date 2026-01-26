package poly.edu.java5_asm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.java5_asm.dto.request.RegisterRequest;
import poly.edu.java5_asm.service.AuthService;

/**
 * Controller xử lý các request liên quan đến xác thực người dùng.
 * Bao gồm: hiển thị trang đăng nhập, đăng ký và xử lý form đăng ký.
 * 
 * Lưu ý: Việc xử lý đăng nhập (POST /auth/login) được Spring Security
 * tự động xử lý, không cần viết method trong controller.
 * 
 * @Controller: Đánh dấu class này là Spring MVC Controller
 * @RequiredArgsConstructor: Lombok tự động tạo constructor với các field final
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    /** Service xử lý nghiệp vụ xác thực */
    private final AuthService authService;

    /**
     * Hiển thị trang đăng nhập.
     * 
     * URL: GET /sign-in
     * Template: sign-in.html
     * 
     * @return Tên template Thymeleaf để render
     */
    @GetMapping("/sign-in")
    public String signInPage() {
        return "module/auth/sign-in";
    }

    /**
     * Hiển thị trang đăng ký tài khoản mới.
     * Khởi tạo một RegisterRequest rỗng để binding với form.
     * 
     * URL: GET /sign-up
     * Template: sign-up.html
     * 
     * @param model Model để truyền dữ liệu sang view
     * @return Tên template Thymeleaf để render
     */
    @GetMapping("/sign-up")
    public String signUpPage(Model model) {
        // Tạo object rỗng để Thymeleaf binding với form
        model.addAttribute("registerRequest", new RegisterRequest());
        return "module/auth/sign-up";
    }

    /**
     * Error handler để debug OAuth2 errors
     */
    @GetMapping("/error")
    public String errorPage(Model model) {
        model.addAttribute("errorMessage", "Có lỗi xảy ra trong quá trình đăng nhập. Vui lòng thử lại.");
        return "module/auth/sign-in";
    }

    /**
     * Xử lý form đăng ký tài khoản mới.
     * 
     * URL: POST /auth/register
     * 
     * Quy trình xử lý:
     * 1. Validate dữ liệu từ form (annotation validation trong DTO)
     * 2. Nếu có lỗi validation -> quay lại trang đăng ký với thông báo lỗi
     * 3. Gọi AuthService để tạo tài khoản mới
     * 4. Nếu thành công -> redirect sang trang đăng nhập với thông báo
     * 5. Nếu thất bại (username/email trùng) -> quay lại với thông báo lỗi
     * 
     * @param registerRequest DTO chứa dữ liệu từ form đăng ký
     * @param bindingResult Kết quả validation, chứa các lỗi nếu có
     * @param redirectAttributes Để truyền flash message khi redirect
     * @param model Model để truyền dữ liệu sang view khi có lỗi
     * @return Tên view hoặc redirect URL
     * 
     * @Valid: Kích hoạt validation theo các annotation trong RegisterRequest
     * @ModelAttribute: Binding dữ liệu từ form vào object
     */
    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        // Kiểm tra lỗi validation (từ annotation trong DTO)
        if (bindingResult.hasErrors()) {
            return "module/auth/sign-up";
        }

        try {
            // Gọi service để đăng ký tài khoản mới
            authService.register(registerRequest);
            
            // Đăng ký thành công -> redirect sang trang đăng nhập
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/sign-in";
            
        } catch (RuntimeException e) {
            // Đăng ký thất bại (username/email trùng)
            model.addAttribute("errorMessage", e.getMessage());
            return "module/auth/sign-up";
        }
    }
}