package com.example.indiebeauty.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//컴파일 경고를 사용하지 않도록 하는 어노테이션
@SuppressWarnings("serial") 
@Entity
@Getter @Setter @ToString
public class UserInfo implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String userid;
	private String passwd;
	private String username;
	private String email;
	private String phone;
	private String address;
}
