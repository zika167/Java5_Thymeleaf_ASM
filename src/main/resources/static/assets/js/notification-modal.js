/**
 * Notification Modal - Thay thế alert() bằng modal đẹp
 * Sử dụng: NotificationModal.success('Thành công!'), NotificationModal.error('Lỗi!'), etc.
 */
(function() {
    // Icons SVG
    var icons = {
        success: '<svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><path d="M9 12l2 2 4-4"></path></svg>',
        error: '<svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>',
        warning: '<svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>',
        info: '<svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>'
    };
    
    var defaultTitles = {
        success: 'Thành công',
        error: 'Lỗi',
        warning: 'Cảnh báo',
        info: 'Thông báo'
    };

    window.NotificationModal = {
        _confirmResolve: null,

        show: function(message, type, title) {
            type = type || 'info';
            var modal = document.getElementById('notificationModal');
            var iconEl = document.getElementById('notificationIcon');
            var titleEl = document.getElementById('notificationTitle');
            var messageEl = document.getElementById('notificationMessage');
            
            if (!modal) {
                console.warn('NotificationModal: Modal not found, using alert fallback');
                alert((title || defaultTitles[type]) + '\n\n' + message);
                return;
            }
            
            iconEl.innerHTML = icons[type] || icons.info;
            iconEl.className = 'notification-modal__icon notification-modal__icon--' + type;
            titleEl.textContent = title || defaultTitles[type] || 'Thông báo';
            messageEl.textContent = message;
            
            modal.classList.add('show');
            document.body.style.overflow = 'hidden';
        },

        success: function(message, title) {
            this.show(message, 'success', title);
        },

        error: function(message, title) {
            this.show(message, 'error', title);
        },

        warning: function(message, title) {
            this.show(message, 'warning', title);
        },

        info: function(message, title) {
            this.show(message, 'info', title);
        },

        close: function() {
            var modal = document.getElementById('notificationModal');
            if (modal) {
                modal.classList.remove('show');
                document.body.style.overflow = '';
            }
        },

        showFeatureDev: function() {
            var modal = document.getElementById('featureDevModal');
            if (modal) {
                modal.classList.add('show');
                document.body.style.overflow = 'hidden';
            }
        },

        closeFeatureDev: function() {
            var modal = document.getElementById('featureDevModal');
            if (modal) {
                modal.classList.remove('show');
                document.body.style.overflow = '';
            }
        },

        confirm: function(message, title) {
            var self = this;
            return new Promise(function(resolve) {
                var modal = document.getElementById('confirmModal');
                var titleEl = document.getElementById('confirmTitle');
                var messageEl = document.getElementById('confirmMessage');
                
                if (!modal) {
                    resolve(confirm(message));
                    return;
                }
                
                self._confirmResolve = resolve;
                titleEl.textContent = title || 'Xác nhận';
                messageEl.textContent = message;
                
                modal.classList.add('show');
                document.body.style.overflow = 'hidden';
            });
        },

        acceptConfirm: function() {
            var modal = document.getElementById('confirmModal');
            if (modal) {
                modal.classList.remove('show');
                document.body.style.overflow = '';
            }
            if (this._confirmResolve) {
                this._confirmResolve(true);
                this._confirmResolve = null;
            }
        },

        cancelConfirm: function() {
            var modal = document.getElementById('confirmModal');
            if (modal) {
                modal.classList.remove('show');
                document.body.style.overflow = '';
            }
            if (this._confirmResolve) {
                this._confirmResolve(false);
                this._confirmResolve = null;
            }
        }
    };

    // Event listeners - chạy khi DOM ready
    function initEventListeners() {
        // Auto-bind href="#!" links - sử dụng capturing phase để bắt event trước các handler khác
        document.addEventListener('click', function(e) {
            var link = e.target.closest('a[href="#!"], button.feature-dev');
            if (link) {
                e.preventDefault();
                e.stopPropagation();
                window.NotificationModal.showFeatureDev();
            }
        }, true);

        // Close on ESC
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                window.NotificationModal.closeFeatureDev();
                window.NotificationModal.close();
                window.NotificationModal.cancelConfirm();
            }
        });

        // Close on overlay click
        document.addEventListener('click', function(e) {
            if (e.target.classList.contains('notification-modal__overlay')) {
                window.NotificationModal.closeFeatureDev();
                window.NotificationModal.close();
                window.NotificationModal.cancelConfirm();
            }
        });
    }

    // Init when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initEventListeners);
    } else {
        initEventListeners();
    }
})();
