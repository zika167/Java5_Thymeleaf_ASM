/**
 * User Menu & Cart Dropdown Handler
 */

(function() {
    function initUserMenu() {
        // User menu now uses hover only (CSS), click goes to profile
        // No JS needed for toggle
    }
    
    // Update cart badge count
    async function updateCartBadge() {
        try {
            const response = await fetch('/api/cart/count');
            if (response.ok) {
                const count = await response.json();
                const badge = document.getElementById('header-cart-count');
                if (badge) {
                    badge.textContent = count;
                    badge.style.display = count > 0 ? 'flex' : 'none';
                }
            }
        } catch (error) {
            console.error('Could not update cart badge:', error);
        }
    }
    
    // Load cart items for dropdown preview
    async function loadCartDropdown() {
        const listEl = document.getElementById('cart-dropdown-list');
        const countEl = document.getElementById('cart-dropdown-count');
        const badge = document.getElementById('header-cart-count');
        if (!listEl) return;
        
        try {
            const response = await fetch('/api/cart');
            
            if (response.ok) {
                const data = await response.json();
                const items = data.items || [];
                const totalItems = data.totalItems || items.reduce((sum, item) => sum + (item.quantity || 1), 0);
                
                // Update badge
                if (badge) {
                    badge.textContent = totalItems;
                    badge.style.display = totalItems > 0 ? 'flex' : 'none';
                }
                
                if (items.length === 0) {
                    listEl.innerHTML = '<div class="cart-dropdown__empty">Giỏ hàng trống</div>';
                    if (countEl) countEl.textContent = '0 Sản Phẩm';
                    return;
                }
                
                // Show max 5 items
                const displayItems = items.slice(0, 5);
                listEl.innerHTML = displayItems.map(item => `
                    <a href="/product/${item.productId}" class="cart-dropdown__item">
                        <img src="${item.productImage || '/assets/img/product/item-1.png'}" 
                             alt="${item.productName || 'Product'}" class="cart-dropdown__item-img" 
                             onerror="this.src='/assets/img/product/item-1.png'" />
                        <div class="cart-dropdown__item-info">
                            <p class="cart-dropdown__item-name">${item.productName || 'Sản phẩm'}</p>
                        </div>
                        <span class="cart-dropdown__item-price">${formatPrice(item.price || 0)}đ</span>
                    </a>
                `).join('');
                
                if (countEl) countEl.textContent = totalItems + ' Sản Phẩm';
            } else {
                listEl.innerHTML = '<div class="cart-dropdown__empty">Không thể tải giỏ hàng</div>';
            }
        } catch (error) {
            console.error('Could not load cart preview:', error);
            listEl.innerHTML = '<div class="cart-dropdown__empty">Lỗi kết nối</div>';
        }
    }
    
    function formatPrice(price) {
        return new Intl.NumberFormat('vi-VN').format(price);
    }
    
    function init() {
        initUserMenu();
        // Load cart data immediately on page load (this updates badge correctly)
        loadCartDropdown();
        
        // Reload cart dropdown when hovering
        var cartWrap = document.getElementById('cart-dropdown-container');
        if (cartWrap) {
            cartWrap.addEventListener('mouseenter', loadCartDropdown);
        }
    }
    
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
    
    // Expose for external use
    window.CartUI = {
        updateBadge: updateCartBadge,
        loadDropdown: loadCartDropdown
    };
})();
