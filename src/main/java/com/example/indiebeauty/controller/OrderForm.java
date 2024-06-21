package com.example.indiebeauty.controller;

import java.sql.Date;
import java.util.List;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderForm {
	private final Orders order = new Orders();
}
