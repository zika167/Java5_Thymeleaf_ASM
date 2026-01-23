package poly.edu.java5_asm.service;

import poly.edu.java5_asm.dto.response.VNPayResponse;
import poly.edu.java5_asm.entity.Order;

import java.util.Map;

/**
 * Service xử lý thanh toán VNPay
 */
public interface VNPayService {

    /**
     * Tạo URL thanh toán VNPay
     * @param order Đơn hàng cần thanh toán
     * @param ipAddress IP của khách hàng
     * @param returnUrl URL callback sau khi thanh toán
     * @return URL redirect đến VNPay
     */
    String createPaymentUrl(Order order, String ipAddress, String returnUrl);

    /**
     * Xác thực chữ ký từ VNPay callback
     * @param params Các tham số từ VNPay trả về
     * @return true nếu chữ ký hợp lệ
     */
    boolean verifyPayment(Map<String, String> params);

    /**
     * Xử lý callback từ VNPay sau khi thanh toán
     * @param params Các tham số từ VNPay trả về
     * @return VNPayResponse chứa kết quả xử lý
     */
    VNPayResponse processCallback(Map<String, String> params);
}
