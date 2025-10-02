package com.example.coffee_service_api.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shops")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return Objects.equals(id, shop.id) && Objects.equals(name, shop.name) && Objects.equals(address, shop.address) && Objects.equals(city, shop.city) && Objects.equals(menuItems, shop.menuItems) && Objects.equals(orders, shop.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, city, menuItems, orders);
    }
}
