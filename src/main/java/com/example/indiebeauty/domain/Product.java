package com.example.indiebeauty.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name="product")
public class Product {
	@Id
	@SequenceGenerator(name="product_seq_gen", 
			sequenceName="product_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq_gen")
	@Column(name = "productid")
	private int productId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryid")
	private Category category;
	
	@Column(name = "pname")
	private String name;
	
	@Column(name = "pdescription")
	private String description;
	
	@OneToMany
	@JoinColumn(name = "productid")
	private List<ProductImage> imageList;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "udate")
	private Date date;
	
	private int price;
	private int stock;
}
