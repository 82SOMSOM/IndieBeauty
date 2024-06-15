package com.example.indiebeauty.domain;

import java.io.Serializable;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
@IdClass(CartItemPK.class) // composite key class
public class CartItem implements Serializable {

	/* Private Fields */
	@Id
	private String userId;

	@Id
	@Column(name="linenum")
	private int lineNumber;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@ManyToOne
	@JoinColumn(name="itemid")
	private Item item;
	
	private int quantity;

	@Type(type = "org.hibernate.type.TrueFalseType")
	//@Transient
	private boolean inStock;

	/* JavaBeans Properties */

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		if (item != null) {
			return item.getListPrice() * quantity;
		} else {
			return 0;
		}
	}

	/* Public methods */

	public void incrementQuantity() {
		quantity++;
	}
	
	//////////////////////////////////
	public void increaseQuantity(int qty) {
		quantity += qty;
	}
	////////////////////////////////
}
