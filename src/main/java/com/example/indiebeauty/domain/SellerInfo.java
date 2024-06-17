package com.example.indiebeauty.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial") 
@Entity
@Table(name="sellerinfo")
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerInfo implements Serializable{
	@Id
	private String sellerid;
	private String passwd;
	private String sellername;
	private String businessname;
	private String email;
	private String phone;
	private String address;
	private String business;
}
