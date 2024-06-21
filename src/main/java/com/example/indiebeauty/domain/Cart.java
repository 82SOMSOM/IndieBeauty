package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.support.PagedListHolder;

@SuppressWarnings("serial")
public class Cart implements Serializable {

    private final Map<Integer, CartProduct> productMap = Collections.synchronizedMap(new HashMap<Integer, CartProduct>());
    private final PagedListHolder<CartProduct> productList = new PagedListHolder<CartProduct>();

    public Cart() { this.productList.setPageSize(4); }
    public Iterator<CartProduct> getAllCartProducts() { return productList.getSource().iterator(); }
    public PagedListHolder<CartProduct> getCartProductList() { return productList; }
    public int getNumberOfProducts() { return productList.getSource().size(); }
    public boolean containsProductId(int workingProductId) { return productMap.containsKey(workingProductId); }

    public void addProduct(Product product, boolean isInStock, int quantity) {
        CartProduct cartProduct = productMap.get(product.getProductId());
        if (cartProduct == null) {
            cartProduct = new CartProduct();
            cartProduct.setProduct(product);
            cartProduct.setQuantity(0);
            cartProduct.setInStock(isInStock);
            productMap.put(product.getProductId(), cartProduct);
            productList.getSource().add(cartProduct);
        }
        cartProduct.incrementQuantity(quantity);
    }

    public Product removeProductById(int productId) {
        CartProduct cartProduct = productMap.remove(productId);
        if (cartProduct == null) {
            return null;
        } else {
            productList.getSource().remove(cartProduct);
            return cartProduct.getProduct();
        }
    }

    public void incrementQuantityByProductId(int productId, int quantity) {
        CartProduct cartProduct = productMap.get(productId);
        cartProduct.incrementQuantity(quantity);
    }

    public void setQuantityByProductId(int productId, int quantity) {
        CartProduct cartProduct = productMap.get(productId);
        cartProduct.setQuantity(quantity);
    }

    public double getSubTotal() {
        double subTotal = 0;
        Iterator<CartProduct> items = getAllCartProducts();
        while (items.hasNext()) {
            CartProduct cartItem = (CartProduct) items.next();
            Product product = cartItem.getProduct();
            double listPrice = product.getPrice();
            int quantity = cartItem.getQuantity();
            subTotal += listPrice * quantity;
        }
        return subTotal;
    }
}
