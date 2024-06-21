package com.example.indiebeauty.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class UploadProduct {
	private int categoryId;
	
	@NotEmpty(message = "Please wirte down the name of product.")
	private String name;
	
	private String description;
	
	private MultipartFile titleImage;
	
	@Size(max = 4, message = "Detail images must be less than or equal to 4.")
	private List<MultipartFile> detailImageList;
	
	private Date date;
	
    @Min(value = 1, message = "The price must be at least 1 KRW.")
	@Max(value = 1000000, message = "The price must be less than or equal to 1,000,000.")
	private int price;
	
    @Min(value = 1, message = "The stock must be at least one.")
	@Max(value = 100000, message = "The stock quantity must be less than or equal to 100,000.")
	private int stock;
}
