/**
 * Address Page JavaScript
 * Handles address CRUD operations and modal management
 */

document.addEventListener('DOMContentLoaded', loadAddresses);

async function loadAddresses() {
    const container = document.getElementById('addressesList');
    
    try {
        const addresses = await AddressAPI.getUserAddresses();

        if (!addresses || addresses.length === 0) {
            container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 40px 20px; color: #999;">Bạn chưa có địa chỉ nào. Hãy thêm địa chỉ mới.</p>';
            return;
        }

        container.innerHTML = addresses.map(addr => `
          <div class="address-item">
            <div class="address-item__content">
              <div class="address-item__header">
                <span class="address-item__name">${addr.recipientName}</span>
                <span class="address-item__divider">|</span>
                <span class="address-item__phone">(+84) ${addr.phone.replace(/^0/, '')}</span>
              </div>
              <p class="address-item__line">${addr.addressLine1}</p>
              <p class="address-item__city">${addr.city}${addr.state ? ', ' + addr.state : ''}</p>
              ${addr.isDefault ? '<span class="address-item__badge">Mặc định</span>' : ''}
            </div>
            <div class="address-item__actions">
              <a href="javascript:void(0)" onclick="editAddress(${addr.id})" class="address-action-link">Cập nhật</a>
              ${!addr.isDefault ? `<a href="javascript:void(0)" onclick="deleteAddress(${addr.id})" class="address-action-link address-action-link--danger">Xóa</a>` : ''}
              ${!addr.isDefault ? `<button type="button" onclick="setDefault(${addr.id})" class="btn btn--outline-dark btn--small">Thiết lập mặc định</button>` : ''}
            </div>
          </div>
        `).join('');
    } catch (error) {
        console.error('Error loading addresses:', error);
        container.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 40px 20px; color: #dc3545;">Lỗi tải địa chỉ. Vui lòng đăng nhập và thử lại.</p>';
    }
}

function showAddAddressModal() {
    console.log('showAddAddressModal called');
    const modal = document.getElementById('addressModal');
    if (!modal) {
        console.error('Modal element not found!');
        alert('Lỗi: Không tìm thấy modal!');
        return;
    }
    document.getElementById('modalTitle').textContent = 'Địa chỉ mới';
    document.getElementById('addressForm').reset();
    document.getElementById('addressId').value = '';
    modal.classList.add('show');
    console.log('Modal show class added');
}

async function editAddress(id) {
    const address = await AddressAPI.getAddress(id);
    if (!address) {
        alert('Không thể tải địa chỉ');
        return;
    }

    document.getElementById('modalTitle').textContent = 'Chỉnh Sửa Địa Chỉ';
    document.getElementById('addressId').value = address.id;
    document.getElementById('fullName').value = address.recipientName;
    document.getElementById('phone').value = address.phone;
    document.getElementById('addressLine').value = address.addressLine1;
    document.getElementById('city').value = address.city;
    document.getElementById('isDefault').checked = address.isDefault;
    document.getElementById('addressModal').classList.add('show');
}

function closeModal() {
    document.getElementById('addressModal').classList.remove('show');
}

async function saveAddress(e) {
    e.preventDefault();

    const phone = document.getElementById('phone').value.trim();
    
    // Validate phone number format
    if (!/^[0-9]{10,11}$/.test(phone)) {
        alert('Số điện thoại phải từ 10-11 chữ số (chỉ số, không có ký tự khác)');
        document.getElementById('phone').focus();
        return;
    }

    const addressId = document.getElementById('addressId').value;
    const data = {
        recipientName: document.getElementById('fullName').value.trim(),
        phone: phone,
        addressLine1: document.getElementById('addressLine').value.trim(),
        city: document.getElementById('city').value.trim(),
        state: '',
        postalCode: '',
        country: 'Vietnam',
        isDefault: document.getElementById('isDefault').checked
    };

    try {
        if (addressId) {
            await AddressAPI.updateAddress(addressId, data);
        } else {
            await AddressAPI.createAddress(data);
        }
        closeModal();
        await loadAddresses();
    } catch (error) {
        console.error('Error saving address:', error);
    }
}

async function deleteAddress(id) {
    if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        try {
            await AddressAPI.deleteAddress(id);
            await loadAddresses();
        } catch (error) {
            console.error('Error deleting address:', error);
        }
    }
}

async function setDefault(id) {
    try {
        await AddressAPI.setDefaultAddress(id);
        await loadAddresses();
    } catch (error) {
        console.error('Error setting default address:', error);
    }
}
