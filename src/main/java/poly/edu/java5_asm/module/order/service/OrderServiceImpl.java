package poly.edu.java5_asm.module.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.exception.CartException;
import poly.edu.java5_asm.common.exception.OrderException;
import poly.edu.java5_asm.common.exception.OrderNotFoundException;
import poly.edu.java5_asm.module.order.dto.request.CheckoutRequest;
import poly.edu.java5_asm.module.order.dto.response.OrderItemResponse;
import poly.edu.java5_asm.module.order.dto.response.OrderResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.cart.entity.CartItem;
import poly.edu.java5_asm.module.cart.repository.CartRepository;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.OrderItem;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.order.repository.OrderItemRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.email.service.EmailService;
import poly.edu.java5_asm.module.address.entity.Address;
import poly.edu.java5_asm.module.address.repository.AddressRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation của OrderService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(User user, CheckoutRequest request) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> CartException.emptyCart());

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw OrderException.emptyCart();
        }

        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.ZERO;
        if ("express".equalsIgnoreCase(request.getShippingMethod())) {
            shippingFee = new BigDecimal("50000");
        } else if ("standard".equalsIgnoreCase(request.getShippingMethod())) {
            shippingFee = new BigDecimal("20000");
        }

        BigDecimal tax = subtotal.multiply(new BigDecimal("0.1"));
        BigDecimal totalAmount = subtotal.add(shippingFee).add(tax);

        Address shippingAddress = null;
        if (request.getShippingAddressId() != null) {
            shippingAddress = addressRepository.findById(request.getShippingAddressId())
                    .orElse(null);
        }
        if (shippingAddress == null) {
            shippingAddress = addressRepository.findByUserAndIsDefaultTrue(user)
                    .orElse(null);
        }

        String orderNumber = generateOrderNumber();
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .shippingAddress(shippingAddress)
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

            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            if (product.getStockQuantity() <= product.getLowStockThreshold()) {
                product.setIsOutOfStock(true);
            }
            productRepository.save(product);
        }

        cartItemRepository.deleteByCart(cart);
        log.info("Xóa giỏ hàng sau khi tạo đơn hàng");

        try {
            emailService.sendOrderConfirmation(order.getId(), user.getId());
        } catch (Exception e) {
            log.error("Failed to send order confirmation email: {}", e.getMessage());
        }

        return getOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getStatus().equals(Order.OrderStatus.PENDING)) {
            throw OrderException.cannotConfirm();
        }

        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        log.info("Xác nhận đơn hàng {}", order.getOrderNumber());

        try {
            emailService.sendOrderStatusUpdate(order.getId(), order.getUser().getId());
        } catch (Exception e) {
            log.error("Failed to send order status update email: {}", e.getMessage());
        }

        return getOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Order.PaymentStatus oldStatus = order.getPaymentStatus();
        order.setPaymentStatus(paymentStatus);

        if (paymentStatus.equals(Order.PaymentStatus.PAID)) {
            if (order.getStatus().equals(Order.OrderStatus.PENDING)) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                order.setConfirmedAt(LocalDateTime.now());
            }
        }

        order = orderRepository.save(order);
        log.info("Cập nhật trạng thái thanh toán đơn hàng {} từ {} thành {}",
                order.getOrderNumber(), oldStatus, paymentStatus);

        if (!oldStatus.equals(paymentStatus)) {
            try {
                emailService.sendPaymentStatusUpdate(order.getId(), order.getUser().getId());
            } catch (Exception e) {
                log.error("Failed to send payment status update email: {}", e.getMessage());
            }
        }

        return getOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updatePaymentStatus(String orderNumber, Order.PaymentStatus paymentStatus) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.byOrderNumber(orderNumber));
        return updatePaymentStatus(order.getId(), paymentStatus);
    }

    @Override
    @Transactional
    public OrderResponse processPaymentCallback(String orderNumber, String transactionId,
                                                Order.PaymentStatus paymentStatus, String gatewayResponse) {
        log.info("Xử lý callback thanh toán cho đơn hàng: {}, transactionId: {}, status: {}",
                orderNumber, transactionId, paymentStatus);

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.byOrderNumber(orderNumber));

        if (order.getPaymentStatus().equals(Order.PaymentStatus.PAID)) {
            log.warn("Đơn hàng {} đã được thanh toán trước đó", orderNumber);
            return getOrderResponse(order);
        }

        Order.PaymentStatus oldStatus = order.getPaymentStatus();

        order.setPaymentTransactionId(transactionId);
        order.setPaymentGatewayResponse(gatewayResponse);
        order.setPaymentStatus(paymentStatus);

        if (paymentStatus.equals(Order.PaymentStatus.PAID)) {
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

        try {
            emailService.sendPaymentStatusUpdate(order.getId(), order.getUser().getId());
        } catch (Exception e) {
            log.error("Failed to send payment notification email for order {}: {}",
                    orderNumber, e.getMessage());
        }

        return getOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(status);

        switch (status) {
            case PROCESSING -> {}
            case SHIPPED -> order.setShippedAt(LocalDateTime.now());
            case DELIVERED -> order.setDeliveredAt(LocalDateTime.now());
            case CANCELLED -> {
                order.setCancelledAt(LocalDateTime.now());
                restoreStock(order);
            }
            default -> {}
        }

        order = orderRepository.save(order);
        log.info("Cập nhật trạng thái đơn hàng {} thành {}", order.getOrderNumber(), status);

        try {
            emailService.sendOrderStatusUpdate(order.getId(), order.getUser().getId());
        } catch (Exception e) {
            log.error("Failed to send order status update email: {}", e.getMessage());
        }

        return getOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus().equals(Order.OrderStatus.DELIVERED) ||
                order.getStatus().equals(Order.OrderStatus.CANCELLED)) {
            throw OrderException.cannotCancel();
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());

        restoreStock(order);

        order = orderRepository.save(order);
        log.info("Hủy đơn hàng {}", order.getOrderNumber());

        return getOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return getOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.byOrderNumber(orderNumber));
        return getOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByUserOrderByOrderedAtDesc(user);
        return orders.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrdersPaginated(User user, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserOrderByOrderedAtDesc(user, pageable);
        return orders.map(this::getOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> orders = orderRepository.findByStatusOrderByOrderedAtDesc(status);
        return orders.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersPaginated(Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByOrderByOrderedAtDesc(pageable);
        return orders.map(this::getOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersPaginated(Order.OrderStatus status, String search, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStatusOrderByOrderedAtDesc(status, pageable);
        } else {
            orders = orderRepository.findAllByOrderByOrderedAtDesc(pageable);
        }
        return orders.map(this::getOrderResponse);
    }

    private OrderResponse getOrderResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProductName())
                        .productImage(item.getProduct().getImageUrl())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        String shippingName = null;
        String shippingPhone = null;
        String shippingAddress = null;
        if (order.getShippingAddress() != null) {
            var addr = order.getShippingAddress();
            shippingName = addr.getRecipientName();
            shippingPhone = addr.getPhone();
            StringBuilder sb = new StringBuilder();
            sb.append(addr.getAddressLine1());
            if (addr.getAddressLine2() != null && !addr.getAddressLine2().isEmpty()) {
                sb.append(", ").append(addr.getAddressLine2());
            }
            if (addr.getState() != null && !addr.getState().isEmpty()) {
                sb.append(", ").append(addr.getState());
            }
            sb.append(", ").append(addr.getCity());
            shippingAddress = sb.toString();
        }

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
                .createdAt(order.getOrderedAt())
                .customerNote(order.getCustomerNote())
                .orderItems(itemResponses)
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userName(order.getUser() != null ? order.getUser().getFullName() : null)
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .shippingName(shippingName)
                .shippingPhone(shippingPhone)
                .shippingAddress(shippingAddress)
                .build();
    }

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

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }
}
