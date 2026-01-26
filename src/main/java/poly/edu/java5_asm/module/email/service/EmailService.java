package poly.edu.java5_asm.service;

import poly.edu.java5_asm.entity.Order;
import poly.edu.java5_asm.entity.User;

/**
 * Service interface for email operations
 */
public interface EmailService {

    /**
     * Send order confirmation email to customer
     *
     * @param order The order to send confirmation for
     * @param user  The user who placed the order
     */
    void sendOrderConfirmation(Order order, User user);

    /**
     * Send order status update email to customer
     *
     * @param order The order with updated status
     * @param user  The user who placed the order
     */
    void sendOrderStatusUpdate(Order order, User user);

    /**
     * Send payment status update email to customer
     *
     * @param order The order with updated payment status
     * @param user  The user who placed the order
     */
    void sendPaymentStatusUpdate(Order order, User user);
}
