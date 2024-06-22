package com.example.indiebeauty.domain;

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
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="favorites")
public class Favorites {
	@Id
	@SequenceGenerator(name="fav_seq_gen",
		sequenceName="fav_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fav_seq_gen")
	private int favId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userid", referencedColumnName="userid")  // UserInfo의 userid와 조인
	private UserInfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="businessname", referencedColumnName="businessname")  // SellerInfo의 businessname과 조인
	private SellerInfo sellerinfo;
	
}
