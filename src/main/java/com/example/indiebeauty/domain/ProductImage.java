package com.example.indiebeauty.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@Table(name="productimage")
public class ProductImage {
	@Id
	@SequenceGenerator(name="productimage_seq_gen", 
			sequenceName="productimage_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="productimage_seq_gen")
	private int imageId;
	@Column(name="product_id")
	private int productId;
	@Column(name="image_url")
	private String imageUrl;
	@Column(name="is_title_img")
	private int isTitleImg;

	public ProductImage() {
		super();
	}
	
	public ProductImage(int imageId, int productId, String imageUrl, int isTitleImg) {
		super();
		this.imageId = imageId;
		this.productId = productId;
		this.imageUrl = imageUrl;
		this.isTitleImg = isTitleImg;
	}
}
