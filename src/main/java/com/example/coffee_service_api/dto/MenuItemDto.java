package com.example.coffee_service_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private int price;
    private Long shopId;
}
