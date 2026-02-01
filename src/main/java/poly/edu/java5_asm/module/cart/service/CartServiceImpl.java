package poly.edu.java5_asm.module.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.constant.ErrorMessages;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.module.cart.dto.request.AddToCartRequest;
import poly.edu.java5_asm.module.cart.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.module.cart.dto.response.CartItemResponse;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.cart.entity.CartItem;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;
import poly.edu.java5_asm.module.cart.repository.CartRepository;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation của CartService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public Cart getOrCreateGuestCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .sessionId(sessionId)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public Cart getOrCreateCartByIdentifier(String identifier) {
        if (identifier.startsWith("user_")) {
            Long userId = Long.parseLong(identifier.substring(5));
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                return getOrCreateCart(userOpt.get());
            }
            return getOrCreateGuestCart(identifier);
        } else {
            String sessionId = identifier.substring(6);
            return getOrCreateGuestCart(sessionId);
        }
    }

    @Override
    @Transactional
    public CartResponse addToCart(User user, AddToCartRequest request) {
        Cart cart = getOrCreateCart(user);
        return addToCartInternal(cart, request);
    }

    @Override
    @Transactional
    public CartResponse addToCartByIdentifier(String identifier, AddToCartRequest request) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return addToCartInternal(cart, request);
    }

    private CartResponse addToCartInternal(Cart cart, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(ErrorMessages.QUANTITY_MUST_BE_POSITIVE);
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessages.PRODUCT_INSUFFICIENT_STOCK);
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (product.getStockQuantity() < newQuantity) {
                throw new IllegalArgumentException(ErrorMessages.PRODUCT_INSUFFICIENT_STOCK);
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
            log.info("Cập nhật sản phẩm {} trong giỏ hàng, số lượng mới: {}", product.getId(), newQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                    .build();
            cartItemRepository.save(newItem);
            log.info("Thêm sản phẩm {} vào giỏ hàng, số lượng: {}", product.getId(), request.getQuantity());
        }

        return getCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(User user, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart(user);
        return updateCartItemInternal(cart, request);
    }

    @Override
    @Transactional
    public CartResponse updateCartItemByIdentifier(String identifier, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return updateCartItemInternal(cart, request);
    }

    private CartResponse updateCartItemInternal(Cart cart, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.CART_ITEM_NOT_FOUND));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException(ErrorMessages.CART_ITEM_NOT_BELONG);
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(ErrorMessages.QUANTITY_MUST_BE_POSITIVE);
        }

        if (item.getProduct().getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessages.PRODUCT_INSUFFICIENT_STOCK);
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        log.info("Cập nhật item {} số lượng: {}", item.getId(), request.getQuantity());

        return getCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);
        return removeFromCartInternal(cart, cartItemId);
    }

    @Override
    @Transactional
    public CartResponse removeFromCartByIdentifier(String identifier, Long cartItemId) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return removeFromCartInternal(cart, cartItemId);
    }

    private CartResponse removeFromCartInternal(Cart cart, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.CART_ITEM_NOT_FOUND));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException(ErrorMessages.CART_ITEM_NOT_BELONG);
        }

        cartItemRepository.delete(item);
        log.info("Xóa item {} khỏi giỏ hàng", cartItemId);

        return getCartResponse(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
        log.info("Xóa toàn bộ giỏ hàng của user {}", user.getId());
    }

    @Override
    @Transactional
    public void clearCartByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        cartItemRepository.deleteByCart(cart);
        log.info("Xóa toàn bộ giỏ hàng {}", identifier);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return getCartResponse(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return getCartResponse(cart);
    }

    private CartResponse getCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);

        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> CartItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .productImage(item.getProduct().getImageUrl())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPrice = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .totalItems(items.size())
                .totalPrice(totalPrice)
                .promoCode(cart.getPromoCode())
                .discountAmount(calculateDiscount(totalPrice, cart.getPromoCode()))
                .finalPrice(calculateFinalPrice(totalPrice, cart.getPromoCode()))
                .build();
    }

    private BigDecimal calculateDiscount(BigDecimal totalPrice, String promoCode) {
        if (promoCode != null && "GIAM10K".equalsIgnoreCase(promoCode)) {
            // Tính tổng sau thuế VAT 10%
            BigDecimal totalWithTax = totalPrice.multiply(new BigDecimal("1.1"));
            BigDecimal targetPrice = new BigDecimal("10000");
            
            if (totalWithTax.compareTo(targetPrice) > 0) {
                // Giảm giá = Tổng sau thuế - 10.000đ
                return totalWithTax.subtract(targetPrice);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateFinalPrice(BigDecimal totalPrice, String promoCode) {
        if (promoCode != null && "GIAM10K".equalsIgnoreCase(promoCode)) {
            // Tổng cuối cùng luôn là 10.000đ (đã bao gồm thuế)
            BigDecimal totalWithTax = totalPrice.multiply(new BigDecimal("1.1"));
            BigDecimal targetPrice = new BigDecimal("10000");
            
            if (totalWithTax.compareTo(targetPrice) > 0) {
                return targetPrice;
            }
        }
        // Nếu không có mã hoặc tổng < 10k thì trả về tổng + thuế
        return totalPrice.multiply(new BigDecimal("1.1"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCartEmpty(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCount(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        // Trả về số sản phẩm riêng biệt (unique items) để đồng bộ với dropdown
        return items.size();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCountByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        // Trả về số sản phẩm riêng biệt (unique items) để đồng bộ với dropdown
        return items.size();
    }

    @Override
    @Transactional
    public CartResponse applyPromoCode(String identifier, String promoCode) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        
        // Kiểm tra mã giảm giá (mã cố định: GIAM10K)
        if (!"GIAM10K".equalsIgnoreCase(promoCode.trim())) {
            throw new IllegalArgumentException("Mã giảm giá không hợp lệ");
        }
        
        // Lưu mã giảm giá vào cart
        cart.setPromoCode(promoCode.trim().toUpperCase());
        cartRepository.save(cart);
        
        log.info("Áp dụng mã giảm giá {} cho giỏ hàng {}", promoCode, identifier);
        return getCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removePromoCode(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        cart.setPromoCode(null);
        cartRepository.save(cart);
        
        log.info("Xóa mã giảm giá cho giỏ hàng {}", identifier);
        return getCartResponse(cart);
    }
}
