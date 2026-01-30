package poly.edu.java5_asm.module.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.java5_asm.common.security.CustomUserDetails;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/avatar/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("avatar") MultipartFile file) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File không được để trống"));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body(Map.of("error", "Kích thước file tối đa là 5MB"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Chỉ chấp nhận file ảnh"));
        }

        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : ".jpg";
            String newFilename = "avatar_" + userDetails.getUser().getId() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update user avatar URL
            String avatarUrl = "/assets/img/avatar/" + newFilename;
            User updatedUser = userService.updateAvatar(userDetails.getUser().getId(), avatarUrl);

            log.info("Avatar updated for user {}: {}", userDetails.getUser().getId(), avatarUrl);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "avatarUrl", avatarUrl,
                    "message", "Cập nhật avatar thành công"
            ));

        } catch (IOException e) {
            log.error("Error uploading avatar: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi khi upload file"));
        }
    }
}
