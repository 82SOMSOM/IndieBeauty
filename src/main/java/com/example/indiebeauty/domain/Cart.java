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

  private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<String, CartItem>());
	
  private final PagedListHolder<CartItem> itemList = new PagedListHolder<CartItem>();

  /* JavaBeans Properties */

  public Cart() {
	this.itemList.setPageSize(4);
  }

  public Iterator<CartItem> getAllCartItems() { return itemList.getSource().iterator(); }
  public PagedListHolder<CartItem> getCartItemList() { return itemList; }
  public int getNumberOfItems() { return itemList.getSource().size(); }

  /* Public Methods */

  public boolean containsItemId(String itemId) {
    return itemMap.containsKey(itemId);
  }

  //public void addItem(Item item, boolean isInStock) {
  public CartItem addItem(Item item, boolean isInStock) {
    CartItem cartItem = itemMap.get(item.getItemId());
    if (cartItem == null) {
      cartItem = new CartItem();
      cartItem.setItem(item);
      cartItem.setQuantity(0);
      cartItem.setInStock(isInStock);
      itemMap.put(item.getItemId(), cartItem);
      itemList.getSource().add(cartItem);
    }
    cartItem.incrementQuantity();
    return cartItem;
  }

  ///////////////// added  /////////////////////
  public void addCartItem(CartItem ci) {
      itemMap.put(ci.getItem().getItemId(), ci);
      itemList.getSource().add(ci);
  }
  
  //public Item removeItemById(String itemId) {
  public CartItem removeItemById(String itemId) {
    CartItem cartItem = itemMap.remove(itemId);
    if (cartItem == null) {
      return null;
    }
	else {
      itemList.getSource().remove(cartItem);
      //return cartItem.getItem();
      return cartItem;
    }
  }

  //public void incrementQuantityByItemId(String itemId) {
  public CartItem incrementQuantityByItemId(String itemId) {  
	CartItem cartItem = itemMap.get(itemId);
    cartItem.incrementQuantity();
    return cartItem;
  }

  //public void setQuantityByItemId(String itemId, int quantity) {
  public CartItem setQuantityByItemId(String itemId, int quantity) {  
    CartItem cartItem = itemMap.get(itemId);
    cartItem.setQuantity(quantity);    
    return cartItem;
  }

  //////////// added  /////////////////
  public void increaseQuantityByItemId(String itemId, int qty) {
    CartItem cartItem = itemMap.get(itemId);
    cartItem.increaseQuantity(qty);
  }
  
  
  public double getSubTotal() {
    double subTotal = 0;
    Iterator<CartItem> items = getAllCartItems();
    while (items.hasNext()) {
      CartItem cartItem = (CartItem) items.next();
      Item item = cartItem.getItem();
      double listPrice = item.getListPrice();
      int quantity = cartItem.getQuantity();
      subTotal += listPrice * quantity;
    }
    return subTotal;
  }

}
