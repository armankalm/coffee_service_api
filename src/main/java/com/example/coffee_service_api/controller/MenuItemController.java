package com.example.coffee_service_api.controller;

import com.example.coffee_service_api.dto.MenuItemDto;
import com.example.coffee_service_api.service.abs.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getMenuItems() {
        List<MenuItemDto> items = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable Long id) {
        MenuItemDto item = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<MenuItemDto> createMenuItem(@RequestBody MenuItemDto dto) {
        MenuItemDto createdItem = menuItemService.createMenuItem(dto);
        return ResponseEntity
                .created(URI.create("/api/menu/" + createdItem.getId()))
                .body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto dto) {
        MenuItemDto updatedItem = menuItemService.updateMenuItem(id, dto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByShopId(shopId));
    }

}
