package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.CreateOrderRequest;
import com.example.coffee_service_api.dto.OrderDto;
import com.example.coffee_service_api.dto.OrderItemDto;
import com.example.coffee_service_api.exception.BadRequestException;
import com.example.coffee_service_api.exception.ResourceNotFoundException;
import com.example.coffee_service_api.exception.UnauthorizedException;
import com.example.coffee_service_api.model.*;
import com.example.coffee_service_api.repo.MenuItemRepository;
import com.example.coffee_service_api.repo.OrderRepository;
import com.example.coffee_service_api.repo.ShopRepository;
import com.example.coffee_service_api.repo.UserRepository;
import com.example.coffee_service_api.service.abs.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        User currentUser = getCurrentUser();

        if (currentUser.getSelectedShop() == null) {
            throw new BadRequestException("Please select a shop first");
        }

        Order order = new Order();
        order.setShop(currentUser.getSelectedShop());
        order.setUser(currentUser);
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = request.getItems().stream()
                .map(itemDto -> {
                    MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                            .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setItems(items);

        // Calculate total cost
        int totalCost = items.stream()
                .mapToInt(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
        order.setTotalCost(totalCost);

        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // обновляем базовые поля
        Shop shop = shopRepository.findById(orderDto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
        order.setShop(shop);

        // пересобираем айтемы
        order.getItems().clear();
        List<OrderItem> items = orderDto.getItems().stream()
                .map(itemDto -> {
                    MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                            .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));
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
            throw new ResourceNotFoundException("Order not found");
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

    @Override
    public List<OrderDto> getMyOrders() {
        User currentUser = getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- Helpers ---
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    // --- Mappers ---
    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getShop().getId(),
                order.getItems().stream()
                        .map(item -> new OrderItemDto(
                                item.getMenuItem().getId(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList()),
                order.getTotalCost()
        );
    }
}
