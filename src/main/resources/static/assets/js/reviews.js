/**
 * Review Management JavaScript
 */

// API Base URL
const API_BASE_URL = '/api/reviews';

/**
 * Load reviews cho sản phẩm
 */
async function loadProductReviews(productId, page = 0, size = 10, sortBy = 'createdAt') {
    try {
        const response = await fetch(
            `${API_BASE_URL}/products/${productId}?page=${page}&size=${size}&sortBy=${sortBy}`
        );
        
        if (!response.ok) {
            throw new Error('Failed to load reviews');
        }
        
        const data = await response.json();
        displayReviews(data);
        displayPagination(data);
        
        return data;
    } catch (error) {
        console.error('Error loading reviews:', error);
        showError('Không thể tải đánh giá. Vui lòng thử lại sau.');
    }
}

/**
 * Load rating summary của sản phẩm
 */
async function loadProductRating(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}/rating`);
        
        if (!response.ok) {
            throw new Error('Failed to load rating');
        }
        
        const data = await response.json();
        displayRatingSummary(data);
        
        return data;
    } catch (error) {
        console.error('Error loading rating:', error);
    }
}

/**
 * Kiểm tra user đã review chưa
 */
async function checkUserReview(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}/check`);
        
        if (!response.ok) {
            return { hasReviewed: false, hasPurchased: false };
        }
        
        return await response.json();
    } catch (error) {
        console.error('Error checking review:', error);
        return { hasReviewed: false, hasPurchased: false };
    }
}

/**
 * Tạo review mới
 */
async function createReview(productId, reviewData) {
    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(reviewData)
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Failed to create review');
        }
        
        showSuccess('Đánh giá của bạn đã được gửi thành công!');
        
        // Reload reviews
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');
        if (productId) {
            await loadProductReviews(productId);
            await loadProductRating(productId);
        }
        
        // Reset form
        document.getElementById('reviewForm')?.reset();
        resetStarRating();
        
        return data;
    } catch (error) {
        console.error('Error creating review:', error);
        showError(error.message);
        throw error;
    }
}

/**
 * Xóa review
 */
async function deleteReview(reviewId) {
    if (!confirm('Bạn có chắc chắn muốn xóa đánh giá này?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/${reviewId}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Failed to delete review');
        }
        
        showSuccess('Xóa đánh giá thành công!');
        
        // Reload reviews
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');
        if (productId) {
            await loadProductReviews(productId);
            await loadProductRating(productId);
        }
        
    } catch (error) {
        console.error('Error deleting review:', error);
        showError(error.message);
    }
}

/**
 * Hiển thị danh sách reviews
 */
function displayReviews(data) {
    const container = document.getElementById('reviewsList');
    if (!container) return;
    
    if (!data.reviews || data.reviews.length === 0) {
        container.innerHTML = '<p class="text-center">Chưa có đánh giá nào cho sản phẩm này.</p>';
        return;
    }
    
    container.innerHTML = data.reviews.map(review => `
        <div class="review-card" data-review-id="${review.id}">
            <div class="review-card__header">
                <img src="${review.userAvatar || '/assets/img/avatar.jpg'}" 
                     alt="${review.userName}" 
                     class="review-card__avatar" />
                <div class="review-card__info">
                    <h4 class="review-card__title">
                        ${review.userName}
                        ${review.isVerifiedPurchase ? '<span class="verified-badge">✓ Đã mua hàng</span>' : ''}
                    </h4>
                    <div class="review-card__rating">
                        ${generateStarRating(review.rating)}
                        <span class="review-card__date">${formatDate(review.createdAt)}</span>
                    </div>
                </div>
                <button class="review-card__delete" onclick="deleteReview(${review.id})" 
                        style="display: none;" data-user-id="${review.userId}">
                    <img src="/assets/icon/trash.svg" alt="Delete" />
                </button>
            </div>
            ${review.title ? `<h5 class="review-card__subtitle">${escapeHtml(review.title)}</h5>` : ''}
            ${review.comment ? `<p class="review-card__desc">${escapeHtml(review.comment)}</p>` : ''}
        </div>
    `).join('');
    
    // Show delete button for own reviews (implement based on your auth system)
    showDeleteButtons();
}

/**
 * Hiển thị rating summary
 */
