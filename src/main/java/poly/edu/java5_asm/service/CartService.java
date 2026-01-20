package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.AddToCartRequest;
import poly.edu.java5_asm.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.dto.response.CartItemResponse;
import poly.edu.java5_asm.dto.response.CartResponse;
import poly.edu.java5_asm.entity.Cart;
import poly.edu.java5_asm.entity.CartItem;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.CartItemRepository;
import poly.edu.java5_asm.repository.CartRepository;
import poly.edu.java5_asm.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    /**
     * Lấy hoặc tạo giỏ hàng cho user
     */
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

    /**
     * Lấy hoặc tạo giỏ hàng cho guest user (dùng session ID)
     */
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

    /**
     * Lấy giỏ hàng theo identifier (user_id hoặc session_id)
     */
    @Transactional
    public Cart getOrCreateCartByIdentifier(String identifier) {
        if (identifier.startsWith("user_")) {
            // Đây là user đã đăng nhập
            Long userId = Long.parseLong(identifier.substring(5));
            // Tìm user từ ID (giả sử có method này)
            // Tạm thời trả về giỏ hàng theo session
            return getOrCreateGuestCart(identifier);
        } else {
            // Đây là guest user
            String sessionId = identifier.substring(6);
            return getOrCreateGuestCart(sessionId);
        }
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @Transactional
    public CartResponse addToCart(User user, AddToCartRequest request) {
        Cart cart = getOrCreateCart(user);
        return addToCartInternal(cart, request);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng theo identifier (hỗ trợ guest)
     */
    @Transactional
    public CartResponse addToCartByIdentifier(String identifier, AddToCartRequest request) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return addToCartInternal(cart, request);
    }

    /**
     * Logic thêm sản phẩm vào giỏ (dùng chung cho user và guest)
     */
    private CartResponse addToCartInternal(Cart cart, AddToCartRequest request) {
        // Kiểm tra sản phẩm tồn tại
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Kiểm tra số lượng
        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Số lượng tồn kho không đủ");
        }

        // Kiểm tra sản phẩm đã có trong giỏ
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            // Cập nhật số lượng
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Số lượng tồn kho không đủ");
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
            log.info("Cập nhật sản phẩm {} trong giỏ hàng, số lượng mới: {}", product.getId(), newQuantity);
        } else {
            // Tạo item mới
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

    /**
     * Cập nhật số lượng item trong giỏ
     */
    @Transactional
    public CartResponse updateCartItem(User user, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart(user);
        return updateCartItemInternal(cart, request);
    }

    /**
     * Cập nhật số lượng item theo identifier (hỗ trợ guest)
     */
    @Transactional
    public CartResponse updateCartItemByIdentifier(String identifier, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return updateCartItemInternal(cart, request);
    }

    /**
     * Logic cập nhật số lượng (dùng chung)
     */
    private CartResponse updateCartItemInternal(Cart cart, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Item không tồn tại"));

        // Kiểm tra item thuộc giỏ hàng
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item không thuộc giỏ hàng của bạn");
        }

        // Kiểm tra số lượng
        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        if (item.getProduct().getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Số lượng tồn kho không đủ");
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        log.info("Cập nhật item {} số lượng: {}", item.getId(), request.getQuantity());

        return getCartResponse(cart);
    }

    /**
     * Xóa item khỏi giỏ hàng
     */
    @Transactional
    public CartResponse removeFromCart(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);
        return removeFromCartInternal(cart, cartItemId);
    }

    /**
     * Xóa item theo identifier (hỗ trợ guest)
     */
    @Transactional
    public CartResponse removeFromCartByIdentifier(String identifier, Long cartItemId) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return removeFromCartInternal(cart, cartItemId);
    }

    /**
     * Logic xóa item (dùng chung)
     */
    private CartResponse removeFromCartInternal(Cart cart, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item không tồn tại"));

        // Kiểm tra item thuộc giỏ hàng
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item không thuộc giỏ hàng của bạn");
        }

        cartItemRepository.delete(item);
        log.info("Xóa item {} khỏi giỏ hàng", cartItemId);

        return getCartResponse(cart);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
        log.info("Xóa toàn bộ giỏ hàng của user {}", user.getId());
    }

    /**
     * Xóa toàn bộ giỏ theo identifier (hỗ trợ guest)
     */
    @Transactional
    public void clearCartByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        cartItemRepository.deleteByCart(cart);
        log.info("Xóa toàn bộ giỏ hàng {}", identifier);
    }

    /**
     * Lấy thông tin giỏ hàng
     */
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return getCartResponse(cart);
    }

    /**
     * Lấy giỏ hàng theo identifier (hỗ trợ guest)
     */
    @Transactional(readOnly = true)
    public CartResponse getCartByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        return getCartResponse(cart);
    }

    /**
     * Chuyển đổi Cart entity thành CartResponse
     */
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
                .build();
    }

    /**
     * Kiểm tra giỏ hàng có trống không
     */
    @Transactional(readOnly = true)
    public boolean isCartEmpty(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.isEmpty();
    }

    /**
     * Lấy tổng số lượng items trong giỏ
     */
    @Transactional(readOnly = true)
    public Integer getCartItemCount(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Lấy tổng số lượng items theo identifier (hỗ trợ guest)
     */
    @Transactional(readOnly = true)
    public Integer getCartItemCountByIdentifier(String identifier) {
        Cart cart = getOrCreateCartByIdentifier(identifier);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
