package com.example.indiebeauty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class SellerInfo {
	@Id
	private String sellerId;
	private String passwd;
	private String sellerName;
	private String phone;
	private String businessRegistration;

}
