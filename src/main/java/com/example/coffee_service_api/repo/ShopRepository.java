package com.example.coffee_service_api.repo;

import com.example.coffee_service_api.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByCity_Id(Long cityId);
}

