package com.example.TechShop.dto.response;

import com.example.TechShop.entity.Order;
import com.example.TechShop.entity.OrderDetail;
import com.example.TechShop.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String code,
        BigDecimal total,
        String customerName,
        String customerPhone,
        String customerEmail,
        String customerAddress,
        LocalDateTime deliveryDate,
        Instant createdAt,
        OrderStatus orderStatus,
        List<OrderDetailResponse> orderDetailResponses
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCode(),
                order.getTotal(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getCustomerEmail(),
                order.getCustomerAddress(),
                order.getDeliveryDate(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getOrderDetailList().stream()
                        .map(OrderDetailResponse::from)
                        .toList()
        );
    }
}
