package poly.edu.java5_asm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.response.WishlistResponse;
import poly.edu.java5_asm.entity.Product;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.entity.Wishlist;
import poly.edu.java5_asm.exception.*;
import poly.edu.java5_asm.repository.ProductRepository;
import poly.edu.java5_asm.repository.UserRepository;
import poly.edu.java5_asm.repository.WishlistRepository;
import poly.edu.java5_asm.service.WishlistService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation của WishlistService
 * 
 * Improvements:
 * - Custom exceptions thay vì RuntimeException
 * - Product availability validation (isActive, stock)
 * - Caching support
 * - Logging
 * - Better error messages
 * - Batch operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 1. Thêm sản phẩm vào wishlist
     * 
     * Improvements:
     * - Custom exceptions
     * - Product availability validation
     * - Cache eviction
     * - Logging
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public WishlistResponse addToWishlist(Long userId, Long productId) {
        log.info("Adding product {} to wishlist for user {}", productId, userId);
        
        // 1. Kiểm tra đã có trong wishlist chưa
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            log.warn("Product {} already in wishlist for user {}", productId, userId);
            throw new WishlistDuplicateException(userId, productId);
        }

        // 2. Validate user tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new UserNotFoundException(userId);
                });

        // 3. Validate product tồn tại
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found: {}", productId);
                    return new ProductNotFoundException(productId);
                });

        // 4. Kiểm tra product availability (NEW)
        validateProductAvailability(product);

        // 5. Tạo wishlist item mới
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        Wishlist saved = wishlistRepository.save(wishlist);
        log.info("Successfully added product {} to wishlist for user {}", productId, userId);

        return mapToWishlistResponse(saved);
    }

    /**
     * 2. Xóa sản phẩm khỏi wishlist
     * 
     * Improvements:
     * - Custom exceptions
     * - Cache eviction
     * - Logging
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public void removeFromWishlist(Long userId, Long productId) {
        log.info("Removing product {} from wishlist for user {}", productId, userId);
        
        // Kiểm tra có tồn tại không
        if (!wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            log.warn("Product {} not found in wishlist for user {}", productId, userId);
            throw new WishlistNotFoundException(userId, productId);
        }

        // Xóa
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
        log.info("Successfully removed product {} from wishlist for user {}", productId, userId);
    }

    /**
     * 3. Lấy danh sách wishlist của user
     * 
     * Improvements:
     * - Custom exceptions
     * - Caching
     * - Logging
     */
    @Override
    @Cacheable(value = "wishlist", key = "#userId")
    public List<WishlistResponse> getUserWishlist(Long userId) {
        log.debug("Getting wishlist for user {}", userId);
        
        // Validate user tồn tại
        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw new UserNotFoundException(userId);
        }

        // Lấy wishlist với JOIN FETCH (tránh N+1 query)
        List<Wishlist> wishlists = wishlistRepository.findByUserIdWithProduct(userId);
        log.debug("Found {} items in wishlist for user {}", wishlists.size(), userId);

        return wishlists.stream()
                .map(this::mapToWishlistResponse)
                .collect(Collectors.toList());
    }

    /**
     * 3b. Lấy wishlist với pagination
     * 
     * Improvements:
     * - Custom exceptions
     * - Logging
     */
    @Override
    public Page<WishlistResponse> getUserWishlistPaginated(Long userId, int page, int size) {
        log.debug("Getting paginated wishlist for user {} (page: {}, size: {})", userId, page, size);
        
        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw new UserNotFoundException(userId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Wishlist> wishlistPage = wishlistRepository.findByUserIdWithProductPaginated(userId, pageable);
        
        log.debug("Found {} items in page {} for user {}", 
                wishlistPage.getNumberOfElements(), page, userId);

        return wishlistPage.map(this::mapToWishlistResponse);
    }

    /**
     * 4. Kiểm tra sản phẩm có trong wishlist không
     */
    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

    /**
     * 5. Xóa toàn bộ wishlist
     * 
     * Improvements:
     * - Custom exceptions
     * - Cache eviction
     * - Logging
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public void clearWishlist(Long userId) {
        log.info("Clearing wishlist for user {}", userId);
        
        // Validate user tồn tại
        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw new UserNotFoundException(userId);
        }

        long count = wishlistRepository.countByUserId(userId);
        
        // Xóa tất cả
        wishlistRepository.deleteByUserId(userId);
        log.info("Successfully cleared {} items from wishlist for user {}", count, userId);
    }

    /**
     * Bonus: Đếm số items trong wishlist
     */
    @Override
    public long getWishlistCount(Long userId) {
        return wishlistRepository.countByUserId(userId);
    }

    /**
     * Bonus: Toggle wishlist (add nếu chưa có, remove nếu đã có)
     * 
     * Improvements:
     * - Cache eviction
     * - Logging
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public boolean toggleWishlist(Long userId, Long productId) {
        log.info("Toggling wishlist for user {} and product {}", userId, productId);
        
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            // Đã có → Xóa
            removeFromWishlist(userId, productId);
            log.info("Removed product {} from wishlist for user {}", productId, userId);
            return false; // Removed
        } else {
            // Chưa có → Thêm
            addToWishlist(userId, productId);
            log.info("Added product {} to wishlist for user {}", productId, userId);
            return true; // Added
        }
    }

    /**
     * Bonus: Lấy danh sách product IDs trong wishlist
     */
    @Override
    public List<Long> getWishlistProductIds(Long userId) {
        return wishlistRepository.findProductIdsByUserId(userId);
    }

    /**
     * Map Wishlist entity sang WishlistResponse DTO
     */
    private WishlistResponse mapToWishlistResponse(Wishlist wishlist) {
        Product product = wishlist.getProduct();

        return WishlistResponse.builder()
                .id(wishlist.getId())
                .createdAt(wishlist.getCreatedAt())
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productDiscountPrice(product.getDiscountPrice())
                .productImageUrl(product.getImageUrl())
                .productStock(product.getStockQuantity())
                .productCategoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .productBrandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .build();
    }

    /**
     * Validate product availability (NEW)
     * 
     * Kiểm tra:
     * - Product có active không
     * - Product có out of stock không
     * - Product có stock quantity > 0 không
     */
    private void validateProductAvailability(Product product) {
        // Kiểm tra product có active không
        if (product.getIsActive() != null && !product.getIsActive()) {
            log.warn("Product {} is not active", product.getId());
            throw new ProductUnavailableException(product.getId(), "Sản phẩm không còn khả dụng");
        }

        // Kiểm tra product có out of stock không
        if (product.getIsOutOfStock() != null && product.getIsOutOfStock()) {
            log.warn("Product {} is out of stock", product.getId());
            throw new ProductUnavailableException(product.getId(), "Sản phẩm đã hết hàng");
        }

        // Kiểm tra stock quantity
        if (product.getStockQuantity() != null && product.getStockQuantity() <= 0) {
            log.warn("Product {} has no stock (quantity: {})", product.getId(), product.getStockQuantity());
            throw new ProductUnavailableException(product.getId(), "Sản phẩm không còn hàng trong kho");
        }
        
        log.debug("Product {} is available", product.getId());
    }

    /**
     * Batch add products to wishlist (NEW)
     * 
     * Thêm nhiều sản phẩm cùng lúc
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public List<WishlistResponse> addMultipleToWishlist(Long userId, List<Long> productIds) {
        log.info("Adding {} products to wishlist for user {}", productIds.size(), userId);
        
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<WishlistResponse> results = productIds.stream()
                .filter(productId -> !wishlistRepository.existsByUserIdAndProductId(userId, productId))
                .map(productId -> {
                    try {
                        return addToWishlist(userId, productId);
                    } catch (Exception e) {
                        log.error("Failed to add product {} to wishlist: {}", productId, e.getMessage());
                        return null;
                    }
                })
                .filter(result -> result != null)
                .collect(Collectors.toList());

        log.info("Successfully added {} products to wishlist for user {}", results.size(), userId);
        return results;
    }

    /**
     * Batch remove products from wishlist (NEW)
     * 
     * Xóa nhiều sản phẩm cùng lúc
     */
    @Override
    @Transactional
    @CacheEvict(value = "wishlist", key = "#userId")
    public void removeMultipleFromWishlist(Long userId, List<Long> productIds) {
        log.info("Removing {} products from wishlist for user {}", productIds.size(), userId);
        
        // Validate user
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        int removedCount = 0;
        for (Long productId : productIds) {
            try {
                if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
                    wishlistRepository.deleteByUserIdAndProductId(userId, productId);
                    removedCount++;
                }
            } catch (Exception e) {
                log.error("Failed to remove product {} from wishlist: {}", productId, e.getMessage());
            }
        }

        log.info("Successfully removed {} products from wishlist for user {}", removedCount, userId);
    }
}
