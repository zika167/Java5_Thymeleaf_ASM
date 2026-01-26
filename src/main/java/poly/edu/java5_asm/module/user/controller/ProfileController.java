package poly.edu.java5_asm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.java5_asm.dto.ProfileUpdateRequest;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.security.CustomUserDetails;
import poly.edu.java5_asm.service.UserService;

/**
 * Controller xử lý các request liên quan đến trang Profile.
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    /**
     * Hiển thị trang Profile.
     */
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", user);
        return "module/user/profile";
    }

    /**
     * Hiển thị trang chỉnh sửa thông tin cá nhân.
     */
    @GetMapping("/edit-personal-info")
    public String editPersonalInfoPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId());

        // Tạo DTO từ thông tin user hiện tại
        ProfileUpdateRequest profileRequest = ProfileUpdateRequest.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

        model.addAttribute("user", user);
        model.addAttribute("profileRequest", profileRequest);
        return "module/user/edit-personal-info";
    }

    /**
     * Xử lý cập nhật thông tin cá nhân.
     */
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @Valid @ModelAttribute("profileRequest") ProfileUpdateRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            User user = userService.findById(userDetails.getUser().getId());
            model.addAttribute("user", user);
            return "module/user/edit-personal-info";
        }

        try {
            userService.updateProfile(userDetails.getUser().getId(), request);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
            return "redirect:/profile";
        } catch (RuntimeException e) {
            User user = userService.findById(userDetails.getUser().getId());
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "module/user/edit-personal-info";
        }
    }
}
