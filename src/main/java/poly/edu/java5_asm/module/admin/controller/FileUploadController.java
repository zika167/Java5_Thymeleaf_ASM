package poly.edu.java5_asm.module.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller xử lý upload file
 */
@RestController
@RequestMapping("/api/admin/upload")
@PreAuthorize("hasRole('ADMIN')")
public class FileUploadController {
    
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${app.upload.dir:src/main/resources/static/assets/img/product}")
    private String uploadDir;

    /**
     * Upload hình ảnh sản phẩm
     */
    @PostMapping("/product-image")
    public ResponseEntity<Map<String, Object>> uploadProductImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng chọn file");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            response.put("success", false);
            response.put("message", "Chỉ chấp nhận file hình ảnh");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            response.put("success", false);
            response.put("message", "File không được vượt quá 5MB");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = "product-" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return URL path
            String imageUrl = "/assets/img/product/" + newFilename;
            
            response.put("success", true);
            response.put("message", "Upload thành công");
            response.put("imageUrl", imageUrl);
            response.put("filename", newFilename);
            
            log.info("Uploaded product image: {}", imageUrl);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Lỗi upload file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
