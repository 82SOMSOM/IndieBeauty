package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Orders implements Serializable{
	@Id
	@SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
	@Column(name = "orderid")
	private Long orderId;

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
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> orderItems = new ArrayList<>();

}
