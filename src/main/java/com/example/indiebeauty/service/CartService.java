package com.example.indiebeauty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.indiebeauty.domain.Cart;
import com.example.indiebeauty.domain.CartItem;
import com.example.indiebeauty.repository.CartItemRepository;
import com.example.indiebeauty.repository.CartRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart addItem(Item item, boolean isInStock) {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setInStock(isInStock);
        cartItem.setQuantity(1);

        cart.addItem(cartItem);
        cartRepository.save(cart);

        return cart;
    }

    public void removeItem(Cart cart, CartItem cartItem) {
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    public List<CartItem> getAllCartItems(Cart cart) {
        return cart.getCartItems();
    }

    public double getSubTotal(Cart cart) {
        return cart.getSubTotal();
    }
}
