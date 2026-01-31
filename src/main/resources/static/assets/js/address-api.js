/**
 * Address API Service - Gọi backend API
 * Hỗ trợ CRUD operations cho địa chỉ giao hàng
 */

(function initAddressAPIService() {
    const API_BASE = '/api/addresses';

    /**
     * Get CSRF token from meta tags or cookie
     */
    function getCsrfToken() {
        // Try to get from meta tag first
        const tokenMeta = document.querySelector('meta[name="_csrf"]');
        const headerMeta = document.querySelector('meta[name="_csrf_header"]');
        if (tokenMeta && headerMeta) {
            return { token: tokenMeta.content, header: headerMeta.content };
        }
        // Try to get from cookie
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [name, value] = cookie.trim().split('=');
            if (name === 'XSRF-TOKEN') {
                return { token: decodeURIComponent(value), header: 'X-XSRF-TOKEN' };
            }
        }
        return null;
    }

    /**
     * Add CSRF headers to request options
     */
    function addCsrfHeaders(headers = {}) {
        const csrf = getCsrfToken();
        if (csrf) {
            headers[csrf.header] = csrf.token;
        }
        return headers;
    }

    /**
     * Lấy tất cả địa chỉ của user
     */
    async function getUserAddresses() {
        try {
            const response = await fetch(`${API_BASE}`);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const addresses = await response.json();
            return addresses;
        } catch (error) {
            console.error('❌ Lỗi khi lấy danh sách địa chỉ:', error);
            return [];
        }
    }

    /**
     * Lấy địa chỉ mặc định
     */
    async function getDefaultAddress() {
        try {
            const response = await fetch(`${API_BASE}/default`);

            if (!response.ok) {
                return null;
            }

            const address = await response.json();
            return address;
        } catch (error) {
            console.error('❌ Lỗi khi lấy địa chỉ mặc định:', error);
            return null;
        }
    }

    /**
     * Lấy địa chỉ theo ID
     */
    async function getAddress(addressId) {
        try {
            const response = await fetch(`${API_BASE}/${addressId}`);

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const address = await response.json();
            return address;
        } catch (error) {
            console.error('❌ Lỗi khi lấy địa chỉ:', error);
            return null;
        }
    }

    /**
     * Tạo địa chỉ mới
     */
    async function createAddress(addressData) {
        try {
            const response = await fetch(`${API_BASE}`, {
                method: 'POST',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                }),
                body: JSON.stringify(addressData)
            });

            if (!response.ok) {
                let errorMessage = `HTTP ${response.status}`;
                try {
                    const errorData = await response.json();
                    if (errorData.message) {
                        errorMessage = errorData.message;
                    } else if (errorData.errors) {
                        errorMessage = Object.values(errorData.errors).join(', ');
                    }
                } catch (e) {
                    const errorText = await response.text();
                    if (errorText) errorMessage = errorText;
                }
                throw new Error(errorMessage);
            }

            const address = await response.json();
            showAddressToast('Địa chỉ đã được thêm');
            return address;
        } catch (error) {
            console.error('❌ Lỗi khi tạo địa chỉ:', error);
            showAddressToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Cập nhật địa chỉ
     */
    async function updateAddress(addressId, addressData) {
        try {
            const response = await fetch(`${API_BASE}/${addressId}`, {
                method: 'PUT',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                }),
                body: JSON.stringify(addressData)
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const address = await response.json();
            showAddressToast('Địa chỉ đã được cập nhật');
            return address;
        } catch (error) {
            console.error('❌ Lỗi khi cập nhật địa chỉ:', error);
            showAddressToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Xóa địa chỉ
     */
    async function deleteAddress(addressId) {
        try {
            const response = await fetch(`${API_BASE}/${addressId}`, {
                method: 'DELETE',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            showAddressToast('Địa chỉ đã được xóa');
            return true;
        } catch (error) {
            console.error('❌ Lỗi khi xóa địa chỉ:', error);
            showAddressToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Set địa chỉ làm mặc định
     */
    async function setDefaultAddress(addressId) {
        try {
            const response = await fetch(`${API_BASE}/${addressId}/set-default`, {
                method: 'PATCH',
                headers: addCsrfHeaders({
                    'Content-Type': 'application/json',
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const address = await response.json();
            showAddressToast('Địa chỉ mặc định đã được cập nhật');
            return address;
        } catch (error) {
            console.error('❌ Lỗi khi set địa chỉ mặc định:', error);
            showAddressToast('Lỗi: ' + error.message, 'error');
            throw error;
        }
    }

    /**
     * Hiển thị thông báo qua NotificationModal
     */
    function showAddressToast(message, type = 'success') {
        if (typeof NotificationModal !== 'undefined') {
            if (type === 'error') {
                NotificationModal.error(message);
            } else {
                NotificationModal.success(message);
            }
        } else {
            // Fallback nếu NotificationModal chưa load
            alert(message);
        }
    }

    // Export functions to global scope
    window.AddressAPI = {
        getUserAddresses,
        getDefaultAddress,
        getAddress,
        createAddress,
        updateAddress,
        deleteAddress,
        setDefaultAddress
    };
})();
