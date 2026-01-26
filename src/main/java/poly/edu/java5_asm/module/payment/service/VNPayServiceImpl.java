package poly.edu.java5_asm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.response.VNPayResponse;
import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.repository.OrderRepository;
import poly.edu.java5_asm.service.VNPayService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation của VNPayService
 * Xử lý tích hợp thanh toán VNPay với HMAC SHA512
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final OrderRepository orderRepository;

    @Value("${vnpay.merchant.id:DEMO}")
    private String vnpTmnCode;

    @Value("${vnpay.secret.key:DEMOSECRETKEY}")
    private String vnpHashSecret;

    @Value("${vnpay.api.url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String vnpPayUrl;

    @Value("${vnpay.return.url:http://localhost:8080/payment/vnpay/callback}")
    private String vnpReturnUrl;

    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_CURRENCY = "VND";
    private static final String VNP_LOCALE = "vn";
    private static final String VNP_ORDER_TYPE = "other";

    @Override
    public String createPaymentUrl(Order order, String ipAddress, String returnUrl) {
        log.info("Tạo URL thanh toán VNPay cho đơn hàng: {}", order.getOrderNumber());

        Map<String, String> vnpParams = new TreeMap<>();

        // Thông tin merchant
        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);

        // Số tiền (VNPay yêu cầu nhân 100, không có phần thập phân)
        long amount = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue();
        vnpParams.put("vnp_Amount", String.valueOf(amount));

        vnpParams.put("vnp_CurrCode", VNP_CURRENCY);
        vnpParams.put("vnp_TxnRef", order.getOrderNumber());
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderNumber());
        vnpParams.put("vnp_OrderType", VNP_ORDER_TYPE);
        vnpParams.put("vnp_Locale", VNP_LOCALE);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpAddr", ipAddress);

        // Thời gian tạo giao dịch
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        // Thời gian hết hạn (15 phút)
        calendar.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // Tạo query string và hash
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder hashDataBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                // Build query string
                queryBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");

                // Build hash data
                hashDataBuilder.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");
            }
        }

        // Remove trailing &
        String queryString = queryBuilder.substring(0, queryBuilder.length() - 1);
        String hashData = hashDataBuilder.substring(0, hashDataBuilder.length() - 1);

        // Tạo secure hash với HMAC SHA512
        String secureHash = hmacSHA512(vnpHashSecret, hashData);
        queryString += "&vnp_SecureHash=" + secureHash;

        String paymentUrl = vnpPayUrl + "?" + queryString;
        log.info("URL thanh toán VNPay đã tạo cho đơn hàng: {}", order.getOrderNumber());

        return paymentUrl;
    }

    @Override
    public boolean verifyPayment(Map<String, String> params) {
        log.info("Xác thực chữ ký VNPay");

        String vnpSecureHash = params.get("vnp_SecureHash");
        if (vnpSecureHash == null || vnpSecureHash.isEmpty()) {
            log.warn("Không tìm thấy vnp_SecureHash trong params");
            return false;
        }

        // Loại bỏ các tham số hash để tính toán lại
        Map<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.remove("vnp_SecureHash");
        sortedParams.remove("vnp_SecureHashType");

        // Tạo hash data từ các tham số còn lại
        StringBuilder hashDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                hashDataBuilder.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");
            }
        }

        String hashData = hashDataBuilder.substring(0, hashDataBuilder.length() - 1);
        String calculatedHash = hmacSHA512(vnpHashSecret, hashData);

        boolean isValid = calculatedHash.equalsIgnoreCase(vnpSecureHash);
        log.info("Kết quả xác thực chữ ký: {}", isValid ? "Hợp lệ" : "Không hợp lệ");

        return isValid;
    }

    @Override
    @Transactional
    public VNPayResponse processCallback(Map<String, String> params) {
        log.info("Xử lý callback từ VNPay");

        // Xác thực chữ ký
        if (!verifyPayment(params)) {
            log.error("Chữ ký VNPay không hợp lệ");
            return VNPayResponse.builder()
                    .success(false)
                    .message("Chữ ký không hợp lệ")
                    .responseCode("97")
                    .responseMessage("Invalid signature")
                    .build();
        }

        String orderNumber = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionId = params.get("vnp_TransactionNo");
        String bankCode = params.get("vnp_BankCode");
        String paymentDate = params.get("vnp_PayDate");
        String amountStr = params.get("vnp_Amount");

        // Chuyển đổi số tiền (VNPay trả về đã nhân 100)
        BigDecimal amount = BigDecimal.ZERO;
        if (amountStr != null && !amountStr.isEmpty()) {
            amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
        }

        // Tìm đơn hàng
        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            log.error("Không tìm thấy đơn hàng: {}", orderNumber);
            return VNPayResponse.builder()
                    .success(false)
                    .message("Không tìm thấy đơn hàng")
                    .orderNumber(orderNumber)
                    .responseCode("01")
                    .responseMessage("Order not found")
                    .build();
        }

        Order order = orderOpt.get();
        String responseMessage = getResponseMessage(responseCode);

        // Kiểm tra mã phản hồi
        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            order.setPaymentStatus(Order.PaymentStatus.PAID);
            order.setPaymentTransactionId(transactionId);
            order.setPaymentGatewayResponse(params.toString());

            // Tự động xác nhận đơn hàng
            if (order.getStatus() == Order.OrderStatus.PENDING) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
            }

            orderRepository.save(order);
            log.info("Thanh toán thành công cho đơn hàng: {}", orderNumber);

            return VNPayResponse.builder()
                    .success(true)
                    .message("Thanh toán thành công")
                    .orderNumber(orderNumber)
                    .transactionId(transactionId)
                    .bankCode(bankCode)
                    .amount(amount)
                    .paymentDate(paymentDate)
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        } else {
            // Thanh toán thất bại
            order.setPaymentStatus(Order.PaymentStatus.FAILED);
            order.setPaymentGatewayResponse(params.toString());
            orderRepository.save(order);

            log.warn("Thanh toán thất bại cho đơn hàng: {}, mã lỗi: {}", orderNumber, responseCode);

            return VNPayResponse.builder()
                    .success(false)
                    .message("Thanh toán thất bại: " + responseMessage)
                    .orderNumber(orderNumber)
                    .transactionId(transactionId)
                    .bankCode(bankCode)
                    .amount(amount)
                    .paymentDate(paymentDate)
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        }
    }

    /**
     * Tạo HMAC SHA512 hash
     */
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hash = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Lỗi tạo HMAC SHA512: {}", e.getMessage());
            throw new RuntimeException("Lỗi tạo chữ ký", e);
        }
    }

    /**
     * Lấy message từ response code của VNPay
     */
    private String getResponseMessage(String responseCode) {
        return switch (responseCode) {
            case "00" -> "Giao dịch thành công";
            case "07" -> "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)";
            case "09" -> "Thẻ/Tài khoản chưa đăng ký dịch vụ InternetBanking";
            case "10" -> "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11" -> "Đã hết hạn chờ thanh toán";
            case "12" -> "Thẻ/Tài khoản bị khóa";
            case "13" -> "Mã OTP không chính xác";
            case "24" -> "Khách hàng hủy giao dịch";
            case "51" -> "Tài khoản không đủ số dư";
            case "65" -> "Tài khoản đã vượt quá hạn mức giao dịch trong ngày";
            case "75" -> "Ngân hàng thanh toán đang bảo trì";
            case "79" -> "Nhập sai mật khẩu thanh toán quá số lần quy định";
            case "99" -> "Lỗi không xác định";
            default -> "Lỗi không xác định (mã: " + responseCode + ")";
        };
    }
}
