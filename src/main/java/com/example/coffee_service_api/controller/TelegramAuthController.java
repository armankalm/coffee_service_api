package com.example.coffee_service_api.controller;

import com.example.coffee_service_api.auth.JwtUtil;
import com.example.coffee_service_api.auth.TelegramAuthValidator;
import com.example.coffee_service_api.model.User;
import com.example.coffee_service_api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class TelegramAuthController {

    private final TelegramAuthValidator validator;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final long maxAgeSeconds = 86400; // допустимое время freshness (24ч)

    public TelegramAuthController(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration-seconds}") long jwtExp,
            UserRepository userRepository
    ) {
        this.validator = new TelegramAuthValidator(botToken);
        this.userRepository = userRepository;
        this.jwtUtil = new JwtUtil(jwtSecret, jwtExp);
    }

    // Telegram widget обычно делает GET запрос с query params на data-auth-url
    @GetMapping("/telegram")
    public ResponseEntity<?> telegramAuth(@RequestParam Map<String, String> params) {
        // проверка времени auth_date
        String authDateStr = params.get("auth_date");
        if (authDateStr == null) return ResponseEntity.badRequest().body("missing auth_date");

        long authDate;
        try {
            authDate = Long.parseLong(authDateStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("invalid auth_date");
        }

        long now = Instant.now().getEpochSecond();
        if (Math.abs(now - authDate) > maxAgeSeconds) {
            return ResponseEntity.status(401).body("auth_date too old");
        }

        // validate hash
        if (!validator.validate(params)) {
            return ResponseEntity.status(401).body("invalid hash");
        }

        // если валидно — получаем данные
        Long tgId = Long.parseLong(params.get("id"));
        String username = params.get("username");
        String firstName = params.get("first_name");
        String lastName = params.get("last_name");
        String photoUrl = params.get("photo_url");

        User user = userRepository.findById(tgId).orElseGet(() -> {
            User u = new User();
            u.setTelegramId(tgId);
            return u;
        });

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        // генерируем JWT (sub = telegramId)
        String token = jwtUtil.generateToken(String.valueOf(tgId), username != null ? username : "");

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "telegramId", tgId,
                        "username", username,
                        "firstName", firstName,
                        "lastName", lastName,
                        "photoUrl", photoUrl
                )
        ));
    }
}
