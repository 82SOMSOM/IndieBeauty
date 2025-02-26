package com.example.indiebeauty.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.indiebeauty.domain.Product;

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
public class UploadReview {
	private Product product;
	private String userId;
	private String content;
	private MultipartFile imageUrl;
	private float star;
	private int productId;
}
