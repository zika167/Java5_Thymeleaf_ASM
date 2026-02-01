package poly.edu.java5_asm.module.order.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.module.order.entity.Order;
import poly.edu.java5_asm.module.order.entity.Order.OrderStatus;
import poly.edu.java5_asm.module.order.repository.OrderRepository;
import poly.edu.java5_asm.module.email.service.EmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Scheduler tự động cập nhật trạng thái đơn hàng mỗi 1 phút
 * Flow: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    // Map trạng thái hiện tại -> trạng thái tiếp theo
    private static final Map<OrderStatus, OrderStatus> STATUS_FLOW = Map.of(
            OrderStatus.PENDING, OrderStatus.CONFIRMED,
            OrderStatus.CONFIRMED, OrderStatus.SHIPPED,
            OrderStatus.SHIPPED, OrderStatus.DELIVERED
    );

    /**
     * Chạy mỗi 1 phút để tự động chuyển trạng thái đơn hàng
     */
    @Scheduled(fixedRate = 60000) // 60 giây = 1 phút
    @Transactional
    public void autoUpdateOrderStatus() {
        log.debug("Bắt đầu auto-update trạng thái đơn hàng...");

        int updatedCount = 0;

        for (OrderStatus currentStatus : STATUS_FLOW.keySet()) {
            List<Order> orders = orderRepository.findByStatusOrderByOrderedAtDesc(currentStatus);

            for (Order order : orders) {
                OrderStatus nextStatus = STATUS_FLOW.get(currentStatus);
                updateOrderStatus(order, nextStatus);
                updatedCount++;
            }
        }

        if (updatedCount > 0) {
            log.info("Đã tự động cập nhật {} đơn hàng", updatedCount);
        }
    }

    private void updateOrderStatus(Order order, OrderStatus newStatus) {
        OrderStatus oldStatus = order.getStatus();
        
        // Nếu đơn hàng đang ở CONFIRMED và chuẩn bị chuyển sang SHIPPED
        // thì tự động HỦY đơn hàng và gửi email xin lỗi
        if (oldStatus == OrderStatus.CONFIRMED && newStatus == OrderStatus.SHIPPED) {
            log.warn("Đơn hàng #{} đang ở CONFIRMED, tự động hủy thay vì chuyển sang SHIPPED", order.getOrderNumber());
            
            order.setStatus(OrderStatus.CANCELLED);
            order.setCancelledAt(LocalDateTime.now());
            orderRepository.save(order);
            
            log.info("Đã tự động hủy đơn hàng #{}", order.getOrderNumber());
            
            // Gửi email xin lỗi khách hàng
            try {
                emailService.sendOrderCancellationApology(order.getId(), order.getUser().getId());
                log.info("Đã gửi email xin lỗi cho đơn hàng #{}", order.getOrderNumber());
            } catch (Exception e) {
                log.error("Lỗi gửi email xin lỗi cho đơn hàng #{}: {}", order.getOrderNumber(), e.getMessage());
            }
            
            return;
        }
        
        order.setStatus(newStatus);

        // Cập nhật timestamp tương ứng
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus) {
            case CONFIRMED -> order.setConfirmedAt(now);
            case SHIPPED -> order.setShippedAt(now);
            case DELIVERED -> {
                order.setDeliveredAt(now);
                // Với COD, khi giao hàng thành công thì coi như đã thanh toán
                if ("COD".equalsIgnoreCase(order.getPaymentMethod()) && 
                    order.getPaymentStatus() != Order.PaymentStatus.PAID) {
                    order.setPaymentStatus(Order.PaymentStatus.PAID);
                    log.info("Đơn hàng COD #{} đã được thanh toán khi giao hàng", order.getOrderNumber());
                }
            }
            default -> {}
        }

        orderRepository.save(order);
        log.info("Đơn hàng #{}: {} -> {}", order.getOrderNumber(), oldStatus, newStatus);

        // Không gửi email tự động khi thay đổi status nữa
    }
}
