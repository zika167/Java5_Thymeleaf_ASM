package poly.edu.java5_asm.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.entity.User;
import poly.edu.java5_asm.repository.OrderItemRepository;
import poly.edu.java5_asm.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OrderItemRepository orderItemRepository;

    @Value("${spring.mail.from}")
    private String fromEmail;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 2000;

    @Override
    @Async
    public void sendOrderConfirmation(Order order, User user) {
        log.info("Sending order confirmation email for order {} to {}", order.getOrderNumber(), user.getEmail());
        
        try {
            String subject = "Xác nhận đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderConfirmationEmail(order, user);
            
            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("Order confirmation email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to send order confirmation email for order {}: {}", order.getOrderNumber(), e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendOrderStatusUpdate(Order order, User user) {
        log.info("Sending order status update email for order {} to {}", order.getOrderNumber(), user.getEmail());
        
        try {
            String subject = "Cập nhật đơn hàng #" + order.getOrderNumber();
            String htmlContent = buildOrderStatusUpdateEmail(order, user);
            
            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("Order status update email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to send order status update email for order {}: {}", order.getOrderNumber(), e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendPaymentStatusUpdate(Order order, User user) {
        log.info("Sending payment status update email for order {} to {}", order.getOrderNumber(), user.getEmail());
        
        try {
            String subject = getPaymentEmailSubject(order);
            String htmlContent = buildPaymentStatusUpdateEmail(order, user);
            
            sendEmailWithRetry(user.getEmail(), subject, htmlContent);
            log.info("Payment status update email sent successfully for order {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to send payment status update email for order {}: {}", order.getOrderNumber(), e.getMessage(), e);
        }
    }

    /**
     * Get email subject based on payment status
     */
    private String getPaymentEmailSubject(Order order) {
        return switch (order.getPaymentStatus()) {
            case PAID -> "Thanh toán thành công - Đơn hàng #" + order.getOrderNumber();
            case FAILED -> "Thanh toán thất bại - Đơn hàng #" + order.getOrderNumber();
            case REFUNDED -> "Hoàn tiền thành công - Đơn hàng #" + order.getOrderNumber();
            case PENDING -> "Chờ thanh toán - Đơn hàng #" + order.getOrderNumber();
        };
    }

    /**
     * Build payment status update email HTML content
     */
    private String buildPaymentStatusUpdateEmail(Order order, User user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItemRepository.findByOrder(order));
        context.setVariable("paymentStatusMessage", getPaymentStatusMessage(order.getPaymentStatus()));
        context.setVariable("isPaymentSuccess", order.getPaymentStatus() == Order.PaymentStatus.PAID);
        
        return templateEngine.process("email/payment-status-email", context);
    }

    /**
     * Get user-friendly payment status message
     */
    private String getPaymentStatusMessage(Order.PaymentStatus status) {
        return switch (status) {
            case PENDING -> "Đơn hàng của bạn đang chờ thanh toán";
            case PAID -> "Thanh toán đã được xác nhận thành công";
            case FAILED -> "Thanh toán không thành công. Vui lòng thử lại hoặc chọn phương thức thanh toán khác";
            case REFUNDED -> "Số tiền đã được hoàn lại vào tài khoản của bạn";
        };
    }

    /**
     * Send email with retry logic (max 3 attempts with exponential backoff)
     */
    private void sendEmailWithRetry(String to, String subject, String htmlContent) throws MessagingException {
        int attempts = 0;
        MessagingException lastException = null;

        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                sendEmail(to, subject, htmlContent);
                return; // Success
            } catch (MessagingException e) {
                lastException = e;
                attempts++;
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    long delay = RETRY_DELAY_MS * (long) Math.pow(2, attempts - 1); // Exponential backoff
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

    /**
     * Send email using JavaMailSender
     */
    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    /**
     * Build order confirmation email HTML content
     */
    private String buildOrderConfirmationEmail(Order order, User user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItemRepository.findByOrder(order));
        
        return templateEngine.process("email/order-confirmation-email", context);
    }

    /**
     * Build order status update email HTML content
     */
    private String buildOrderStatusUpdateEmail(Order order, User user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItemRepository.findByOrder(order));
        context.setVariable("statusMessage", getStatusMessage(order.getStatus()));
        
        return templateEngine.process("email/order-status-update-email", context);
    }

    /**
     * Get user-friendly status message
     */
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
}
