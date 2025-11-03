package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.UserDto;
import com.example.coffee_service_api.model.Shop;
import com.example.coffee_service_api.model.User;
import com.example.coffee_service_api.repo.ShopRepository;
import com.example.coffee_service_api.repo.UserRepository;
import com.example.coffee_service_api.service.abs.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    @Override
    public UserDto getCurrentUser() {
        User user = getAuthenticatedUser();
        return toDto(user);
    }

    @Override
    @Transactional
    public UserDto setSelectedShop(Long shopId) {
        User user = getAuthenticatedUser();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        user.setSelectedShop(shop);
        userRepository.save(user);

        return toDto(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .selectedShopId(user.getSelectedShop() != null ? user.getSelectedShop().getId() : null)
                .build();
    }
}
