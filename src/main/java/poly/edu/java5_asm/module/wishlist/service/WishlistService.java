package poly.edu.java5_asm.service;

import org.springframework.data.domain.Page;
import poly.edu.java5_asm.dto.response.WishlistResponse;

import java.util.List;

/**
 * Service interface cho Wishlist operations
 */
public interface WishlistService {

    /**
     * Thêm sản phẩm vào wishlist
     *
     * @param userId    ID người dùng
     * @param productId ID sản phẩm
     * @return WishlistResponse
     * @throws WishlistDuplicateException  nếu sản phẩm đã có trong wishlist
     * @throws UserNotFoundException       nếu không tìm thấy user
     * @throws ProductNotFoundException    nếu không tìm thấy sản phẩm
     * @throws ProductUnavailableException nếu sản phẩm không khả dụng
     */
    WishlistResponse addToWishlist(Long userId, Long productId);

    /**
     * Xóa sản phẩm khỏi wishlist
     *
     * @param userId    ID người dùng
     * @param productId ID sản phẩm
     * @throws WishlistNotFoundException nếu không tìm thấy trong wishlist
     */
    void removeFromWishlist(Long userId, Long productId);

    /**
     * Lấy danh sách wishlist của user
     *
     * @param userId ID người dùng
     * @return List of WishlistResponse
     * @throws UserNotFoundException nếu không tìm thấy user
     */
    List<WishlistResponse> getUserWishlist(Long userId);

    /**
     * Lấy wishlist với pagination
     *
     * @param userId ID người dùng
     * @param page   Số trang (0-based)
     * @param size   Kích thước trang
     * @return Page of WishlistResponse
     * @throws UserNotFoundException nếu không tìm thấy user
     */
    Page<WishlistResponse> getUserWishlistPaginated(Long userId, int page, int size);

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     *
     * @param userId    ID người dùng
     * @param productId ID sản phẩm
     * @return true nếu có trong wishlist, false nếu không
     */
    boolean isInWishlist(Long userId, Long productId);

    /**
     * Xóa toàn bộ wishlist
     *
     * @param userId ID người dùng
     * @throws UserNotFoundException nếu không tìm thấy user
     */
    void clearWishlist(Long userId);

    /**
     * Đếm số items trong wishlist
     *
     * @param userId ID người dùng
     * @return Số lượng items
     */
    long getWishlistCount(Long userId);

    /**
     * Toggle wishlist (add nếu chưa có, remove nếu đã có)
     *
     * @param userId    ID người dùng
     * @param productId ID sản phẩm
     * @return true nếu đã thêm, false nếu đã xóa
     */
    boolean toggleWishlist(Long userId, Long productId);

    /**
     * Lấy danh sách product IDs trong wishlist
     *
     * @param userId ID người dùng
     * @return List of product IDs
     */
    List<Long> getWishlistProductIds(Long userId);

    /**
     * Thêm nhiều sản phẩm vào wishlist (batch operation)
     *
     * @param userId     ID người dùng
     * @param productIds List of product IDs
     * @return List of WishlistResponse
     * @throws UserNotFoundException nếu không tìm thấy user
     */
    List<WishlistResponse> addMultipleToWishlist(Long userId, List<Long> productIds);

    /**
     * Xóa nhiều sản phẩm khỏi wishlist (batch operation)
     *
     * @param userId     ID người dùng
     * @param productIds List of product IDs
     * @throws UserNotFoundException nếu không tìm thấy user
     */
    void removeMultipleFromWishlist(Long userId, List<Long> productIds);
}
