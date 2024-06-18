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
import jakarta.persistence.ManyToOne;
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
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="review")
public class Review {
	@Id
	@SequenceGenerator(name="review_seq_gen", 
			sequenceName="review_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="review_seq_gen")
	@Column(name="reviewid")
	private int reviewId;
	
	@Column(name = "userid", nullable = false)
	private String userId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="reviewdate")
	private Date reviewDate;
	
	@Column(name="content", length = 20)
	private String content;
	
	@Column(name="imageurl", length = 200)
	private String imageUrl;
	
	@Column(name="star")
	private float star;
	
	@Column(name="productid", nullable = false)
	private int productId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", insertable = false, updatable = false)
    private Product product;
}
