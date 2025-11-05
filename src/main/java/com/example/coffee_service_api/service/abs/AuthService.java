package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.*;

public interface AuthService {
    SendCodeResponse sendVerificationCode(SendCodeRequest request);
    AuthResponse verifyCode(VerifyCodeRequest request);
}
