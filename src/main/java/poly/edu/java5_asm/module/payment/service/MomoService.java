package poly.edu.java5_asm.service;

import poly.edu.java5_asm.dto.response.MomoResponse;
import poly.edu.java5_asm.entity.Order;

import java.util.Map;

/**
 * Service xử lý thanh toán Momo
 */
public interface MomoService {

    /**
     * Tạo URL thanh toán Momo
     * @param order Đơn hàng cần thanh toán
     * @param returnUrl URL callback sau khi thanh toán
     * @param notifyUrl URL nhận IPN từ Momo
     * @return URL redirect đến Momo
     */
    String createPaymentUrl(Order order, String returnUrl, String notifyUrl);

    /**
     * Xác thực chữ ký từ Momo callback
     * @param params Các tham số từ Momo trả về
     * @return true nếu chữ ký hợp lệ
     */
    boolean verifyPayment(Map<String, String> params);

    /**
     * Xử lý callback từ Momo sau khi thanh toán
     * @param params Các tham số từ Momo trả về
     * @return MomoResponse chứa kết quả xử lý
     */
    MomoResponse processCallback(Map<String, String> params);
}
