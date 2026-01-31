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
     * Lấy user từ Authentication hoặc session ID cho guest
     */
    private String getCartIdentifier(Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated() 
                && authentication.getPrincipal() instanceof CustomUserDetails) {
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            return "user_" + user.getId();
        }
        // Cho guest user, dùng session ID
        return "guest_" + session.getId();
    }

    /**
     * Lấy giỏ hàng hiện tại
     */
    @GetMapping
    @Operation(summary = "Lấy giỏ hàng", description = "Lấy thông tin giỏ hàng hiện tại của user hoặc guest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<CartResponse> getCart(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.getCartByIdentifier(cartId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            log.error("Lỗi khi lấy giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (hỗ trợ cả guest user)
     */
    @PostMapping("/add")
    @Operation(summary = "Thêm vào giỏ hàng", description = "Thêm sản phẩm vào giỏ hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thêm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc hết hàng"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<CartResponse> addToCart(
            Authentication authentication,
            HttpSession session,
            @Valid @RequestBody AddToCartRequest request) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.addToCartByIdentifier(cartId, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Lỗi khi thêm vào giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật số lượng item trong giỏ
     */
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCartItem(
            Authentication authentication,
            HttpSession session,
            @Valid @RequestBody UpdateCartItemRequest request) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.updateCartItemByIdentifier(cartId, request);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa item khỏi giỏ hàng
     */
    @DeleteMapping("/remove/{cartItemId}")
    @Operation(summary = "Xóa item khỏi giỏ", description = "Xóa một sản phẩm khỏi giỏ hàng")
    public ResponseEntity<CartResponse> removeFromCart(
            Authentication authentication,
            HttpSession session,
            @Parameter(description = "ID của cart item") @PathVariable Long cartItemId) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            CartResponse cart = cartService.removeFromCartByIdentifier(cartId, cartItemId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa khỏi giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            cartService.clearCartByIdentifier(cartId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy số lượng items trong giỏ
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(Authentication authentication, HttpSession session) {
        try {
            String cartId = getCartIdentifier(authentication, session);
            Integer count = cartService.getCartItemCountByIdentifier(cartId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Lỗi khi lấy số lượng giỏ hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
