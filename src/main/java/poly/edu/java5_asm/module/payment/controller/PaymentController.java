package poly.edu.java5_asm.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.java5_asm.dto.response.MomoResponse;
import poly.edu.java5_asm.dto.response.VNPayResponse;
import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.repository.OrderRepository;
import poly.edu.java5_asm.service.MomoService;
import poly.edu.java5_asm.service.VNPayService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller xử lý callback từ các cổng thanh toán
 */
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final VNPayService vnPayService;
    private final MomoService momoService;
    private final OrderRepository orderRepository;

    /**
     * Trang chọn phương thức thanh toán (bước cuối checkout)
     */
    @GetMapping
    public String paymentPage() {
        return "module/payment/payment";
    }

    /**
     * Tạo URL thanh toán VNPay
     */
    @GetMapping("/vnpay/create/{orderNumber}")
    public String createVNPayPayment(@PathVariable String orderNumber,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        log.info("Tạo thanh toán VNPay cho đơn hàng: {}", orderNumber);

        Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/orders";
        }

        String ipAddress = getClientIpAddress(request);
        String returnUrl = getBaseUrl(request) + "/payment/vnpay/callback";

        try {
            String paymentUrl = vnPayService.createPaymentUrl(order, ipAddress, returnUrl);
            return "redirect:" + paymentUrl;
        } catch (Exception e) {
            log.error("Lỗi tạo URL thanh toán VNPay: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Lỗi tạo thanh toán: " + e.getMessage());
            return "redirect:/order/" + orderNumber;
        }
    }

    /**
     * Callback từ VNPay sau khi thanh toán
     */
    @GetMapping("/vnpay/callback")
    public String vnpayCallback(HttpServletRequest request,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("Nhận callback từ VNPay");

        // Lấy tất cả params từ request
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        log.debug("VNPay callback params: {}", params);

        // Xử lý callback
        VNPayResponse response = vnPayService.processCallback(params);

        if (response.isSuccess()) {
            log.info("Thanh toán VNPay thành công cho đơn hàng: {}", response.getOrderNumber());
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            redirectAttributes.addFlashAttribute("transactionId", response.getTransactionId());
            return "redirect:/payment/success?orderNumber=" + response.getOrderNumber();
        } else {
            log.warn("Thanh toán VNPay thất bại: {}", response.getMessage());
            redirectAttributes.addFlashAttribute("error", response.getMessage());
            return "redirect:/payment/failure?orderNumber=" + response.getOrderNumber();
        }
    }

    /**
     * Tạo URL thanh toán Momo
     */
    @GetMapping("/momo/create/{orderNumber}")
    public String createMomoPayment(@PathVariable String orderNumber,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        log.info("Tạo thanh toán Momo cho đơn hàng: {}", orderNumber);

        Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/orders";
        }

        String baseUrl = getBaseUrl(request);
        String returnUrl = baseUrl + "/payment/momo/callback";
        String notifyUrl = baseUrl + "/payment/momo/ipn";

        try {
            String paymentUrl = momoService.createPaymentUrl(order, returnUrl, notifyUrl);
            return "redirect:" + paymentUrl;
        } catch (Exception e) {
            log.error("Lỗi tạo URL thanh toán Momo: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Lỗi tạo thanh toán: " + e.getMessage());
            return "redirect:/order/" + orderNumber;
        }
    }

    /**
     * Callback từ Momo sau khi thanh toán (redirect URL)
     */
    @GetMapping("/momo/callback")
    public String momoCallback(HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        log.info("Nhận callback từ Momo");

        // Lấy tất cả params từ request
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        log.debug("Momo callback params: {}", params);

        // Xử lý callback
        MomoResponse response = momoService.processCallback(params);

        if (response.isSuccess()) {
            log.info("Thanh toán Momo thành công cho đơn hàng: {}", response.getOrderNumber());
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            redirectAttributes.addFlashAttribute("transactionId", response.getTransactionId());
            return "redirect:/payment/success?orderNumber=" + response.getOrderNumber();
        } else {
            log.warn("Thanh toán Momo thất bại: {}", response.getMessage());
            redirectAttributes.addFlashAttribute("error", response.getMessage());
            return "redirect:/payment/failure?orderNumber=" + response.getOrderNumber();
        }
    }

    /**
     * IPN (Instant Payment Notification) từ Momo
     */
    @PostMapping("/momo/ipn")
    @ResponseBody
    public Map<String, Object> momoIpn(@RequestBody Map<String, String> params) {
        log.info("Nhận IPN từ Momo");
        log.debug("Momo IPN params: {}", params);

        MomoResponse response = momoService.processCallback(params);

        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", response.isSuccess() ? 0 : 1);
        result.put("message", response.getMessage());

        return result;
    }

    /**
     * Trang thanh toán thành công
     */
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String orderNumber, Model model) {
        log.info("Hiển thị trang thanh toán thành công cho đơn hàng: {}", orderNumber);

        Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
        if (order != null) {
            model.addAttribute("order", order);
        }
        model.addAttribute("orderNumber", orderNumber);

        return "module/payment/payment-success";
    }

    /**
     * Trang thanh toán thất bại
     */
    @GetMapping("/failure")
    public String paymentFailure(@RequestParam(required = false) String orderNumber, Model model) {
        log.info("Hiển thị trang thanh toán thất bại cho đơn hàng: {}", orderNumber);

        if (orderNumber != null) {
            Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
            if (order != null) {
                model.addAttribute("order", order);
            }
            model.addAttribute("orderNumber", orderNumber);
        }

        return "module/payment/payment-failure";
    }

    /**
     * Lấy IP address của client
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // Nếu có nhiều IP (qua proxy), lấy IP đầu tiên
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    /**
     * Lấy base URL của request
     */
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath);
        return url.toString();
    }
}
