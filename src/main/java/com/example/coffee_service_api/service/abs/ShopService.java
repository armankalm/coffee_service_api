package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.ShopDto;
import com.example.coffee_service_api.model.Shop;

import java.util.List;

public interface ShopService {
    List<ShopDto> getAllShops();
    ShopDto getShopById(Long id);
    ShopDto createShop(ShopDto shopDto);
    ShopDto updateShop(Long id, ShopDto shopDto);
    void deleteShop(Long id);

    List<ShopDto> getShopsByCityId(Long cityId);
}
