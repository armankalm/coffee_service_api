package com.example.coffee_service_api.controller;

import com.example.coffee_service_api.dto.UserDto;
import com.example.coffee_service_api.service.abs.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/me/selected-shop/{shopId}")
    public ResponseEntity<UserDto> setSelectedShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(userService.setSelectedShop(shopId));
    }
}
