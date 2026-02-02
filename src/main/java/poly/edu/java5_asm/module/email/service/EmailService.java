package poly.edu.java5_asm.module.email.service;

/**
 * Service interface for email operations
 */
public interface EmailService {

    /**
     * Send order confirmation email to customer
     *
     * @param orderId The order ID to send confirmation for
     * @param userId  The user ID who placed the order
     */
    void sendOrderConfirmation(Long orderId, Long userId);

    /**
     * Send order status update email to customer
     *
     * @param orderId The order ID with updated status
     * @param userId  The user ID who placed the order
     */
    void sendOrderStatusUpdate(Long orderId, Long userId);

    /**
     * Send payment status update email to customer
     *
     * @param orderId The order ID with updated payment status
     * @param userId  The user ID who placed the order
     */
    void sendPaymentStatusUpdate(Long orderId, Long userId);

    /**
     * Send order cancellation apology email to customer
     *
     * @param orderId The order ID that was cancelled
     * @param userId  The user ID who placed the order
     */
    void sendOrderCancellationApology(Long orderId, Long userId);
}
