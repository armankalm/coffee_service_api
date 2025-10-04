package com.example.coffee_service_api.repo;

import com.example.coffee_service_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
