package com.example.coffee_service_api.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String address;
    private Long cityId;
}
