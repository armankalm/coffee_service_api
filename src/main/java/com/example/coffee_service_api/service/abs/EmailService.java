package com.example.coffee_service_api.service.abs;

public interface EmailService {
    void sendVerificationCode(String to, String code);
}
