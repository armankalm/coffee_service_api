package com.example.coffee_service_api.dto;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String customerName;
    private Long shopId;
    private List<OrderItemDto> items;
}
