/**
 * Reviews API - Xử lý các request liên quan đến đánh giá sản phẩm
 */

class ReviewsAPI {
    constructor() {
        this.baseUrl = '/api/reviews';
    }

    /**
     * Lấy danh sách đánh giá của sản phẩm
     */
    async getProductReviews(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/product/${productId}`);
            if (!response.ok) throw new Error('Failed to fetch reviews');
            return await response.json();
        } catch (error) {
            console.error('Error fetching reviews:', error);
            return [];
        }
    }

    /**
     * Tạo đánh giá mới
     */
    async createReview(productId, rating, title, comment) {
        try {
            const response = await fetch(`${this.baseUrl}/create`, {
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
                const error = await response.json();
                throw new Error(error.message || 'Failed to create review');
            }

            return await response.json();
        } catch (error) {
            console.error('Error creating review:', error);
            throw error;
        }
    }

    /**
     * Cập nhật đánh giá
     */
    async updateReview(reviewId, rating, title, comment) {
        try {
            const response = await fetch(`${this.baseUrl}/${reviewId}`, {
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

            if (!response.ok) throw new Error('Failed to update review');
            return await response.json();
        } catch (error) {
            console.error('Error updating review:', error);
            throw error;
        }
    }

    /**
     * Xóa đánh giá
     */
    async deleteReview(reviewId) {
        try {
            const response = await fetch(`${this.baseUrl}/${reviewId}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error('Failed to delete review');
            return true;
        } catch (error) {
            console.error('Error deleting review:', error);
            throw error;
        }
    }

    /**
     * Lấy đánh giá trung bình của sản phẩm
     */
    async getProductRating(productId) {
        try {
            const response = await fetch(`${this.baseUrl}/product/${productId}/rating`);
            if (!response.ok) throw new Error('Failed to fetch rating');
            return await response.json();
        } catch (error) {
            console.error('Error fetching rating:', error);
            return { averageRating: 0, totalReviews: 0 };
        }
    }
}

// Khởi tạo instance
const reviewsAPI = new ReviewsAPI();

/**
 * Render star rating
 */
function renderStars(rating) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            stars += `<img th:src="@{/assets/icon/star.svg}" alt="" class="review-card__star" />`;
        } else if (i - 0.5 === rating) {
            stars += `<img th:src="@{/assets/icon/half-star.svg}" alt="" class="review-card__star" />`;
        } else {
            stars += `<img th:src="@{/assets/icon/blank-star.svg}" alt="" class="review-card__star" />`;
        }
    }
    return stars;
}

/**
 * Format ngày tháng
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

/**
 * Render review card
 */
function renderReviewCard(review) {
    return `
        <div class="col">
            <div class="review-card">
                <div class="review-card__content">
                    <img src="/assets/img/avatar.jpg" alt="" class="review-card__avatar" />
                    <div class="review-card__info">
                        <h4 class="review-card__title">${review.userName}</h4>
                        <p class="review-card__desc">${review.comment}</p>
                        <small class="review-card__date">${formatDate(review.createdAt)}</small>
                    </div>
                </div>
                <div class="review-card__rating">
                    <div class="review-card__star-list">
                        ${renderStars(review.rating)}
                    </div>
                    <span class="review-card__rating-title">( ${review.rating} ) Review</span>
                </div>
            </div>
        </div>
    `;
}

/**
 * Load và hiển thị reviews
 */
async function loadReviews(productId) {
    try {
        const reviews = await reviewsAPI.getProductReviews(productId);
        const reviewsContainer = document.querySelector('.reviews-list');
        
        if (!reviewsContainer) return;

        if (reviews.length === 0) {
            reviewsContainer.innerHTML = '<p class="text-center">Chưa có đánh giá nào</p>';
            return;
        }

        reviewsContainer.innerHTML = reviews.map(review => renderReviewCard(review)).join('');
    } catch (error) {
        console.error('Error loading reviews:', error);
    }
}

/**
 * Khởi tạo review form
 */
function initReviewForm(productId) {
    const form = document.querySelector('.review-form');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const rating = form.querySelector('input[name="rating"]:checked')?.value;
        const title = form.querySelector('input[name="title"]')?.value;
        const comment = form.querySelector('textarea[name="comment"]')?.value;

        if (!rating || !title || !comment) {
            alert('Vui lòng điền đầy đủ thông tin');
            return;
        }

        try {
            await reviewsAPI.createReview(productId, rating, title, comment);
            alert('Đánh giá của bạn đã được gửi thành công!');
            form.reset();
            loadReviews(productId);
        } catch (error) {
            alert('Lỗi: ' + error.message);
        }
    });
}
