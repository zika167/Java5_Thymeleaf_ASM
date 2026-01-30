/**
 * Cart API Service - Gọi backend API thay vì localStorage
 * Hỗ trợ cả guest user (dùng session ID) và logged-in user
 */

(function initCartAPIService() {
    const API_BASE = '/api/cart';

    /**
     * Get CSRF token from meta tags
     */
    function getCsrfToken() {
        const tokenMeta = document.querySelector('meta[name="_csrf"]');
        const headerMeta = document.querySelector('meta[name="_csrf_header"]');
        if (tokenMeta && headerMeta) {
            return { token: tokenMeta.content, header: headerMeta.content };
        }
        return null;
    }

    /**
     * Add CSRF headers to request
     */
    function addCsrfHeaders(headers = {}) {
        const csrf = getCsrfToken();
        if (csrf) {
            headers[csrf.header] = csrf.token;
        }
        return headers;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (gọi backend API)
     */
    async function addToCart(productId, quantity = 1) {
        try {
            const response = await fetch(`${API_BASE}/add`, {
                method: 'POST',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                }),
                body: JSON.stringify({
                    productId: productId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const cartData = await response.json();
            showCartToast('Đã thêm vào giỏ hàng');
            updateHeaderCartUI(cartData);
            return cartData;
        } catch (error) {
            console.error('❌ Lỗi khi thêm vào giỏ hàng:', error);
            showCartToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Lấy giỏ hàng hiện tại
     */
    async function getCart() {
        try {
            const response = await fetch(`${API_BASE}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const cartData = await response.json();
            return cartData;
        } catch (error) {
            console.error('❌ Lỗi khi lấy giỏ hàng:', error);
            return null;
        }
    }

    /**
     * Cập nhật số lượng item
     */
    async function updateCartItem(cartItemId, quantity) {
        try {
            const response = await fetch(`${API_BASE}/update`, {
                method: 'PUT',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                }),
                body: JSON.stringify({
                    cartItemId: cartItemId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const cartData = await response.json();
            updateHeaderCartUI(cartData);
            return cartData;
        } catch (error) {
            console.error('❌ Lỗi khi cập nhật giỏ hàng:', error);
            showCartToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Xóa item khỏi giỏ
     */
    async function removeFromCart(cartItemId) {
        try {
            const response = await fetch(`${API_BASE}/remove/${cartItemId}`, {
                method: 'DELETE',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const cartData = await response.json();
            showCartToast('Đã xóa khỏi giỏ hàng');
            updateHeaderCartUI(cartData);
            return cartData;
        } catch (error) {
            console.error('❌ Lỗi khi xóa khỏi giỏ:', error);
            showCartToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    async function clearCart() {
        try {
            const response = await fetch(`${API_BASE}/clear`, {
                method: 'DELETE',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            showCartToast('Đã xóa toàn bộ giỏ hàng');
            updateHeaderCartUI({items: [], totalItems: 0, totalPrice: 0});
        } catch (error) {
            console.error('❌ Lỗi khi xóa giỏ hàng:', error);
            showCartToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Lấy số lượng items trong giỏ
     */
    async function getCartItemCount() {
        try {
            const response = await fetch(`${API_BASE}/count`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const count = await response.json();
            return count;
        } catch (error) {
            console.error('❌ Lỗi khi lấy số lượng:', error);
            return 0;
        }
    }

    /**
     * Hiển thị toast notification
     */
    function showCartToast(message, type = 'success') {
        let toast = document.createElement('div');
        toast.textContent = message;
        toast.style.position = 'fixed';

        const headerEl = document.querySelector('#header');
        const headerHeight = headerEl ? headerEl.getBoundingClientRect().height : 0;

        toast.style.right = '30px';
        toast.style.top = `${Math.max(0, headerHeight) + 30}px`;
        toast.style.padding = '12px 16px';
        toast.style.background = type === 'error' ? '#dc3545' : '#2a7d2e';
        toast.style.color = '#fff';
        toast.style.borderRadius = '8px';
        toast.style.boxShadow = '0 2px 8px rgba(0,0,0,0.15)';
        toast.style.zIndex = '9999';
        toast.style.fontSize = '14px';
        toast.style.fontWeight = '500';

        document.body.appendChild(toast);

        setTimeout(() => {
            toast.style.transition = 'opacity .3s';
            toast.style.opacity = '0';
            setTimeout(() => toast.remove(), 300);
        }, 2000);
    }

    /**
     * Format price to VND (convert USD if needed)
     */
    function formatPriceVND(price) {
        let p = parseFloat(price) || 0;
        // Convert USD to VND if price < 10000
        if (p > 0 && p < 10000) {
            p = p * 25000;
        }
        return new Intl.NumberFormat('vi-VN').format(p) + 'đ';
    }

    /**
     * Cập nhật UI header giỏ hàng
     */
    function updateHeaderCartUI(cartData) {
        if (!cartData) return;

        const totalItems = cartData.totalItems || 0;
        let totalPrice = parseFloat(cartData.totalPrice) || 0;
        
        // Convert USD to VND if needed
        if (totalPrice > 0 && totalPrice < 10000) {
            totalPrice = totalPrice * 25000;
        }

        // Cập nhật badge số lượng
        document.querySelectorAll('.nav-btn__qnt').forEach((el) => {
            el.textContent = String(totalItems);
        });

        // Cập nhật header cart count (new badge)
        const headerCartCount = document.getElementById('header-cart-count');
        if (headerCartCount) {
            if (totalItems > 0) {
                headerCartCount.textContent = String(totalItems);
                headerCartCount.style.display = 'flex';
                // Thêm animation bounce
                headerCartCount.classList.remove('bounce');
                void headerCartCount.offsetWidth; // Force reflow
                headerCartCount.classList.add('bounce');
            } else {
                headerCartCount.textContent = '0';
                headerCartCount.style.display = 'none';
            }
        }

        // Cập nhật cart-badge với animation (legacy support)
        const cartBadge = document.getElementById('cart-badge');
        if (cartBadge) {
            if (totalItems > 0) {
                cartBadge.textContent = String(totalItems);
                cartBadge.style.display = 'flex';
                // Thêm animation bounce
                cartBadge.classList.remove('bounce');
                void cartBadge.offsetWidth; // Force reflow
                cartBadge.classList.add('bounce');
            } else {
                cartBadge.style.display = 'none';
            }
        }

        // Cập nhật cart icon title
        const cartWrap = Array.from(document.querySelectorAll('.top-act__btn-wrap')).find((wrap) =>
            wrap.querySelector('img.top-act__icon[src*="buy"]')
        );

        if (cartWrap) {
            const countSpan = cartWrap.querySelector('.top-act__title');
            if (countSpan) countSpan.textContent = String(totalItems);

            const ddTitle = cartWrap.querySelector('.act-dropdown__title');
            if (ddTitle) ddTitle.textContent = `Bạn có ${totalItems} sản phẩm`;

            // Cập nhật dropdown list
            const listEl = cartWrap.querySelector('.act-dropdown__list');
            if (listEl && cartData.items) {
                listEl.innerHTML = '';
                cartData.items.slice(0, 3).forEach((item) => {
                    const col = document.createElement('div');
                    col.className = 'col';
                    col.innerHTML = `
                        <article class="cart-preview-item">
                            <div class="cart-preview-item__img-wrap">
                                <img src="${item.productImage}" alt="" class="cart-preview-item__thumb" />
                            </div>
                            <h3 class="cart-preview-item__title">${item.productName}</h3>
                            <p class="cart-preview-item__price">${formatPriceVND(item.subtotal)}</p>
                        </article>
                    `;
                    listEl.appendChild(col);
                });
            }

            // Cập nhật bottom summary
            const ddBottom = cartWrap.querySelector('.act-dropdown__bottom');
            if (ddBottom) {
                const rows = ddBottom.querySelectorAll('.act-dropdown__row');
                rows.forEach((row) => {
                    const label = row.querySelector('.act-dropdown__label')?.textContent?.trim();
                    const valueEl = row.querySelector('.act-dropdown__value');
                    if (!valueEl) return;

                    if (label === 'Subtotal' || label === 'Tạm tính') valueEl.textContent = formatPriceVND(totalPrice);
                    if (label === 'Shipping' || label === 'Vận chuyển') valueEl.textContent = 'Miễn phí';
                    if (label === 'Total Price' || label === 'Tổng cộng') valueEl.textContent = formatPriceVND(totalPrice * 1.1);
                });
            }
        }
    }

    /**
     * Bind "Add to Cart" buttons trên product cards
     * Chỉ bind cho các nút KHÔNG có class js-add-to-cart (đã được xử lý bởi products.js)
     */
    function bindAddToCartButtons() {
        document.addEventListener('click', async (e) => {
            // Skip nếu là nút js-add-to-cart (đã được xử lý bởi products.js)
            if (e.target.closest('.js-add-to-cart')) return;
            
            const btn = e.target.closest('.btn--primary[data-product-id]');
            if (!btn) return;

            e.preventDefault();

            const productId = btn.getAttribute('data-product-id');
            if (!productId) return;

            const quantity = parseInt(btn.getAttribute('data-quantity') || '1', 10);

            try {
                await addToCart(parseInt(productId, 10), quantity);
            } catch (error) {
                console.error('Lỗi:', error);
            }
        });
    }

    /**
     * Khởi tạo khi DOM ready
     */
    document.addEventListener('DOMContentLoaded', () => {
        bindAddToCartButtons();
        getCartItemCount().then(count => {
            document.querySelectorAll('.nav-btn__qnt').forEach((el) => {
                el.textContent = String(count);
            });
            
            // Initialize header cart count (new badge)
            const headerCartCount = document.getElementById('header-cart-count');
            if (headerCartCount) {
                if (count > 0) {
                    headerCartCount.textContent = String(count);
                    headerCartCount.style.display = 'flex';
                } else {
                    headerCartCount.textContent = '0';
                    headerCartCount.style.display = 'none';
                }
            }
            
            // Initialize cart badge (legacy)
            const cartBadge = document.getElementById('cart-badge');
            if (cartBadge) {
                if (count > 0) {
                    cartBadge.textContent = String(count);
                    cartBadge.style.display = 'flex';
                } else {
                    cartBadge.style.display = 'none';
                }
            }
        });
    });

    /**
     * Expose API cho global scope
     */
    window.CartAPI = {
        addToCart,
        getCart,
        updateCartItem,
        removeFromCart,
        clearCart,
        getCartItemCount,
        updateHeaderCartUI
    };
})();
