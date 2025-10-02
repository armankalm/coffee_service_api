package com.example.coffee_service_api.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price; // цена в тенге

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(name, menuItem.name) && Objects.equals(price, menuItem.price) && Objects.equals(shop, menuItem.shop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, shop);
    }
}
