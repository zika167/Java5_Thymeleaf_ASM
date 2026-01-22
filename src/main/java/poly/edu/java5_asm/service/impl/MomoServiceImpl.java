package poly.edu.java5_asm.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import poly.edu.java5_asm.dto.response.MomoResponse;
import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.repository.OrderRepository;
import poly.edu.java5_asm.service.MomoService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Implementation của MomoService
 * Xử lý tích hợp thanh toán Momo với HMAC SHA256
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MomoServiceImpl implements MomoService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Value("${momo.partner.code:DEMO}")
    private String partnerCode;

    @Value("${momo.access.key:DEMO_ACCESS_KEY}")
    private String accessKey;

    @Value("${momo.secret.key:DEMO_SECRET_KEY}")
    private String secretKey;

    @Value("${momo.api.url:https://test-payment.momo.vn/v2/gateway/api/create}")
    private String momoApiUrl;

    @Value("${momo.return.url:http://localhost:8080/payment/momo/callback}")
    private String defaultReturnUrl;

    private static final String REQUEST_TYPE = "payWithMethod";
    private static final String EXTRA_DATA = "";

    @Override
    public String createPaymentUrl(Order order, String returnUrl, String notifyUrl) {
        log.info("Tạo URL thanh toán Momo cho đơn hàng: {}", order.getOrderNumber());

        String orderId = order.getOrderNumber();
        String requestId = UUID.randomUUID().toString();
        long amount = order.getTotalAmount().longValue();
        String orderInfo = "Thanh toan don hang " + orderId;

        // Tạo raw signature theo format của Momo
        String rawSignature = String.format(
                "accessKey=%s&amount=%d&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                accessKey, amount, EXTRA_DATA, notifyUrl, orderId, orderInfo, partnerCode, returnUrl, requestId, REQUEST_TYPE
        );

        // Tạo signature với HMAC SHA256
        String signature = hmacSHA256(secretKey, rawSignature);

        // Tạo request body
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("partnerName", "Fat C Grocery Store");
        requestBody.put("storeId", partnerCode);
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", returnUrl);
        requestBody.put("ipnUrl", notifyUrl);
        requestBody.put("lang", "vi");
        requestBody.put("requestType", REQUEST_TYPE);
        requestBody.put("autoCapture", true);
        requestBody.put("extraData", EXTRA_DATA);
        requestBody.put("signature", signature);

        try {
            // Gọi API Momo
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(momoApiUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                int resultCode = jsonResponse.get("resultCode").asInt();

                if (resultCode == 0) {
                    String payUrl = jsonResponse.get("payUrl").asText();
                    log.info("Tạo URL thanh toán Momo thành công cho đơn hàng: {}", orderId);
                    return payUrl;
                } else {
                    String message = jsonResponse.get("message").asText();
                    log.error("Lỗi tạo URL Momo: {} - {}", resultCode, message);
                    throw new RuntimeException("Lỗi tạo URL thanh toán Momo: " + message);
                }
            } else {
                throw new RuntimeException("Lỗi kết nối đến Momo API");
            }
        } catch (Exception e) {
            log.error("Lỗi tạo URL thanh toán Momo: {}", e.getMessage());
            throw new RuntimeException("Lỗi tạo URL thanh toán Momo", e);
        }
    }

    @Override
    public boolean verifyPayment(Map<String, String> params) {
        log.info("Xác thực chữ ký Momo");

        String receivedSignature = params.get("signature");
        if (receivedSignature == null || receivedSignature.isEmpty()) {
            log.warn("Không tìm thấy signature trong params");
            return false;
        }

        // Tạo raw signature để verify
        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%s&resultCode=%s&transId=%s",
                accessKey,
                params.getOrDefault("amount", ""),
                params.getOrDefault("extraData", ""),
                params.getOrDefault("message", ""),
                params.getOrDefault("orderId", ""),
                params.getOrDefault("orderInfo", ""),
                params.getOrDefault("orderType", ""),
                params.getOrDefault("partnerCode", ""),
                params.getOrDefault("payType", ""),
                params.getOrDefault("requestId", ""),
                params.getOrDefault("responseTime", ""),
                params.getOrDefault("resultCode", ""),
                params.getOrDefault("transId", "")
        );

        String calculatedSignature = hmacSHA256(secretKey, rawSignature);
        boolean isValid = calculatedSignature.equals(receivedSignature);

        log.info("Kết quả xác thực chữ ký Momo: {}", isValid ? "Hợp lệ" : "Không hợp lệ");
        return isValid;
    }

    @Override
    @Transactional
    public MomoResponse processCallback(Map<String, String> params) {
        log.info("Xử lý callback từ Momo");

        // Xác thực chữ ký
        if (!verifyPayment(params)) {
            log.error("Chữ ký Momo không hợp lệ");
            return MomoResponse.builder()
                    .success(false)
                    .message("Chữ ký không hợp lệ")
                    .resultCode(97)
                    .resultMessage("Invalid signature")
                    .build();
        }

        String orderNumber = params.get("orderId");
        String resultCodeStr = params.get("resultCode");
        String transactionId = params.get("transId");
        String requestId = params.get("requestId");
        String responseTime = params.get("responseTime");
        String amountStr = params.get("amount");
        String message = params.get("message");

        int resultCode = Integer.parseInt(resultCodeStr);
        BigDecimal amount = new BigDecimal(amountStr);

        // Tìm đơn hàng
        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            log.error("Không tìm thấy đơn hàng: {}", orderNumber);
            return MomoResponse.builder()
                    .success(false)
                    .message("Không tìm thấy đơn hàng")
                    .orderNumber(orderNumber)
                    .resultCode(1)
                    .resultMessage("Order not found")
                    .build();
        }

        Order order = orderOpt.get();
        String resultMessage = getResultMessage(resultCode);

        // Kiểm tra mã kết quả
        if (resultCode == 0) {
            // Thanh toán thành công
            order.setPaymentStatus(Order.PaymentStatus.PAID);
            order.setPaymentTransactionId(transactionId);
            order.setPaymentGatewayResponse(params.toString());

            // Tự động xác nhận đơn hàng
            if (order.getStatus() == Order.OrderStatus.PENDING) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
            }

            orderRepository.save(order);
            log.info("Thanh toán Momo thành công cho đơn hàng: {}", orderNumber);

            return MomoResponse.builder()
                    .success(true)
                    .message("Thanh toán thành công")
                    .orderNumber(orderNumber)
                    .transactionId(transactionId)
                    .requestId(requestId)
                    .amount(amount)
                    .responseTime(responseTime)
                    .resultCode(resultCode)
                    .resultMessage(resultMessage)
                    .build();
        } else {
            // Thanh toán thất bại
            order.setPaymentStatus(Order.PaymentStatus.FAILED);
            order.setPaymentGatewayResponse(params.toString());
            orderRepository.save(order);

            log.warn("Thanh toán Momo thất bại cho đơn hàng: {}, mã lỗi: {}", orderNumber, resultCode);

            return MomoResponse.builder()
                    .success(false)
                    .message("Thanh toán thất bại: " + resultMessage)
                    .orderNumber(orderNumber)
                    .transactionId(transactionId)
                    .requestId(requestId)
                    .amount(amount)
                    .responseTime(responseTime)
                    .resultCode(resultCode)
                    .resultMessage(resultMessage)
                    .build();
        }
    }

    /**
     * Tạo HMAC SHA256 hash
     */
    private String hmacSHA256(String key, String data) {
        try {
            Mac hmac256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac256.init(secretKeySpec);
            byte[] hash = hmac256.doFinal(data.getBytes(StandardCharsets.UTF_8));

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
            log.error("Lỗi tạo HMAC SHA256: {}", e.getMessage());
            throw new RuntimeException("Lỗi tạo chữ ký", e);
        }
    }

    /**
     * Lấy message từ result code của Momo
     */
    private String getResultMessage(int resultCode) {
        return switch (resultCode) {
            case 0 -> "Giao dịch thành công";
            case 9000 -> "Giao dịch đã được xác nhận thành công";
            case 8000 -> "Giao dịch đang được xử lý";
            case 7000 -> "Giao dịch đang được xử lý bởi nhà cung cấp";
            case 1000 -> "Giao dịch đã được khởi tạo, chờ người dùng xác nhận";
            case 11 -> "Truy cập bị từ chối";
            case 12 -> "Phiên bản API không được hỗ trợ";
            case 13 -> "Xác thực merchant thất bại";
            case 20 -> "Yêu cầu sai định dạng";
            case 21 -> "Số tiền giao dịch không hợp lệ";
            case 40 -> "RequestId bị trùng";
            case 41 -> "OrderId bị trùng";
            case 42 -> "OrderId không hợp lệ hoặc không tìm thấy";
            case 43 -> "Yêu cầu bị từ chối vì xung đột giao dịch";
            case 1001 -> "Giao dịch thanh toán thất bại do tài khoản không đủ tiền";
            case 1002 -> "Giao dịch bị từ chối bởi nhà phát hành";
            case 1003 -> "Giao dịch bị hủy";
            case 1004 -> "Giao dịch thất bại do số tiền vượt quá hạn mức";
            case 1005 -> "Giao dịch thất bại do url hoặc QR code hết hạn";
            case 1006 -> "Giao dịch thất bại do người dùng từ chối xác nhận";
            case 1007 -> "Giao dịch bị từ chối vì tài khoản không tồn tại hoặc bị khóa";
            case 1026 -> "Giao dịch bị hạn chế theo quy định của Momo";
            case 1080 -> "Giao dịch hoàn tiền bị từ chối. Giao dịch gốc không tồn tại hoặc đã bị hoàn";
            case 1081 -> "Giao dịch hoàn tiền bị từ chối. Giao dịch gốc có thể đã bị hoàn";
            default -> "Lỗi không xác định (mã: " + resultCode + ")";
        };
    }
}
