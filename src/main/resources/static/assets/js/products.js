// Format price to VND
function formatVND(price) {
    return new Intl.NumberFormat('vi-VN').format(price) + 'đ';
}

class ProductRenderer {
    constructor(containerSelector) {
        this.container = document.querySelector(containerSelector);
        this.products = [];
        this.filteredProducts = [];
        this.currentPage = 1;
        this.itemsPerPage = 8;
        this.init();
    }

    async init() {
        if (!this.container) return;
        await this.loadProductsFromAPI();
        await this.syncWishlistStatus();
        this.renderProducts();
        this.renderTopRated();
        this.initPagination();
    }

    async loadProductsFromAPI() {
        try {
            const response = await fetch('/api/products?page=0&size=50');
            if (response.ok) {
                const data = await response.json();
                this.products = data.products.map(p => ({
                    id: p.id,
                    name: p.name,
                    brand: p.brandName || 'Unknown',
                    price: p.price,
                    rating: p.averageRating || 0,
                    image: p.imageUrl || './assets/img/product/item-1.png',
                    isLiked: false
                }));
                this.filteredProducts = [...this.products];
                console.log('Loaded products from API:', this.products.length);
            }
        } catch (error) {
            console.error('Could not load products from API:', error);
        }
    }

    async syncWishlistStatus() {
        try {
            const response = await fetch('/api/wishlist');
            if (response.ok) {
                const wishlistItems = await response.json();
                const wishlistProductIds = wishlistItems.map(item => item.productId);
                this.products.forEach(product => {
                    product.isLiked = wishlistProductIds.includes(product.id);
                });
                this.filteredProducts = [...this.products];
            }
        } catch (error) {
            console.log('Could not sync wishlist status');
        }
    }

    // Render top 3 rated products
    renderTopRated() {
        const container = document.getElementById('top-rated-container');
        if (!container || this.products.length === 0) return;

        const topRated = [...this.products]
            .sort((a, b) => b.rating - a.rating)
            .slice(0, 3);

        container.innerHTML = topRated.map(product => {
            const imageUrl = this.getImageUrl(product.image);
            return `
                <div class="col">
                    <a href="/product/${product.id}">
                        <article class="cate-item">
                            <img src="${imageUrl}" alt="${product.name}" class="cate-item__thumb"/>
                            <div class="cate-item__info">
                                <h3 class="cate-item__title">${formatVND(product.price)}</h3>
                                <p class="cate-item__desc">${product.name}</p>
                                <p class="cate-item__rating">⭐ ${product.rating.toFixed(1)}</p>
                            </div>
                        </article>
                    </a>
                </div>
            `;
        }).join('');
    }

    getImageUrl(image) {
        if (!image) return './assets/img/product/item-1.png';
        if (image.startsWith('http')) return image;
        if (image.startsWith('/')) return image;
        return './assets/img/product/item-1.png';
    }

