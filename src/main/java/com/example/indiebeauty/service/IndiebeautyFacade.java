package com.example.indiebeauty.service;

import java.util.List;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.domain.UserInfo;

public interface IndiebeautyFacade {
	UserInfo getUserInfo(String userid);
	
	UserInfo getUserInfo(String userid, String passwd);
	
	void insertUserInfo(UserInfo userinfo);
	void updateUserInfo(UserInfo userinfo);
//	List<String> getUserIdList();

	// Cart
	boolean isProductInStock(int workingProductId);
	Product getProduct(int workingProductId);

	// Review
	Review getReview(int reviewId);
	void insertReview(Review review);
}
