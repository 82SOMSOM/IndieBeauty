package com.example.indiebeauty.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CartProduct implements Serializable {

    private Product product;
    private int quantity;
    private boolean inStock;

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        if (product != null) {
            return product.getPrice() * quantity;
        } else {
            return 0;
        }
    }

    public void incrementQuantity(int quantity) {
        this.quantity += quantity;
    }
}