    // Get paginated products
    getPaginatedProducts() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        const end = start + this.itemsPerPage;
        return this.filteredProducts.slice(start, end);
    }

    getTotalPages() {
        return Math.ceil(this.filteredProducts.length / this.itemsPerPage);
    }

    renderProducts() {
        if (!this.container) return;
        
        const paginatedProducts = this.getPaginatedProducts();
        
        if (paginatedProducts.length === 0) {
            this.container.innerHTML = '<div class="col-12 text-center py-5"><p>Không có sản phẩm nào</p></div>';
            this.renderPagination();
            return;
        }
        
        this.container.innerHTML = paginatedProducts.map(product => this.createProductHTML(product)).join('');
        this.initLikeButtons();
        bindAddToCartButtons();
        this.renderPagination();
    }

    createProductHTML(product) {
        const likeClass = product.isLiked ? 'like-btn__liked' : '';
        const rating = Math.round((product.rating || 0) * 10) / 10;
        const imageUrl = this.getImageUrl(product.image);
        return `
            <div class="col">
                <article class="product-card">
                    <div class="product-card__img-wrap">
                        <a href="/product/${product.id}">
                            <img src="${imageUrl}" alt="${product.name}" class="product-card__thumb" />
                        </a>
                        <button class="like-btn product-card__like-btn ${likeClass}" data-product-id="${product.id}">
                            <img src="./assets/icon/heart.svg" alt="" class="like-btn__icon icon" />
                            <img src="./assets/icon/heart-red.svg" alt="" class="like-btn__icon--liked" />
                        </button>
                    </div>
                    <h3 class="product-card__title">
                        <a href="/product/${product.id}">${product.name}</a>
                    </h3>
                    <p class="product-card__brand">${product.brand}</p>
                    <div class="product-card__row">
                        <span class="product-card__price">${formatVND(product.price)}</span>
                        <img src="./assets/icon/star.svg" alt="" class="product-card__star" />
                        <span class="product-card__score">${rating}</span>
                    </div>
                    <div class="product-card__row">
                        <button class="btn btn--primary js-add-to-cart"
                                data-product-id="${product.id}"
                                data-name="${product.name}"
                                data-price="${product.price}"
                                data-image="${imageUrl}">
                            Thêm vào giỏ
                        </button>
                    </div>
                </article>
            </div>
        `;
    }

    initLikeButtons() {
        const likeButtons = this.container.querySelectorAll('.like-btn');
        likeButtons.forEach(button => {
            button.onclick = async (e) => {
                e.preventDefault();
                e.stopPropagation();
                
                const productId = button.getAttribute('data-product-id');
                if (!productId) return;
                
                if (window.WishlistAPI) {
                    try {
                        const result = await window.WishlistAPI.toggleWishlist(productId);
                        if (result !== false) {
                            button.classList.toggle('like-btn__liked');
                            const product = this.products.find(p => p.id === parseInt(productId));
                            if (product) {
                                product.isLiked = !product.isLiked;
                            }
                        }
                    } catch (error) {
                        console.error('Wishlist toggle error:', error);
                    }
                }
            };
        });
    }

    // Pagination
    initPagination() {
        const prevBtn = document.getElementById('prev-page');
        const nextBtn = document.getElementById('next-page');
        
        if (prevBtn) {
            prevBtn.onclick = () => {
                if (this.currentPage > 1) {
                    this.currentPage--;
                    this.renderProducts();
                    this.scrollToProducts();
                }
            };
        }
        
        if (nextBtn) {
            nextBtn.onclick = () => {
                if (this.currentPage < this.getTotalPages()) {
                    this.currentPage++;
                    this.renderProducts();
                    this.scrollToProducts();
                }
            };
        }
    }

    renderPagination() {
        const totalPages = this.getTotalPages();
        const pagesContainer = document.getElementById('pagination-pages');
        const prevBtn = document.getElementById('prev-page');
        const nextBtn = document.getElementById('next-page');
        const paginationContainer = document.getElementById('pagination-container');
        
        // Hide pagination if only 1 page
        if (paginationContainer) {
            paginationContainer.style.display = totalPages <= 1 ? 'none' : 'flex';
        }
        
        if (prevBtn) prevBtn.disabled = this.currentPage <= 1;
        if (nextBtn) nextBtn.disabled = this.currentPage >= totalPages;
        
        if (pagesContainer) {
            let html = '';
            for (let i = 1; i <= totalPages; i++) {
                const activeClass = i === this.currentPage ? 'pagination__page--active' : '';
                html += `<button class="pagination__page ${activeClass}" data-page="${i}">${i}</button>`;
            }
            pagesContainer.innerHTML = html;
            
            // Bind page buttons
            pagesContainer.querySelectorAll('.pagination__page').forEach(btn => {
                btn.onclick = () => {
                    this.currentPage = parseInt(btn.dataset.page);
                    this.renderProducts();
                    this.scrollToProducts();
                };
            });
        }
    }

    scrollToProducts() {
        this.container?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    filterProducts(filters = {}) {
        this.filteredProducts = this.products.filter(product => {
            if (filters.minPrice !== undefined && product.price < filters.minPrice) return false;
            if (filters.maxPrice !== undefined && product.price > filters.maxPrice) return false;
            if (filters.wishlistOnly && !product.isLiked) return false;
            if (filters.minRating !== undefined && product.rating < filters.minRating) return false;
            return true;
        });
        this.currentPage = 1; // Reset to first page after filter
        this.renderProducts();
    }

    searchProducts(query) {
        if (!query.trim()) {
            this.filteredProducts = [...this.products];
        } else {
            this.filteredProducts = this.products.filter(product =>
                product.name.toLowerCase().includes(query.toLowerCase()) ||
                product.brand.toLowerCase().includes(query.toLowerCase())
            );
        }
        this.currentPage = 1;
        this.renderProducts();
    }
}

// Initialize product renderer when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
    const productContainer = document.querySelector('#products-container');
    if (productContainer) {
        window.productRenderer = new ProductRenderer('#products-container');
        console.log('ProductRenderer initialized');
    }
    
    // Bind filter submit button
    const filterSubmitBtn = document.querySelector('.filter__submit');
    if (filterSubmitBtn) {
        filterSubmitBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            applyFilters();
        });
    }
    
    // Bind rating tags
    document.querySelectorAll('.rating-tag').forEach(tag => {
        tag.addEventListener('click', function(e) {
            e.preventDefault();
            document.querySelectorAll('.rating-tag').forEach(t => t.classList.remove('selected'));
            this.classList.add('selected');
            document.getElementById('filter-rating').value = this.dataset.rating;
        });
    });
    
    // Set default rating tag as selected
    const defaultRatingTag = document.querySelector('.rating-tag[data-rating="0"]');
    if (defaultRatingTag) defaultRatingTag.classList.add('selected');
});

// Filter function
function applyFilters() {
    const minPrice = parseFloat(document.getElementById('min-price')?.value) * 1000 || 0;
    const maxPrice = parseFloat(document.getElementById('max-price')?.value) * 1000 || 5000000;
    const wishlistOnly = document.getElementById('filter-wishlist')?.checked || false;
    const minRating = parseFloat(document.getElementById('filter-rating')?.value) || 0;

    if (window.productRenderer) {
        window.productRenderer.filterProducts({
            minPrice: minPrice,
            maxPrice: maxPrice,
            wishlistOnly: wishlistOnly,
            minRating: minRating
        });
    }
    
    document.getElementById('home-filter')?.classList.add('hide');
}

function bindAddToCartButtons() {
    document.querySelectorAll('.js-add-to-cart').forEach((btn) => {
        btn.onclick = async function (e) {
            e.preventDefault();
            const productId = parseInt(this.dataset.productId, 10);
            if (window.CartAPI && typeof window.CartAPI.addToCart === 'function') {
                try {
                    await window.CartAPI.addToCart(productId, 1);
                } catch (error) {
                    console.error('Lỗi khi thêm vào giỏ:', error);
                }
            }
        };
    });
}
