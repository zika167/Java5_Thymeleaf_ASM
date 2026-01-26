package poly.edu.java5_asm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.dto.request.CheckoutRequest;
import poly.edu.java5_asm.dto.response.OrderItemResponse;
import poly.edu.java5_asm.dto.response.OrderResponse;
import poly.edu.java5_asm.entity.*;
import poly.edu.java5_asm.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    /**
     * Tạo đơn hàng từ giỏ hàng
     */
    @Transactional
    public OrderResponse createOrder(User user, CheckoutRequest request) {
        // Lấy giỏ hàng
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Lấy items trong giỏ
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // Tính tổng tiền
        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.ZERO;
        if ("express".equalsIgnoreCase(request.getShippingMethod())) {
            shippingFee = new BigDecimal("50000");
        } else if ("standard".equalsIgnoreCase(request.getShippingMethod())) {
            shippingFee = new BigDecimal("20000");
        }

        BigDecimal tax = subtotal.multiply(new BigDecimal("0.1")); // 10% thuế
        BigDecimal totalAmount = subtotal.add(shippingFee).add(tax);

        // Tạo đơn hàng
        String orderNumber = generateOrderNumber();
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .tax(tax)
                .totalAmount(totalAmount)
                .shippingMethod(request.getShippingMethod())
                .paymentMethod(request.getPaymentMethod())
                .customerNote(request.getCustomerNote())
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .build();

        order = orderRepository.save(order);
        log.info("Tạo đơn hàng {} cho user {}", orderNumber, user.getId());

        // Tạo order items từ cart items
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .productName(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getPrice())
                    .subtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build();
            orderItemRepository.save(orderItem);

            // Cập nhật tồn kho
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            if (product.getStockQuantity() <= product.getLowStockThreshold()) {
                product.setIsOutOfStock(true);
            }
            productRepository.save(product);
        }

        // Xóa giỏ hàng
        cartItemRepository.deleteByCart(cart);
        log.info("Xóa giỏ hàng sau khi tạo đơn hàng");

        // Gửi email xác nhận đơn hàng
        try {
            emailService.sendOrderConfirmation(order, user);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email: {}", e.getMessage());
            // Don't fail the order creation if email fails
        }

        return getOrderResponse(order);
    }

    /**
     * Xác nhận đơn hàng
     */
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if (!order.getStatus().equals(Order.OrderStatus.PENDING)) {
            throw new RuntimeException("Chỉ có thể xác nhận đơn hàng ở trạng thái PENDING");
        }

        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        log.info("Xác nhận đơn hàng {}", order.getOrderNumber());

        // Gửi email cập nhật trạng thái
        try {
            emailService.sendOrderStatusUpdate(order, order.getUser());
        } catch (Exception e) {
            log.error("Failed to send order status update email: {}", e.getMessage());
        }

        return getOrderResponse(order);
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    @Transactional
    public OrderResponse updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        Order.PaymentStatus oldStatus = order.getPaymentStatus();
        order.setPaymentStatus(paymentStatus);

        if (paymentStatus.equals(Order.PaymentStatus.PAID)) {
            // Tự động xác nhận đơn hàng nếu thanh toán thành công
            if (order.getStatus().equals(Order.OrderStatus.PENDING)) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                order.setConfirmedAt(LocalDateTime.now());
            }
        }

        order = orderRepository.save(order);
        log.info("Cập nhật trạng thái thanh toán đơn hàng {} từ {} thành {}",
                order.getOrderNumber(), oldStatus, paymentStatus);

        // Gửi email thông báo khi trạng thái thanh toán thay đổi
        if (!oldStatus.equals(paymentStatus)) {
            try {
                emailService.sendPaymentStatusUpdate(order, order.getUser());
            } catch (Exception e) {
                log.error("Failed to send payment status update email: {}", e.getMessage());
            }
        }

        return getOrderResponse(order);
    }

    /**
     * Cập nhật trạng thái thanh toán theo mã đơn hàng
     */
    @Transactional
    public OrderResponse updatePaymentStatus(String orderNumber, Order.PaymentStatus paymentStatus) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại: " + orderNumber));
        return updatePaymentStatus(order.getId(), paymentStatus);
    }

    /**
     * Xử lý callback thanh toán từ payment gateway
     *
     * @param orderNumber     Mã đơn hàng
     * @param transactionId   Mã giao dịch từ payment gateway
     * @param paymentStatus   Trạng thái thanh toán
     * @param gatewayResponse Response gốc từ payment gateway
     * @return OrderResponse
     */
    @Transactional
    public OrderResponse processPaymentCallback(String orderNumber, String transactionId,
                                                Order.PaymentStatus paymentStatus, String gatewayResponse) {
        log.info("Xử lý callback thanh toán cho đơn hàng: {}, transactionId: {}, status: {}",
                orderNumber, transactionId, paymentStatus);

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại: " + orderNumber));

        // Kiểm tra đơn hàng đã được xử lý chưa
        if (order.getPaymentStatus().equals(Order.PaymentStatus.PAID)) {
            log.warn("Đơn hàng {} đã được thanh toán trước đó", orderNumber);
            return getOrderResponse(order);
        }

        Order.PaymentStatus oldStatus = order.getPaymentStatus();

        // Cập nhật thông tin thanh toán
        order.setPaymentTransactionId(transactionId);
        order.setPaymentGatewayResponse(gatewayResponse);
        order.setPaymentStatus(paymentStatus);

        // Xử lý theo trạng thái thanh toán
        if (paymentStatus.equals(Order.PaymentStatus.PAID)) {
            // Thanh toán thành công - tự động xác nhận đơn hàng
            if (order.getStatus().equals(Order.OrderStatus.PENDING)) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                order.setConfirmedAt(LocalDateTime.now());
                log.info("Tự động xác nhận đơn hàng {} sau khi thanh toán thành công", orderNumber);
            }
        } else if (paymentStatus.equals(Order.PaymentStatus.FAILED)) {
            log.warn("Thanh toán thất bại cho đơn hàng: {}", orderNumber);
        } else if (paymentStatus.equals(Order.PaymentStatus.REFUNDED)) {
            log.info("Đơn hàng {} đã được hoàn tiền", orderNumber);
        }

        order = orderRepository.save(order);
        log.info("Đã cập nhật trạng thái thanh toán đơn hàng {} từ {} thành {}",
                orderNumber, oldStatus, paymentStatus);

        // Gửi email thông báo
        try {
            emailService.sendPaymentStatusUpdate(order, order.getUser());
        } catch (Exception e) {
            log.error("Failed to send payment notification email for order {}: {}",
                    orderNumber, e.getMessage());
        }

        return getOrderResponse(order);
    }

    /**
     * Cập nhật trạng thái giao hàng
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        order.setStatus(status);

        switch (status) {
            case PROCESSING:
                break;
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                // Hoàn lại tồn kho
                restoreStock(order);
                break;
        }

        order = orderRepository.save(order);
        log.info("Cập nhật trạng thái đơn hàng {} thành {}", order.getOrderNumber(), status);

        // Gửi email cập nhật trạng thái
        try {
            emailService.sendOrderStatusUpdate(order, order.getUser());
        } catch (Exception e) {
            log.error("Failed to send order status update email: {}", e.getMessage());
        }

        return getOrderResponse(order);
    }

    /**
     * Hủy đơn hàng
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if (order.getStatus().equals(Order.OrderStatus.DELIVERED) ||
                order.getStatus().equals(Order.OrderStatus.CANCELLED)) {
            throw new RuntimeException("Không thể hủy đơn hàng ở trạng thái này");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());

        // Hoàn lại tồn kho
        restoreStock(order);

        order = orderRepository.save(order);
        log.info("Hủy đơn hàng {}", order.getOrderNumber());

        return getOrderResponse(order);
    }

    /**
     * Lấy đơn hàng theo ID
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        return getOrderResponse(order);
    }

    /**
     * Lấy đơn hàng theo mã đơn hàng
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        return getOrderResponse(order);
    }

    /**
     * Lấy tất cả đơn hàng của user
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByUserOrderByOrderedAtDesc(user);
        return orders.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy đơn hàng của user với phân trang
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrdersPaginated(User user, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserOrderByOrderedAtDesc(user, pageable);
        return orders.map(this::getOrderResponse);
    }

    /**
     * Lấy tất cả đơn hàng theo trạng thái
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> orders = orderRepository.findByStatusOrderByOrderedAtDesc(status);
        return orders.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi Order entity thành OrderResponse
     */
    private OrderResponse getOrderResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().toString())
                .paymentStatus(order.getPaymentStatus().toString())
                .paymentMethod(order.getPaymentMethod())
                .shippingMethod(order.getShippingMethod())
                .subtotal(order.getSubtotal())
                .shippingFee(order.getShippingFee())
                .tax(order.getTax())
                .totalAmount(order.getTotalAmount())
                .orderedAt(order.getOrderedAt())
                .confirmedAt(order.getConfirmedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .customerNote(order.getCustomerNote())
                .orderItems(itemResponses)
                .build();
    }

    /**
     * Hoàn lại tồn kho khi hủy đơn hàng
     */
    private void restoreStock(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
            product.setIsOutOfStock(false);
            productRepository.save(product);
        }

        log.info("Hoàn lại tồn kho cho đơn hàng {}", order.getOrderNumber());
    }

    /**
     * Tạo mã đơn hàng
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }
}
