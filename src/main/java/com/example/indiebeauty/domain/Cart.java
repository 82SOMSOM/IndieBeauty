package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addItem(CartItem cartItem) {
        cartItem.setCart(this);
        cartItems.add(cartItem);
    }

    public void removeItem(CartItem cartItem) {
        cartItem.setCart(null);
        cartItems.remove(cartItem);
    }

    public double getSubTotal() {
        double subTotal = 0;
        for (CartItem cartItem : cartItems) {
            subTotal += cartItem.getTotalPrice();
        }
        return subTotal;
    }
}
