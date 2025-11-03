package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.AuthResponse;
import com.example.coffee_service_api.dto.LoginRequest;
import com.example.coffee_service_api.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
