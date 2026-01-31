package poly.edu.java5_asm.module.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Value("${spring.mail.from:noreply@grocerystore.com}")
    private String fromEmail;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 2000;

    @Override
    // @Async - Tạm bỏ để debug
    @Transactional(readOnly = true)
    public void sendOrderConfirmation(Long orderId, Long userId) {
        log.info("=== START sendOrderConfirmation === orderId={}, userId={}", orderId, userId);

        try {
            // Query fresh data within this transaction
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            log.info("Found order {} with {} items for user {}", 
                    order.getOrderNumber(), orderItems.size(), user.getEmail());

            String subject = "Xác nhận đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderConfirmationEmail(order, user, orderItems);

            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderConfirmation === email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderConfirmation === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void sendOrderStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendOrderStatusUpdate === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            log.info("Found order {} with status {} for user {}", 
                    order.getOrderNumber(), order.getStatus(), user.getEmail());

            String subject = "Cập nhật đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderStatusUpdateEmail(order, user, orderItems);

            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("=== END sendOrderStatusUpdate === email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("=== ERROR sendOrderStatusUpdate === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void sendPaymentStatusUpdate(Long orderId, Long userId) {
        log.info("=== START sendPaymentStatusUpdate === orderId={}, userId={}", orderId, userId);

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

            log.info("Found order {} with payment status {} for user {}", 
                    order.getOrderNumber(), order.getPaymentStatus(), user.getEmail());

            String subject = getPaymentEmailSubject(order);
            String htmlContent = buildPaymentStatusUpdateEmail(order, user, orderItems);

            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("=== END sendPaymentStatusUpdate === email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("=== ERROR sendPaymentStatusUpdate === orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    private String getPaymentEmailSubject(Order order) {
        return switch (order.getPaymentStatus()) {
            case PAID -> "Thanh toán thành công - Đơn hàng #" + order.getOrderNumber();
            case FAILED -> "Thanh toán thất bại - Đơn hàng #" + order.getOrderNumber();
            case REFUNDED -> "Hoàn tiền thành công - Đơn hàng #" + order.getOrderNumber();
            case PENDING -> "Chờ thanh toán - Đơn hàng #" + order.getOrderNumber();
        };
    }

    private String buildOrderConfirmationEmail(Order order, User user, List<OrderItem> orderItems) {
        log.info("Building email for order {}, items count: {}", order.getOrderNumber(), orderItems.size());
        for (OrderItem item : orderItems) {
            log.info("  - Item: productName='{}', quantity={}, subtotal={}", 
                item.getProductName(), item.getQuantity(), item.getSubtotal());
        }
        
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);

        return templateEngine.process("shared/email/order-confirmation-email", context);
    }

    private String buildOrderStatusUpdateEmail(Order order, User user, List<OrderItem> orderItems) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        context.setVariable("statusMessage", getStatusMessage(order.getStatus()));

        return templateEngine.process("shared/email/order-status-update-email", context);
    }

    private String buildPaymentStatusUpdateEmail(Order order, User user, List<OrderItem> orderItems) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        context.setVariable("paymentStatusMessage", getPaymentStatusMessage(order.getPaymentStatus()));
        context.setVariable("isPaymentSuccess", order.getPaymentStatus() == Order.PaymentStatus.PAID);

        return templateEngine.process("shared/email/payment-status-email", context);
    }

    private String getPaymentStatusMessage(Order.PaymentStatus status) {
        return switch (status) {
            case PENDING -> "Đơn hàng của bạn đang chờ thanh toán";
            case PAID -> "Thanh toán đã được xác nhận thành công";
            case FAILED -> "Thanh toán không thành công. Vui lòng thử lại hoặc chọn phương thức thanh toán khác";
            case REFUNDED -> "Số tiền đã được hoàn lại vào tài khoản của bạn";
        };
    }

    private String getStatusMessage(Order.OrderStatus status) {
        return switch (status) {
            case PENDING -> "Đơn hàng của bạn đang chờ xác nhận";
            case CONFIRMED -> "Đơn hàng của bạn đã được xác nhận";
            case PROCESSING -> "Đơn hàng của bạn đang được xử lý";
            case SHIPPED -> "Đơn hàng của bạn đã được giao cho đơn vị vận chuyển";
            case DELIVERED -> "Đơn hàng của bạn đã được giao thành công";
            case CANCELLED -> "Đơn hàng của bạn đã bị hủy";
        };
    }

    private void sendEmailWithRetry(String to, String subject, String htmlContent) throws MessagingException {
        int attempts = 0;
        MessagingException lastException = null;

        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                sendEmail(to, subject, htmlContent);
                return;
            } catch (MessagingException e) {
                lastException = e;
                attempts++;
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    long delay = RETRY_DELAY_MS * (long) Math.pow(2, attempts - 1);
                    log.warn("Email sending failed (attempt {}/{}), retrying in {}ms...", attempts, MAX_RETRY_ATTEMPTS, delay);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new MessagingException("Email sending interrupted", ie);
                    }
                }
            }
        }
        log.error("Failed to send email after {} attempts", MAX_RETRY_ATTEMPTS);
        throw lastException;
    }

    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
