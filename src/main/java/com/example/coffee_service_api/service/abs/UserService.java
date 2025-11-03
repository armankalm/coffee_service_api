package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.UserDto;

public interface UserService {
    UserDto getCurrentUser();
    UserDto setSelectedShop(Long shopId);
}
