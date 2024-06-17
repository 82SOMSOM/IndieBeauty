package com.example.indiebeauty.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	private int reviewId;
	private String userId;
	private Date reviewDate;
	private String content;
	private MultipartFile imageUrl;
	private float star;
	private int productId;
}
