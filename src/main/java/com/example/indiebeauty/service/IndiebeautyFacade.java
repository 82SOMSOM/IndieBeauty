package com.example.indiebeauty.service;

import java.util.List;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.domain.UserInfo;

public interface IndiebeautyFacade {
//	user
	UserInfo getUserInfo(String userid);
	UserInfo getUserInfo(String userid, String passwd);
	void insertUserInfo(UserInfo userinfo);
	void updateUserInfo(UserInfo userinfo);
	boolean existsUserId(String userid);
	void deleteAllUserRelatedData(String userid);
	void deleteUserInfo(String userid);
	
	// Cart
	boolean isProductInStock(int workingProductId);
	Product getProduct(int workingProductId);

	// Review
	Review getReview(int reviewId);
	void insertReview(Review review);
	
//	seller
	SellerInfo getSellerInfo(String sellerid);
	SellerInfo getSellerInfo(String selllerid, String passwd);
	void insertSellerInfo(SellerInfo sellerinfo);
	void updateSellerInfo(SellerInfo sellerinfo);
	boolean existsSellerId(String sellerid);
//	void deleteAllSellerRelatedData(String sellerid);
//	void deleteSellerInfo(String sellerid);
}
