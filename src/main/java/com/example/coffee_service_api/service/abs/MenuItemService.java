package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    List<MenuItemDto> getAllMenuItems();
    MenuItemDto getMenuItemById(Long id);
    MenuItemDto createMenuItem(MenuItemDto menuItemDto);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);

    List<MenuItemDto> getMenuItemsByShopId(Long shopId);
}
