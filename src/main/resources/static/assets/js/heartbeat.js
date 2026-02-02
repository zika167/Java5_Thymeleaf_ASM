/**
 * Heartbeat - Duy trì trạng thái online
 * Tự động ping server mỗi 2 phút để cập nhật activity log
 */
(function() {
    'use strict';
    
    // Chỉ chạy heartbeat cho user đã đăng nhập (không phải trang admin)
    const isAdminPage = window.location.pathname.startsWith('/admin');
    if (isAdminPage) {
        return; // Không chạy heartbeat ở trang admin
    }
    
    // Ping server mỗi 2 phút (120000ms)
    const HEARTBEAT_INTERVAL = 120000;
    
    function sendHeartbeat() {
        // Tạo một request nhẹ để trigger interceptor
        fetch('/api/heartbeat', {
            method: 'GET',
            headers: {
                'X-Heartbeat': 'true'
            }
        }).catch(() => {
            // Ignore errors - không quan trọng nếu heartbeat fail
        });
    }
    
    // Bắt đầu heartbeat sau khi page load
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            setInterval(sendHeartbeat, HEARTBEAT_INTERVAL);
        });
    } else {
        setInterval(sendHeartbeat, HEARTBEAT_INTERVAL);
    }
    
    // Gửi heartbeat khi user tương tác (click, scroll, keypress)
    let lastActivityTime = Date.now();
    const ACTIVITY_THROTTLE = 60000; // Chỉ gửi tối đa 1 lần/phút khi có activity
    
    function onUserActivity() {
        const now = Date.now();
        if (now - lastActivityTime > ACTIVITY_THROTTLE) {
            lastActivityTime = now;
            sendHeartbeat();
        }
    }
    
    // Lắng nghe các sự kiện user activity
    ['click', 'scroll', 'keypress', 'mousemove'].forEach(event => {
        document.addEventListener(event, onUserActivity, { passive: true, once: false });
    });
    
})();
