package com.example.indiebeauty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="sellerinfo")
@Getter @Setter @ToString
public class SellerInfo {
	@Id
	private String sellerid;
	private String passwd;
	private String sellerName;
	private String phone;
	private String business;

}
