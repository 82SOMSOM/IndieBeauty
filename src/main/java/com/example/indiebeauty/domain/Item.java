package com.example.indiebeauty.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "item")
public class Item implements Serializable{
	@Id
	@SequenceGenerator(name = "item_seq_gen", sequenceName = "item_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
	@Column(name = "itemid")
	private int itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="productid")
	private Product product;

	private int price;

	private int quantity;

    @ManyToOne
    @JoinColumn(name = "orderid")
    private Orders order;
}
