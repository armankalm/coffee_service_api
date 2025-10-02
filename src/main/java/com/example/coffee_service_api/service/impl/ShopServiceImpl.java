package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.ShopDto;
import com.example.coffee_service_api.model.City;
import com.example.coffee_service_api.model.Shop;
import com.example.coffee_service_api.repo.CityRepository;
import com.example.coffee_service_api.repo.ShopRepository;
import com.example.coffee_service_api.service.abs.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final CityRepository cityRepository;

    @Override
    public List<ShopDto> getAllShops() {
        return shopRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShopDto getShopById(Long id) {
        return shopRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    @Override
    public ShopDto createShop(ShopDto shopDto) {
        City city = cityRepository.findById(shopDto.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));
        Shop shop = new Shop();
        shop.setName(shopDto.getName());
        shop.setAddress(shopDto.getAddress());
        shop.setCity(city);
        return toDto(shopRepository.save(shop));
    }

    @Override
    public ShopDto updateShop(Long id, ShopDto shopDto) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        City city = cityRepository.findById(shopDto.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));
        shop.setName(shopDto.getName());
        shop.setAddress(shopDto.getAddress());
        shop.setCity(city);
        return toDto(shopRepository.save(shop));
    }

    @Override
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }

    @Override
    public List<ShopDto> getShopsByCityId(Long cityId) {
        return shopRepository.findByCity_Id(cityId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ShopDto toDto(Shop shop) {
        return new ShopDto(shop.getId(), shop.getName(), shop.getAddress(), shop.getCity().getId());
    }

    private Shop toEntity(ShopDto dto) {
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Shop shop = new Shop();
        shop.setName(dto.getName());
        shop.setAddress(dto.getAddress());
        shop.setCity(city);
        return shop;
    }

}
