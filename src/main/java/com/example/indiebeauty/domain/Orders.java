package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "orders")
public class Orders implements Serializable {
	@Id
	@SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
	@Column(name = "orderid")
	private int orderId;

	@Column(name = "userid")
	private String userId;

	@Temporal(TemporalType.DATE)
	@Column(name = "orderdate")
	private Date date;

	@Column(name = "totalprice")
	private int totalPrice;

	@Column(name = "isorderpaid")
	private int isOrderPaid;

	@Column(name = "accountnumber")
	private int accountNumber;

	private String address;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="orderid")
	private List<Item> orderItems;

	public void initOrder(UserInfo userInfo, Cart cart) {
		userId = userInfo.getUserid();
		date = new Date();
		this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
		totalPrice = (int) cart.getSubTotal();

		Iterator<CartProduct> i = cart.getAllCartProducts();
		while (i.hasNext()) {
			CartProduct cartItem = (CartProduct) i.next();
			addItem(cartItem);
		}
	}

	public void addItem(CartProduct cartItem) {
		Item item = new Item();
		item.setProduct(cartItem.getProduct()); // Assuming cartItem has reference to Product
		item.setPrice(cartItem.getProduct().getPrice()); // Set price based on Product price
		item.setQuantity(cartItem.getProduct().getStock());
//		item.setOrder(this); // Set the relationship to this order

		orderItems.add(item);
	}
}
