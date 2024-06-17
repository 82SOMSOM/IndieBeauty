package com.example.indiebeauty.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.indiebeauty.domain.ProductImage;

@Service
public class ProductImageService {
	
	public ProductImage getTitleImage(List<ProductImage> piList) {
		if (piList.size() > 0) {
			for (ProductImage pi : piList) {
				if (pi.getIsTitleImg() == 1) {
					return pi;
				}
			}
			return null;
		} else {
			return null;
		}
	}
}
