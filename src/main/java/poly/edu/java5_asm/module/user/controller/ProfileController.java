package poly.edu.java5_asm.module.user.controller;

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
import poly.edu.java5_asm.module.user.dto.request.ProfileUpdateRequest;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.common.security.CustomUserDetails;
import poly.edu.java5_asm.module.user.service.UserService;
import poly.edu.java5_asm.module.address.service.AddressServiceImpl;
import poly.edu.java5_asm.module.address.dto.response.AddressResponse;

import java.util.List;

/**
 * Controller xử lý các request liên quan đến trang Profile.
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final AddressServiceImpl addressService;

    /**
     * Hiển thị trang Profile.
     */
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getUser().getId());
        model.addAttribute("user", user);
        
        // Load địa chỉ của user
        try {
            List<AddressResponse> addresses = addressService.getUserAddresses(user);
            model.addAttribute("addresses", addresses);
            
            // Lấy địa chỉ mặc định
            AddressResponse defaultAddress = addresses.stream()
                    .filter(a -> a.getIsDefault() != null && a.getIsDefault())
                    .findFirst()
                    .orElse(addresses.isEmpty() ? null : addresses.get(0));
            model.addAttribute("defaultAddress", defaultAddress);
        } catch (Exception e) {
            model.addAttribute("addresses", List.of());
            model.addAttribute("defaultAddress", null);
        }
        
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
        } catch (Exception e) {
            User user = userService.findById(userDetails.getUser().getId());
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "module/user/edit-personal-info";
        }
    }
}
