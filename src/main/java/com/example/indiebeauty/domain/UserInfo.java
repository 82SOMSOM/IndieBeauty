package com.example.indiebeauty.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial") 
@Entity
@Table(name="userinfo")
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable{
	@Id
	private String userid;
	private String passwd;
	private String username;
	private String email;
	private String phone;
	private String address;

    @ManyToMany(mappedBy = "participants")
    private Set<SellerEvents> events = new HashSet<>();
}
