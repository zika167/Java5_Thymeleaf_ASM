/**
 * Address API Service - Gọi backend API
 * Hỗ trợ CRUD operations cho địa chỉ giao hàng
 */

(function initAddressAPIService() {
    const API_BASE = '/api/addresses';

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
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(addressData)
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error || `HTTP ${response.status}`);
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
                headers: {
                    'Content-Type': 'application/json',
                },
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
                headers: {
                    'Content-Type': 'application/json',
                }
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
                headers: {
                    'Content-Type': 'application/json',
                }
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
     * Hiển thị toast notification
     */
    function showAddressToast(message, type = 'success') {
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

    console.log('✅ Address API Service initialized');
})();
