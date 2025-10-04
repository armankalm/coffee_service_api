package com.example.coffee_service_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // внутренний PK

    @Column(unique = true)
    private Long telegramId; // уникальный ID из Telegram

    private String username;
    private String firstName;
    private String lastName;
    private String photoUrl;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(telegramId, user.telegramId) && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(photoUrl, user.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, username, firstName, lastName, photoUrl);
    }
}

