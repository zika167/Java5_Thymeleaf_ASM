package poly.edu.java5_asm.controller;

import jakarta.validation.Valid;
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

import java.util.List;

/**
 * REST Controller quản lý địa chỉ giao hàng
 * Endpoints: /api/addresses
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressServiceImpl addressService;
    private final UserRepository userRepository;

    /**
     * Lấy tất cả địa chỉ của user hiện tại
     * GET /api/addresses
     */
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        List<AddressResponse> addresses = addressService.getUserAddresses(user);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Lấy địa chỉ mặc định của user
     * GET /api/addresses/default
     */
    @GetMapping("/default")
    public ResponseEntity<AddressResponse> getDefaultAddress(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            AddressResponse address = addressService.getDefaultAddress(user);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            AddressResponse address = addressService.getAddress(user, id);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            AddressResponse address = addressService.createAddress(user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(address);
        } catch (RuntimeException e) {
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
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            AddressResponse address = addressService.updateAddress(user, id, request);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
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
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            addressService.deleteAddress(user, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Set địa chỉ làm mặc định
     * PATCH /api/addresses/{id}/set-default
     */
    @PatchMapping("/{id}/set-default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @PathVariable Long id,
            Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        try {
            AddressResponse address = addressService.setDefaultAddress(user, id);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
