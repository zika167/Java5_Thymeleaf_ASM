package poly.edu.java5_asm.module.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import poly.edu.java5_asm.module.cart.dto.request.AddToCartRequest;
import poly.edu.java5_asm.module.cart.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.common.security.CustomUserDetails;
import poly.edu.java5_asm.module.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart", description = "API quản lý giỏ hàng")
public class CartController {

    private final CartService cartService;

    /**
     * Lấy user từ Authentication - YÊU CẦU ĐĂNG NHẬP
     */
    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() 
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("Vui lòng đăng nhập để sử dụng giỏ hàng");
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    /**
     * Lấy giỏ hàng hiện tại - YÊU CẦU ĐĂNG NHẬP
     */
    @GetMapping
    @Operation(summary = "Lấy giỏ hàng", description = "Lấy thông tin giỏ hàng hiện tại của user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa đăng nhập"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<?> getCart(Authentication authentication) {
        try {
            User user = getAuthenticatedUser(authentication);
            CartResponse cart = cartService.getCart(user);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi xác thực: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi lấy giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Thêm sản phẩm vào giỏ hàng - YÊU CẦU ĐĂNG NHẬP
     */
    @PostMapping("/add")
    @Operation(summary = "Thêm vào giỏ hàng", description = "Thêm sản phẩm vào giỏ hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thêm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc hết hàng"),
            @ApiResponse(responseCode = "401", description = "Chưa đăng nhập"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<?> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request) {
        try {
            User user = getAuthenticatedUser(authentication);
            CartResponse cart = cartService.addToCart(user, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            if (e.getMessage().contains("đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật số lượng item trong giỏ - YÊU CẦU ĐĂNG NHẬP
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            Authentication authentication,
            @Valid @RequestBody UpdateCartItemRequest request) {
        try {
            User user = getAuthenticatedUser(authentication);
            CartResponse cart = cartService.updateCartItem(user, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            if (e.getMessage().contains("đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa item khỏi giỏ hàng - YÊU CẦU ĐĂNG NHẬP
     */
    @DeleteMapping("/remove/{cartItemId}")
    @Operation(summary = "Xóa item khỏi giỏ", description = "Xóa một sản phẩm khỏi giỏ hàng")
    public ResponseEntity<?> removeFromCart(
            Authentication authentication,
            @Parameter(description = "ID của cart item") @PathVariable Long cartItemId) {
        try {
            User user = getAuthenticatedUser(authentication);
            CartResponse cart = cartService.removeFromCart(user, cartItemId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            if (e.getMessage().contains("đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng - YÊU CẦU ĐĂNG NHẬP
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            User user = getAuthenticatedUser(authentication);
            cartService.clearCart(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Lỗi xác thực: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi xóa giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng items trong giỏ - YÊU CẦU ĐĂNG NHẬP
     */
    @GetMapping("/count")
    public ResponseEntity<?> getCartItemCount(Authentication authentication) {
        try {
            User user = getAuthenticatedUser(authentication);
            Integer count = cartService.getCartItemCount(user);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            // Nếu chưa đăng nhập, trả về 0 thay vì lỗi
            return ResponseEntity.ok(0);
        } catch (Exception e) {
            log.error("Lỗi khi lấy số lượng giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Áp dụng mã giảm giá - YÊU CẦU ĐĂNG NHẬP
     */
    @PostMapping("/apply-promo")
    @Operation(summary = "Áp dụng mã giảm giá", description = "Áp dụng mã giảm giá cho giỏ hàng")
    public ResponseEntity<?> applyPromoCode(
            Authentication authentication,
            @RequestParam String promoCode) {
        try {
            User user = getAuthenticatedUser(authentication);
            String cartId = "user_" + user.getId();
            CartResponse cart = cartService.applyPromoCode(cartId, promoCode);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            log.error("Mã giảm giá không hợp lệ: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi áp dụng mã giảm giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa mã giảm giá - YÊU CẦU ĐĂNG NHẬP
     */
    @DeleteMapping("/remove-promo")
    @Operation(summary = "Xóa mã giảm giá", description = "Xóa mã giảm giá đã áp dụng")
    public ResponseEntity<?> removePromoCode(Authentication authentication) {
        try {
            User user = getAuthenticatedUser(authentication);
            String cartId = "user_" + user.getId();
            CartResponse cart = cartService.removePromoCode(cartId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("đăng nhập")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi xóa mã giảm giá: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
