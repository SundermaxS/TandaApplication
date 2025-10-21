package com.Tanda.service;

import com.Tanda.entity.*;
import com.Tanda.repository.CartItemRepository;
import com.Tanda.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public Order checkout(User user) {
        Cart cart = cartService.getUserCart(user);

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalPrice(total)
                .items(cart.getItems().stream()
                        .map(i -> OrderItem.builder()
                                .product(i.getProduct())
                                .quantity(i.getQuantity())
                                .priceAtPurchase(i.getProduct().getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        order.getItems().forEach(i -> i.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        // здесь я очистил корзину после покупки логично в целом
        cartItemRepository.deleteAll(cart.getItems());

        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
