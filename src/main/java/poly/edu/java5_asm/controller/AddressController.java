package poly.edu.java5_asm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.dto.request.CreateAddressRequest;
import poly.edu.java5_asm.dto.response.AddressResponse;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.UserRepository;
import poly.edu.java5_asm.service.impl.AddressServiceImpl;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST API Controller để quản lý địa chỉ giao hàng
 * 
 * Endpoints:
 * - GET /api/addresses - Lấy tất cả địa chỉ của user
 * - GET /api/addresses/{id} - Lấy địa chỉ theo ID
 * - GET /api/addresses/default - Lấy địa chỉ mặc định
 * - POST /api/addresses - Tạo địa chỉ mới
 * - PUT /api/addresses/{id} - Cập nhật địa chỉ
 * - DELETE /api/addresses/{id} - Xóa địa chỉ
 * - PUT /api/addresses/{id}/set-default - Set địa chỉ làm mặc định
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressServiceImpl addressService;
    private final UserRepository userRepository;

    /**
     * Lấy user từ Authentication
     */
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Bạn cần đăng nhập để thực hiện hành động này");
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    /**
     * Lấy tất cả địa chỉ của user
     * GET /api/addresses
     */
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            List<AddressResponse> addresses = addressService.getUserAddresses(user);
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            log.error("Error getting user addresses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Lấy địa chỉ theo ID
     * GET /api/addresses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddress(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            AddressResponse address = addressService.getAddress(user, id);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            log.error("Error getting address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Lấy địa chỉ mặc định
     * GET /api/addresses/default
     */
    @GetMapping("/default")
    public ResponseEntity<AddressResponse> getDefaultAddress(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            AddressResponse address = addressService.getDefaultAddress(user);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            log.error("Error getting default address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Tạo địa chỉ mới
     * POST /api/addresses
     */
    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @Valid @RequestBody CreateAddressRequest request,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            AddressResponse address = addressService.createAddress(user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(address);
        } catch (RuntimeException e) {
            log.error("Error creating address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cập nhật địa chỉ
     * PUT /api/addresses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody CreateAddressRequest request,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            AddressResponse address = addressService.updateAddress(user, id, request);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            log.error("Error updating address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Xóa địa chỉ
     * DELETE /api/addresses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            addressService.deleteAddress(user, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Set địa chỉ làm mặc định
     * PUT /api/addresses/{id}/set-default
     */
    @PutMapping("/{id}/set-default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            AddressResponse address = addressService.setDefaultAddress(user, id);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            log.error("Error setting default address: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
