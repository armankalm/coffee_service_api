package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.OrderDto;
import com.example.coffee_service_api.dto.OrderItemDto;
import com.example.coffee_service_api.model.MenuItem;
import com.example.coffee_service_api.model.Order;
import com.example.coffee_service_api.model.OrderItem;
import com.example.coffee_service_api.model.Shop;
import com.example.coffee_service_api.repo.MenuItemRepository;
import com.example.coffee_service_api.repo.OrderRepository;
import com.example.coffee_service_api.repo.ShopRepository;
import com.example.coffee_service_api.service.abs.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = toEntity(orderDto);
        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // обновляем базовые поля
        order.setCustomerName(orderDto.getCustomerName());
        Shop shop = shopRepository.findById(orderDto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        order.setShop(shop);

        // пересобираем айтемы
        order.getItems().clear();
        List<OrderItem> items = orderDto.getItems().stream()
                .map(itemDto -> {
                    MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                            .orElseThrow(() -> new RuntimeException("MenuItem not found"));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.getItems().addAll(items);

        return toDto(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> getAllOrdersByShopId(Long id) {
        return orderRepository.findByShopId(id)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- Mappers ---
    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getCustomerName(),
                order.getShop().getId(),
                order.getItems().stream()
                        .map(item -> new OrderItemDto(
                                item.getMenuItem().getId(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }

    private Order toEntity(OrderDto dto) {
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setShop(shop);

        List<OrderItem> items = dto.getItems().stream()
                .map(itemDto -> {
                    MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                            .orElseThrow(() -> new RuntimeException("MenuItem not found"));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setItems(items);

        return order;
    }
}
