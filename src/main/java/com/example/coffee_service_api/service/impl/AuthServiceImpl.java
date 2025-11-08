package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.*;
import com.example.coffee_service_api.exception.BadRequestException;
import com.example.coffee_service_api.exception.UnauthorizedException;
import com.example.coffee_service_api.exception.ValidationException;
import com.example.coffee_service_api.model.User;
import com.example.coffee_service_api.model.VerificationCode;
import com.example.coffee_service_api.repo.UserRepository;
import com.example.coffee_service_api.repo.VerificationCodeRepository;
import com.example.coffee_service_api.security.JwtUtil;
import com.example.coffee_service_api.service.abs.AuthService;
import com.example.coffee_service_api.service.abs.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Value("${verification.code.length:6}")
    private int codeLength;

    @Value("${verification.code.expiration:300000}")
    private long codeExpiration;

    @Override
    @Transactional
    public SendCodeResponse sendVerificationCode(SendCodeRequest request) {
        String email = request.getEmail();

        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Invalid email address");
        }

        // Delete old codes for this email
        verificationCodeRepository.deleteByEmail(email);

        // Generate verification code
        String code = generateCode();

        // Save verification code to database
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(codeExpiration / 1000))
                .used(false)
                .build();

        verificationCodeRepository.save(verificationCode);

        // Send email
        try {
            emailService.sendVerificationCode(email, code);
            System.out.println("CODE__________ " + code);
        } catch (Exception e) {
            log.error("Failed to send verification code to {}", email, e);
            throw new BadRequestException("Failed to send verification code. Please try again.");
        }

        return SendCodeResponse.builder()
                .message("Verification code sent successfully")
                .email(email)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse verifyCode(VerifyCodeRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        if (code == null || code.isBlank()) {
            throw new ValidationException("Verification code is required");
        }

        // Find verification code
        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired verification code"));

        // Check if code is expired
        if (verificationCode.isExpired()) {
            throw new UnauthorizedException("Verification code has expired");
        }

        // Mark code as used
        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        // Find or create user
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            if (request.getFullname() == null || request.getFullname().isBlank()) {
                throw new ValidationException("Full name must be filled");
            }

            user = createNewUser(email, request.getFullname());
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    private User createNewUser(String email, String fullname) {
        // Generate username from email
        String username = email.split("@")[0] + "_" + System.currentTimeMillis();

        User user = User.builder()
                .username(username)
                .email(email)
                .fullname(fullname)
                .password("") // No password needed for email auth
                .role(User.Role.USER)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
