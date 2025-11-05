package com.example.coffee_service_api.repo;

import com.example.coffee_service_api.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByEmailAndCodeAndUsedFalse(String email, String code);

    Optional<VerificationCode> findTopByEmailOrderByCreatedAtDesc(String email);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    void deleteByEmail(String email);
}
