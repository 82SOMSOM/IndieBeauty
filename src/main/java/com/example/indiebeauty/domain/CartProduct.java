package com.example.indiebeauty.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CartProduct implements Serializable {

  /* Private Fields */

  private Product product;
  private int quantity;
  private boolean inStock;

  /* JavaBeans Properties */

  public boolean isInStock() { return inStock; }
  public void setInStock(boolean inStock) { this.inStock = inStock; }

  public Product getProduct() { return product; }
  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getTotalPrice() {
	if (product != null) {
//		return product.getListPrice() * quantity;
		return product.getPrice() * quantity;
	}
	else {
		return 0;
	}
  }

  /* Public methods */

  public void incrementQuantity(int quantity) {
	  System.out.println("============== quantity 확인 ============== : " + quantity);
	  this.quantity += quantity;
	  System.out.println("============== 누적 quantity 확인 ============== : " + this.quantity);
  }
}
