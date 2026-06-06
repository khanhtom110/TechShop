package com.example.TechShop.service;

import com.example.TechShop.dto.request.OrderRequest;
import com.example.TechShop.dto.response.OrderResponse;
import com.example.TechShop.entity.*;
import com.example.TechShop.exception.extended.AppException;
import com.example.TechShop.exception.extended.ResourceNotFoundException;
import com.example.TechShop.repository.OrderRepository;
import com.example.TechShop.repository.ProductVariantRepository;
import com.example.TechShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductVariantRepository productVariantRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getCart() == null || user.getCart().getCartItems().isEmpty()) {
            throw new AppException(400, "Giỏ hàng đang trống, không thể đặt hàng");
        }

        Order order = Order.builder()
                .code("ORD-" + System.currentTimeMillis())
                .customerName(request.customerName())
                .customerAddress(request.customerAddress())
                .customerEmail(request.customerEmail())
                .customerPhone(request.customerPhone())
                .description(request.description())
                .user(user)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (CartItem item : user.getCart().getCartItems()) {
            //Kiem tra con so luong trong kho hay khong
            ProductVariant variant = item.getProductVariant();
            if (item.getQuantity() > variant.getStockQuantity()) {
                throw new AppException(400, String.format("Sản phẩm '%s' không đủ số lượng để bán",
                        variant.getProduct().getName()));
            }

            //Tru so luong trong kho
            variant.setStockQuantity(variant.getStockQuantity() - item.getQuantity());

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productVariant(variant)
                    .quantity(item.getQuantity())
                    .price(variant.getPrice())
                    .build();

            order.addOrderDetail(orderDetail);
            BigDecimal tmpTotal = orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(tmpTotal);
        }

        order.setTotal(totalOrderPrice);

        Order savedOrder = orderRepository.save(order);

        //Don dep gio hang
        cartService.clearCart(userId);

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderByCode(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> Objects.equals(role.getName(), "ROLE_ADMIN"));
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "code", code));

        //Truong hop nguoi xem don hang khong phai nguoi dang dang nhap
        if (!userId.equals(order.getUser().getId()) && !isAdmin) {
            throw new AppException(403, "Người đang đăng nhập không phải người được xem đơn hàng");
        }

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newOrderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        //Kiem tra tinh hop le
        if (order.getOrderStatus() == OrderStatus.DELIVERED ||
                order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new AppException(400, "Đơn hàng đã hoàn thành hoặc đã hủy, không thể cập nhật trạng thái mới");
        }

        //Chi duoc huy hang khi chua giao hang
        if (newOrderStatus == OrderStatus.CANCELLED && order.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException(400, "Đơn hàng đã được xử lý hoặc đang giao, không thể tự hủy");
        }

        //Ngay da giao hang toi khach hang
        if (newOrderStatus == OrderStatus.DELIVERED) {
            order.setDeliveryDate(LocalDateTime.now());
        }

        //Neu khach huy thi cong lai so luong hang da dat vao kho
        if (newOrderStatus == OrderStatus.CANCELLED) {
            for (OrderDetail detail : order.getOrderDetailList()) {
                ProductVariant variant = detail.getProductVariant();
                int currentStock = (variant.getStockQuantity() != null)
                        ? variant.getStockQuantity()
                        : 0;
                variant.setStockQuantity(currentStock + detail.getQuantity());
                productVariantRepository.save(variant);
            }
        }

        order.setOrderStatus(newOrderStatus);

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse cancelOrderByCustomer(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!userId.equals(order.getUser().getId())) {
            throw new AppException(403, "Bạn không có quyền hủy đơn hàng của người khác");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException(400, "Đơn hàng đã được xử lý hoặc đang giao, không thể tự hủy");
        }

        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}
