package com.example.indiebeauty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class UserInfo {
	@Id
	private String userId;
	
	private String passwd;
	private String userName;
	private String email;
	private String phone;
	private String address;
}
