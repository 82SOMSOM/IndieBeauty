package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.support.PagedListHolder;

@SuppressWarnings("serial")
public class Cart implements Serializable {

  /* Private Fields */

  private final Map<Integer, CartItem> itemMap = Collections.synchronizedMap(new HashMap<Integer, CartItem>());
	
  private final PagedListHolder<CartItem> itemList = new PagedListHolder<CartItem>();

  /* JavaBeans Properties */

  public Cart() {
	this.itemList.setPageSize(4);
  }

  public Iterator<CartItem> getAllCartItems() { return itemList.getSource().iterator(); }
  public PagedListHolder<CartItem> getCartItemList() { return itemList; }
  public int getNumberOfItems() { return itemList.getSource().size(); }

  /* Public Methods */

  public boolean containsProductId(int workingProductId) {
    return itemMap.containsKey(workingProductId);
  }

  public void addProduct(Product product, boolean isInStock) {
    CartItem cartItem = itemMap.get(product.getProductId());
    if (cartItem == null) {
      cartItem = new CartItem();
      cartItem.setProduct(product);
      cartItem.setQuantity(0);
      cartItem.setInStock(isInStock);
      itemMap.put(product.getProductId(), cartItem);
      itemList.getSource().add(cartItem);
    }
    cartItem.incrementQuantity();
  }


  public Product removeProductById(String productId) {
    CartItem cartItem = itemMap.remove(productId);
    if (cartItem == null) {
      return null;
    }
	else {
      itemList.getSource().remove(cartItem);
      return cartItem.getProduct();
    }
  }

  public void incrementQuantityByProductId(int productId) {
    CartItem cartItem = itemMap.get(productId);
    cartItem.incrementQuantity();
  }

  public void setQuantityByProductId(int productId, int quantity) {
    CartItem cartItem = itemMap.get(productId);
    cartItem.setQuantity(quantity);
  }

  public double getSubTotal() {
    double subTotal = 0;
    Iterator<CartItem> items = getAllCartItems();
    while (items.hasNext()) {
      CartItem cartItem = (CartItem) items.next();
      Product product = cartItem.getProduct();
//      double listPrice = product.getListPrice();
      double listPrice = product.getPrice();
      int quantity = cartItem.getQuantity();
      subTotal += listPrice * quantity;
    }
    return subTotal;
  }
}
