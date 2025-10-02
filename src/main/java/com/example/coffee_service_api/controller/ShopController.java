package com.example.coffee_service_api.controller;

import com.example.coffee_service_api.dto.ShopDto;
import com.example.coffee_service_api.service.abs.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<List<ShopDto>> getShops() {
        List<ShopDto> shops = shopService.getAllShops();
        return ResponseEntity.ok(shops);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDto> getShop(@PathVariable Long id) {
        ShopDto shop = shopService.getShopById(id);
        return ResponseEntity.ok(shop);
    }

    @PostMapping
    public ResponseEntity<ShopDto> createShop(@RequestBody ShopDto shopDto) {
        ShopDto createdShop = shopService.createShop(shopDto);
        return ResponseEntity
                .created(URI.create("/api/shops/" + createdShop.getId()))
                .body(createdShop);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopDto> updateShop(@PathVariable Long id, @RequestBody ShopDto shopDto) {
        ShopDto updatedShop = shopService.updateShop(id, shopDto);
        return ResponseEntity.ok(updatedShop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<ShopDto>> getShopsByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(shopService.getShopsByCityId(cityId));
    }

}
