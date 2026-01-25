/**
 * Wishlist API Service - Gọi backend API
 * Hỗ trợ thêm/xóa sản phẩm yêu thích
 */

(function initWishlistAPIService() {
    const API_BASE = '/api/wishlist';

    /**
     * Thêm sản phẩm vào wishlist
     */
    async function addToWishlist(productId) {
        try {
            const response = await fetch(`${API_BASE}/products/${productId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            showWishlistToast('Đã thêm vào danh sách yêu thích');
            return true;
        } catch (error) {
            console.error('❌ Lỗi khi thêm vào wishlist:', error);
            showWishlistToast('Lỗi: ' + error.message, 'error');
            return false;
        }
    }

    /**
     * Xóa sản phẩm khỏi wishlist
     */
    async function removeFromWishlist(productId) {
        try {
            const response = await fetch(`${API_BASE}/products/${productId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            showWishlistToast('Đã xóa khỏi danh sách yêu thích');
            return true;
        } catch (error) {
            console.error('❌ Lỗi khi xóa khỏi wishlist:', error);
            showWishlistToast('Lỗi: ' + error.message, 'error');
            return false;
        }
    }

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     */
    async function isInWishlist(productId) {
        try {
            const response = await fetch(`${API_BASE}/products/${productId}/check`);
            
            if (!response.ok) {
                return false;
            }

            const data = await response.json();
            return data.inWishlist || false;
        } catch (error) {
            console.error('❌ Lỗi khi kiểm tra wishlist:', error);
            return false;
        }
    }

    /**
     * Lấy số lượng sản phẩm trong wishlist
     */
    async function getWishlistCount() {
        try {
            const response = await fetch(`${API_BASE}/count`);
            
            if (!response.ok) {
                return 0;
            }

            const data = await response.json();
            return data.count || 0;
        } catch (error) {
            console.error('❌ Lỗi khi lấy số lượng wishlist:', error);
            return 0;
        }
    }

    /**
     * Hiển thị toast notification
     */
    function showWishlistToast(message, type = 'success') {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;
        toast.style.cssText = `
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: ${type === 'error' ? '#dc3545' : '#28a745'};
            color: white;
            padding: 15px 20px;
            border-radius: 4px;
            z-index: 9999;
            animation: slideIn 0.3s ease-in-out;
        `;
        
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.style.animation = 'slideOut 0.3s ease-in-out';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    /**
     * Toggle wishlist (thêm hoặc xóa)
     */
    async function toggleWishlist(productId) {
        const isIn = await isInWishlist(productId);
        
        if (isIn) {
            return await removeFromWishlist(productId);
        } else {
            return await addToWishlist(productId);
        }
    }

    // Export functions to global scope
    window.WishlistAPI = {
        addToWishlist,
        removeFromWishlist,
        isInWishlist,
        getWishlistCount,
        toggleWishlist
    };

    console.log('✅ Wishlist API Service initialized');
})();
