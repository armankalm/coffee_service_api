package com.example.coffee_service_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long menuItemId;
    private Integer quantity;
}
