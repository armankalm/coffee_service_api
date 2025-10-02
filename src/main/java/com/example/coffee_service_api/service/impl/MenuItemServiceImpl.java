package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.MenuItemDto;
import com.example.coffee_service_api.model.MenuItem;
import com.example.coffee_service_api.model.Shop;
import com.example.coffee_service_api.repo.MenuItemRepository;
import com.example.coffee_service_api.repo.ShopRepository;
import com.example.coffee_service_api.service.abs.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final ShopRepository shopRepository;

    @Override
    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDto getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    @Override
    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        Shop shop = shopRepository.findById(menuItemDto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDto.getName());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setShop(shop);
        return toDto(menuItemRepository.save(menuItem));
    }

    @Override
    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        Shop shop = shopRepository.findById(menuItemDto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        menuItem.setName(menuItemDto.getName());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setShop(shop);
        return toDto(menuItemRepository.save(menuItem));
    }

    @Override
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItemDto> getMenuItemsByShopId(Long shopId) {
        return menuItemRepository.findByShop_Id(shopId).stream()
                .map(this::toDto)
                .toList();
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.getShop().getId());
    }



    private MenuItem toEntity(MenuItemDto dto) {
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setShop(shop);
        return item;
    }
}
