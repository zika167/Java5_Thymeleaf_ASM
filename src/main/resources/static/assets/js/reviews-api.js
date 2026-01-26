/**
 * Reviews API Service - Gọi backend API
 * Hỗ trợ CRUD operations cho reviews
 */

(function initReviewsAPIService() {
    const API_BASE = '/api/reviews';

    /**
     * Tạo review mới
     */
    async function createReview(productId, rating, title, comment) {
        try {
            const response = await fetch(`${API_BASE}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    productId: productId,
                    rating: rating,
                    title: title,
                    comment: comment
                })
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error || `HTTP ${response.status}`);
            }

            const review = await response.json();
            showReviewToast('Đánh giá của bạn đã được gửi');
            return review;
        } catch (error) {
            console.error('❌ Lỗi khi tạo đánh giá:', error);
            showReviewToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Cập nhật review
     */
    async function updateReview(reviewId, rating, title, comment) {
        try {
            const response = await fetch(`${API_BASE}/${reviewId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    rating: rating,
                    title: title,
                    comment: comment
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const review = await response.json();
            showReviewToast('Đánh giá đã được cập nhật');
            return review;
        } catch (error) {
            console.error('❌ Lỗi khi cập nhật đánh giá:', error);
            showReviewToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Xóa review
     */
    async function deleteReview(reviewId) {
        try {
            const response = await fetch(`${API_BASE}/${reviewId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            showReviewToast('Đánh giá đã được xóa');
            return true;
        } catch (error) {
            console.error('❌ Lỗi khi xóa đánh giá:', error);
            showReviewToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Lấy danh sách đánh giá của sản phẩm
     */
    async function getProductReviews(productId) {
        try {
            const response = await fetch(`${API_BASE}/product/${productId}`);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const reviews = await response.json();
            return reviews;
        } catch (error) {
            console.error('❌ Lỗi khi lấy danh sách đánh giá:', error);
            return [];
        }
    }

    /**
     * Lấy rating trung bình của sản phẩm
     */
    async function getProductAverageRating(productId) {
        try {
            const response = await fetch(`${API_BASE}/product/${productId}/average-rating`);

            if (!response.ok) {
                return {averageRating: 0, reviewCount: 0};
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('❌ Lỗi khi lấy rating trung bình:', error);
            return {averageRating: 0, reviewCount: 0};
        }
    }

    /**
     * Hiển thị toast notification
     */
    function showReviewToast(message, type = 'success') {
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
     * Render rating stars
     */
    function renderStars(rating) {
        let stars = '';
        for (let i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars += '<img src="/assets/icon/star.svg" alt="" class="review-card__star" />';
            } else {
                stars += '<img src="/assets/icon/blank-star.svg" alt="" class="review-card__star" />';
            }
        }
        return stars;
    }

    // Export functions to global scope
    window.ReviewsAPI = {
        createReview,
        updateReview,
        deleteReview,
        getProductReviews,
        getProductAverageRating,
        renderStars
    };

    console.log('✅ Reviews API Service initialized');
})();
