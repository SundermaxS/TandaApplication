package com.Tanda.service;

import com.Tanda.entity.Cart;
import com.Tanda.entity.CartItem;
import com.Tanda.entity.Product;
import com.Tanda.entity.User;
import com.Tanda.repository.CartItemRepository;
import com.Tanda.repository.CartRepository;
import com.Tanda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getUserCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
        cart.getItems().size(); //заставит Hibernate загрузить items
        return cart;
    }


    public Cart addProduct(User user, Long productId, int quantity) {
        Cart cart = getUserCart(user);
        Product product = productRepository.findById(productId).orElseThrow();

        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .priceAtAdding(product.getPrice() * quantity)
                .build();

        cartItemRepository.save(item);
        return getUserCart(user);
    }

    public Cart updateItem(User user, Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if (!item.getCart().getUser().equals(user)) throw new RuntimeException("Access denied");
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return getUserCart(user);
    }

    public Cart removeItem(User user, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if (!item.getCart().getUser().equals(user)) throw new RuntimeException("Access denied");
        cartItemRepository.delete(item);
        return getUserCart(user);
    }

    public void clearCart(User user) {
        Cart cart = getUserCart(user);
        cartItemRepository.deleteAll(cart.getItems());
    }
}