function displayRatingSummary(data) {
    const container = document.getElementById('ratingSummary');
    if (!container) return;
    
    const avgRating = data.averageRating || 0;
    const totalReviews = data.totalReviews || 0;
    
    container.innerHTML = `
        <div class="rating-summary">
            <div class="rating-summary__score">
                <h2 class="rating-summary__number">${avgRating.toFixed(1)}</h2>
                <div class="rating-summary__stars">
                    ${generateStarRating(avgRating)}
                </div>
                <p class="rating-summary__text">${totalReviews} đánh giá</p>
            </div>
            <div class="rating-summary__bars">
                ${[5, 4, 3, 2, 1].map(star => {
                    const count = data.ratingDistribution[star] || 0;
                    const percentage = data.ratingPercentage[star] || 0;
                    return `
                        <div class="rating-bar">
                            <span class="rating-bar__label">${star} sao</span>
                            <div class="rating-bar__track">
                                <div class="rating-bar__fill" style="width: ${percentage}%"></div>
                            </div>
                            <span class="rating-bar__count">${count}</span>
                        </div>
                    `;
                }).join('')}
            </div>
        </div>
    `;
}

/**
 * Hiển thị pagination
 */
function displayPagination(data) {
    const container = document.getElementById('reviewsPagination');
    if (!container) return;
    
    if (data.totalPages <= 1) {
        container.innerHTML = '';
        return;
    }
    
    let html = '<div class="pagination">';
    
    for (let i = 0; i < data.totalPages; i++) {
        const active = i === data.currentPage ? 'active' : '';
        html += `<button class="pagination__btn ${active}" onclick="changePage(${i})">${i + 1}</button>`;
    }
    
    html += '</div>';
    container.innerHTML = html;
}

/**
 * Generate star rating HTML
 */
function generateStarRating(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    
    let html = '';
    
    for (let i = 0; i < fullStars; i++) {
        html += '<img src="/assets/icon/star.svg" alt="star" class="star-icon" />';
    }
    
    if (hasHalfStar) {
        html += '<img src="/assets/icon/half-star.svg" alt="half star" class="star-icon" />';
    }
    
    for (let i = 0; i < emptyStars; i++) {
        html += '<img src="/assets/icon/blank-star.svg" alt="empty star" class="star-icon" />';
    }
    
    return html;
}

/**
 * Format date
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) return 'Hôm nay';
    if (diffDays === 1) return 'Hôm qua';
    if (diffDays < 7) return `${diffDays} ngày trước`;
    if (diffDays < 30) return `${Math.floor(diffDays / 7)} tuần trước`;
    if (diffDays < 365) return `${Math.floor(diffDays / 30)} tháng trước`;
    
    return date.toLocaleDateString('vi-VN');
}

/**
 * Escape HTML to prevent XSS
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

/**
 * Show success message
 */
function showSuccess(message) {
    alert(message); // Replace with your toast/notification system
}

/**
 * Show error message
 */
function showError(message) {
    alert(message); // Replace with your toast/notification system
}

/**
 * Change page
 */
function changePage(page) {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    if (productId) {
        loadProductReviews(productId, page);
    }
}

/**
 * Star rating input handler
 */
function initStarRating() {
    const stars = document.querySelectorAll('.star-rating__star');
    const ratingInput = document.getElementById('ratingInput');
    
    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            const rating = index + 1;
            ratingInput.value = rating;
            updateStarDisplay(rating);
        });
        
        star.addEventListener('mouseenter', () => {
            updateStarDisplay(index + 1);
        });
    });
    
    const container = document.querySelector('.star-rating');
    if (container) {
        container.addEventListener('mouseleave', () => {
            updateStarDisplay(ratingInput.value || 0);
        });
    }
}

function updateStarDisplay(rating) {
    const stars = document.querySelectorAll('.star-rating__star');
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add('active');
        } else {
            star.classList.remove('active');
        }
    });
}

function resetStarRating() {
    const ratingInput = document.getElementById('ratingInput');
    if (ratingInput) {
        ratingInput.value = '';
    }
    updateStarDisplay(0);
}

/**
 * Show delete buttons for user's own reviews
 */
function showDeleteButtons() {
    // This should be implemented based on your authentication system
    // For now, it's a placeholder
}

/**
 * Initialize review form
 */
function initReviewForm() {
    const form = document.getElementById('reviewForm');
    if (!form) return;
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');
        
        if (!productId) {
            showError('Không tìm thấy sản phẩm');
            return;
        }
        
        const rating = document.getElementById('ratingInput').value;
        const title = document.getElementById('reviewTitle').value;
        const comment = document.getElementById('reviewComment').value;
        
        if (!rating) {
            showError('Vui lòng chọn số sao đánh giá');
            return;
        }
        
        try {
            await createReview(productId, {
                rating: parseInt(rating),
                title: title,
                comment: comment
            });
        } catch (error) {
            // Error already handled in createReview
        }
    });
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    
    if (productId) {
        loadProductReviews(productId);
        loadProductRating(productId);
        checkUserReview(productId).then(data => {
            // Handle review status
            const reviewForm = document.getElementById('reviewFormContainer');
            if (reviewForm && data.hasReviewed) {
                reviewForm.innerHTML = '<p class="text-center">Bạn đã đánh giá sản phẩm này rồi.</p>';
            }
        });
    }
    
    initStarRating();
    initReviewForm();
});
