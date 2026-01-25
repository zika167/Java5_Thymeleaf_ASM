/**
 * Wishlist API - Xử lý các request liên quan đến danh sách yêu thích
 */

class WishlistAPI {
    constructor() {
        this.baseUrl = '/api/wishlist';
    }

    /**
     * Lấy danh sách wishlist của user
     */
    async getWishlist() {
        try {
            const response = await fetch(`${this.baseUrl}`);
            if (!response.ok) throw new Error('Failed to fetch wishlist');
            return await response.json();
        } catch (error) {
            console.error('Error fetching wishlist:', error);
            return [];
        }
    }

    /**
     * Thêm sản phẩm vào wishlist
     */
    async addToWishlist(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/add`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ productId: productId })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Failed to add to wishlist');
            }

            return await response.json();
        } catch (error) {
            console.error('Error adding to wishlist:', error);
            throw error;
        }
    }

    /**
     * Xóa sản phẩm khỏi wishlist
     */
    async removeFromWishlist(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/remove/${productId}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error('Failed to remove from wishlist');
            return true;
        } catch (error) {
            console.error('Error removing from wishlist:', error);
            throw error;
        }
    }

    /**
     * Kiểm tra sản phẩm có trong wishlist không
     */
    async isInWishlist(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/check/${productId}`);
            if (!response.ok) throw new Error('Failed to check wishlist');
            const data = await response.json();
            return data.inWishlist;
        } catch (error) {
            console.error('Error checking wishlist:', error);
            return false;
        }
    }

    /**
     * Lấy số lượng sản phẩm trong wishlist
     */
    async getWishlistCount() {
        try {
            const response = await fetch(`${this.baseUrl}/count`);
            if (!response.ok) throw new Error('Failed to fetch wishlist count');
            const data = await response.json();
            return data.count;
        } catch (error) {
            console.error('Error fetching wishlist count:', error);
            return 0;
        }
    }
}

// Khởi tạo instance
const wishlistAPI = new WishlistAPI();

/**
 * Toggle wishlist button
 */
async function toggleWishlist(productId, button) {
    try {
        const isInWishlist = await wishlistAPI.isInWishlist(productId);

        if (isInWishlist) {
            await wishlistAPI.removeFromWishlist(productId);
            button.classList.remove('like-btn__liked');
        } else {
            await wishlistAPI.addToWishlist(productId);
            button.classList.add('like-btn__liked');
        }

        // Cập nhật wishlist count
        updateWishlistCount();
    } catch (error) {
        alert('Lỗi: ' + error.message);
    }
}

/**
 * Cập nhật wishlist count trong header
 */
async function updateWishlistCount() {
    try {
        const count = await wishlistAPI.getWishlistCount();
        const countElement = document.querySelector('.wishlist-count');
        if (countElement) {
            countElement.textContent = count;
        }
    } catch (error) {
        console.error('Error updating wishlist count:', error);
    }
}

/**
 * Khởi tạo wishlist buttons
 */
function initWishlistButtons() {
    const buttons = document.querySelectorAll('.like-btn');
    buttons.forEach(button => {
        button.addEventListener('click', async (e) => {
            e.preventDefault();
            const productId = button.dataset.productId;
            if (productId) {
                await toggleWishlist(productId, button);
            }
        });
    });
}

/**
 * Load wishlist page
 */
async function loadWishlistPage() {
    try {
        const wishlist = await wishlistAPI.getWishlist();
        const container = document.querySelector('.wishlist-products');

        if (!container) return;

        if (wishlist.length === 0) {
            container.innerHTML = `
                <div class="text-center py-5">
                    <p>Danh sách yêu thích của bạn trống</p>
                    <a href="/" class="btn btn--primary">Tiếp tục mua sắm</a>
                </div>
            `;
            return;
        }

        container.innerHTML = wishlist.map(product => `
            <div class="col">
                <article class="product-card">
                    <div class="product-card__img-wrap">
                        <a href="/product-detail/${product.id}">
                            <img src="${product.imageUrl}" alt="" class="product-card__thumb" />
                        </a>
                        <button class="like-btn product-card__like-btn like-btn__liked" data-product-id="${product.id}">
                            <img src="/assets/icon/heart.svg" alt="" class="like-btn__icon icon" />
                            <img src="/assets/icon/heart-red.svg" alt="" class="like-btn__icon--liked" />
                        </button>
                    </div>
                    <h3 class="product-card__title">
                        <a href="/product-detail/${product.id}">${product.name}</a>
                    </h3>
                    <p class="product-card__brand">${product.brand?.name || 'N/A'}</p>
                    <div class="product-card__row">
                        <span class="product-card__price">$${product.price}</span>
                        <img src="/assets/icon/star.svg" alt="" class="product-card__star" />
                        <span class="product-card__score">4.3</span>
                    </div>
                    <button class="btn btn--primary btn-block" onclick="addToCart(${product.id})">
                        Thêm vào giỏ hàng
                    </button>
                </article>
            </div>
        `).join('');

        // Khởi tạo wishlist buttons
        initWishlistButtons();
    } catch (error) {
        console.error('Error loading wishlist:', error);
    }
}

// Khởi tạo khi trang load
document.addEventListener('DOMContentLoaded', () => {
    initWishlistButtons();
    updateWishlistCount();
});
