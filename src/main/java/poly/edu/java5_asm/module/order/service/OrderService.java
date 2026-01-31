package poly.edu.java5_asm.module.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.java5_asm.module.order.dto.request.CheckoutRequest;
import poly.edu.java5_asm.module.order.dto.response.OrderResponse;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.user.entity.User;

import java.util.List;

/**
 * Interface cho Order Service
 */
public interface OrderService {

    // === Create Order ===
    OrderResponse createOrder(User user, CheckoutRequest request);

    // === Order Status Management ===
    OrderResponse confirmOrder(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status);
    OrderResponse cancelOrder(Long orderId);

    // === Payment Status ===
    OrderResponse updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus);
    OrderResponse updatePaymentStatus(String orderNumber, Order.PaymentStatus paymentStatus);
    OrderResponse processPaymentCallback(String orderNumber, String transactionId,
                                         Order.PaymentStatus paymentStatus, String gatewayResponse);

    // === Get Orders ===
    OrderResponse getOrder(Long orderId);
    OrderResponse getOrderByNumber(String orderNumber);
    List<OrderResponse> getUserOrders(User user);
    Page<OrderResponse> getUserOrdersPaginated(User user, Pageable pageable);
    List<OrderResponse> getOrdersByStatus(Order.OrderStatus status);
    
    // === Admin: Get All Orders ===
    Page<OrderResponse> getAllOrdersPaginated(Pageable pageable);
    Page<OrderResponse> getAllOrdersPaginated(Order.OrderStatus status, String search, Pageable pageable);
}
