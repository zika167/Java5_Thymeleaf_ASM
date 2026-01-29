/**
 * Admin Utilities - SweetAlert2 wrapper functions
 */

// Toast notification (góc trên bên phải)
const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
    }
});

// Success toast
function showSuccess(message) {
    Toast.fire({
        icon: 'success',
        title: message
    });
}

// Error toast
function showError(message) {
    Toast.fire({
        icon: 'error',
        title: message
    });
}

// Warning toast
function showWarning(message) {
    Toast.fire({
        icon: 'warning',
        title: message
    });
}

// Info toast
function showInfo(message) {
    Toast.fire({
        icon: 'info',
        title: message
    });
}

// Confirm dialog
function showConfirm(title, text, confirmText = 'Xác nhận', cancelText = 'Hủy') {
    return Swal.fire({
        title: title,
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#667eea',
        cancelButtonColor: '#6c757d',
        confirmButtonText: confirmText,
        cancelButtonText: cancelText,
        reverseButtons: true
    });
}

// Delete confirm dialog
function showDeleteConfirm(itemName = 'mục này') {
    return Swal.fire({
        title: 'Xác nhận xóa?',
        text: `Bạn có chắc muốn xóa ${itemName}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '🗑️ Xóa',
        cancelButtonText: 'Hủy',
        reverseButtons: true
    });
}

// Loading dialog
function showLoading(title = 'Đang xử lý...') {
    Swal.fire({
        title: title,
        allowOutsideClick: false,
        allowEscapeKey: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
}

// Close loading
function hideLoading() {
    Swal.close();
}

// Success with redirect
function showSuccessAndRedirect(message, url, delay = 1500) {
    Swal.fire({
        icon: 'success',
        title: 'Thành công!',
        text: message,
        timer: delay,
        showConfirmButton: false
    }).then(() => {
        window.location.href = url;
    });
}

// Input dialog
function showInput(title, inputPlaceholder = '', inputValue = '') {
    return Swal.fire({
        title: title,
        input: 'text',
        inputValue: inputValue,
        inputPlaceholder: inputPlaceholder,
        showCancelButton: true,
        confirmButtonColor: '#667eea',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        inputValidator: (value) => {
            if (!value) {
                return 'Vui lòng nhập giá trị!';
            }
        }
    });
}

// Select dialog
function showSelect(title, options, inputValue = '') {
    return Swal.fire({
        title: title,
        input: 'select',
        inputOptions: options,
        inputValue: inputValue,
        showCancelButton: true,
        confirmButtonColor: '#667eea',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    });
}
