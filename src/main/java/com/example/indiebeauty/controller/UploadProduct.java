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
public class UploadProduct {
	private int categoryId;
	private String name;
	private String description;
	private MultipartFile titleImage;
	private List<MultipartFile> detailImageList;
	private Date date;
	private int price;
	private int stock;
}
