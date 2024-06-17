package com.example.indiebeauty.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indiebeauty.domain.Item;
import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.repository.ProductRepository;
import com.example.indiebeauty.repository.ReviewRepository;
import com.example.indiebeauty.repository.SellerRepository;
import com.example.indiebeauty.repository.UserRepository;

@Service
@Transactional
public class IndiebeautyImpl implements IndiebeautyFacade {
//	user
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductRepository productRepository;
	
	public UserInfo getUserInfo(String userid) {
		return userRepository.getReferenceById(userid);
	}
	public UserInfo getUserInfo(String userId, String passwd) {
		return userRepository.findByUseridAndPasswd(userId, passwd);
	}
	public void insertUserInfo(UserInfo userinfo) {
		userRepository.save(userinfo);
	}
	public void updateUserInfo(UserInfo userinfo) {
		userRepository.save(userinfo);
	}

//	public List<String> getUserIdList(){
//	return userRepository.findUserIds();
//}
	
	@Override
	public boolean isProductInStock(int workingProductId) {
		return productRepository.existsByProductIdAndStockGreaterThan(workingProductId, 0);
	}
	
//	@Override
//	public Product getProduct(int workingProductId) {
////		return productRepository.getReferenceById(itemId);
//		return null;
//	}
	
	@Override
	public Review getReview(int reviewId) {
		return reviewRepository.getReferenceById(reviewId);
	}
	
	@Override
	public void insertReview(Review review) {
		reviewRepository.save(review);
	}
	
//	seller
	@Autowired
	private SellerRepository sellerRepository;
	public SellerInfo getSellerInfo(String sellerid) {
		return sellerRepository.getReferenceById(sellerid);
	}
	public SellerInfo getSellerInfo(String userId, String passwd) {
		return sellerRepository.findBySelleridAndPasswd(userId, passwd);
	}
	public void insertSellerInfo(SellerInfo sellerinfo) {
		sellerRepository.save(sellerinfo);
	}
	public void updateSellerInfo(SellerInfo sellerinfo) {
		sellerRepository.save(sellerinfo);
	}

}
