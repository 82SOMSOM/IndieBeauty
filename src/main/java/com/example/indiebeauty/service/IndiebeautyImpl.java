package com.example.indiebeauty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.indiebeauty.domain.Product;
import com.example.indiebeauty.domain.Review;
import com.example.indiebeauty.domain.SellerInfo;
import com.example.indiebeauty.domain.UserInfo;
import com.example.indiebeauty.repository.OrdersRepository;
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
	@Autowired
	private OrdersRepository ordersRepository;
	
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
	public boolean existsUserId(String userid) {
	    return userRepository.existsById(userid);
	}
	public void deleteAllUserRelatedData(String userid) {
	    ordersRepository.deleteByUserId(userid);
	    reviewRepository.deleteByUserId(userid);
	}
	public void deleteUserInfo(String userId) {
	    deleteAllUserRelatedData(userId); // 자식 레코드 먼저 삭제
	    userRepository.deleteById(userId); // 그 다음 사용자 레코드 삭제
	}
	
	@Override
	public boolean isProductInStock(int workingProductId) {
		return productRepository.existsByProductIdAndStockGreaterThan(workingProductId, 0);
	}
	
	@Override
	public Product getProduct(int workingProductId) {
		return null;
	}
	
	
// 	review
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
	public boolean existsSellerId(String sellerid) {
	    return sellerRepository.existsById(sellerid);
	}

}