package com.example.coffee_service_api.controller;

import com.example.coffee_service_api.dto.*;
import com.example.coffee_service_api.service.abs.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<SendCodeResponse> sendCode(@RequestBody SendCodeRequest request) {
        return ResponseEntity.ok(authService.sendVerificationCode(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthResponse> verifyCode(@RequestBody VerifyCodeRequest request) {
        return ResponseEntity.ok(authService.verifyCode(request));
    }
}
