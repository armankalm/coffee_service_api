package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.OrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);

    List<OrderDto> getAllOrdersByShopId(Long id);
}
