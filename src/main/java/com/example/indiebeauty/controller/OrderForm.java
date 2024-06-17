package com.example.indiebeauty.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderForm {
	private String userId;
	private Date date;
	private int totalPrice;
	private int accountNumber;
	private String address;
	private List<Item> orderItems;
}
