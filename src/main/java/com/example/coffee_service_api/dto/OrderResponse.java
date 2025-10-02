package com.example.coffee_service_api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String customerName;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
