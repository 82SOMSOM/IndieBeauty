package com.example.indiebeauty.controller;

import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.domain.SellerEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventForm {
	private final SellerEvents event = new SellerEvents();
	private MultipartFile eventImage;
}